package br.com.loginfirebase.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import br.com.loginfirebase.MediaUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class EventVideoActivity : ComponentActivity() {
    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
        fun validarStatusVideo(tocando: Boolean): String = if (tocando) "Reproduzindo" else "Pausado"
    }

    private val eventId by lazy { sanitizarId(intent.getStringExtra("idEvento").orEmpty()) }
    private val uid by lazy { FirebaseAuth.getInstance().currentUser?.uid }
    private var videos by mutableStateOf<List<File>>(emptyList())
    private var videoCaptura: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carregarVideos()
        setContent {
            LoginFirebaseTheme {
                val camera = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    val uri = result.data?.data
                    val arquivo = videoCaptura
                    if (result.resultCode == RESULT_OK && arquivo != null && arquivo.exists() && arquivo.length() > 0) {
                        salvarVideo(arquivo, uid)
                    } else if (uri != null) importarVideo(uri)
                    else { arquivo?.delete(); videoCaptura = null }
                }
                val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                    if (uri != null) importarVideo(uri)
                }
                Column(Modifier.fillMaxSize().padding(20.dp)) {
                    Text(intent.getStringExtra("nomeEvento") ?: "Vídeos do evento", style = MaterialTheme.typography.headlineMedium)
                    Text("Publique clipes curtos. Os vídeos ficam salvos neste dispositivo.")
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { if (uid == null) avisoLogin() else iniciarCaptura(camera) }, modifier = Modifier.weight(1f)) { Text("Gravar vídeo") }
                        OutlinedButton(onClick = { if (uid == null) avisoLogin() else galeria.launch("video/*") }, modifier = Modifier.weight(1f)) { Text("Escolher da galeria") }
                    }
                    Spacer(Modifier.height(12.dp))
                    if (videos.isEmpty()) Text("Ainda não há vídeos publicados para este evento.")
                    else LazyColumn(Modifier.weight(1f)) {
                        items(videos, key = { it.name }) { arquivo ->
                            val autorUid = arquivo.name.substringBefore('_')
                            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("Vídeo · ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(arquivo.lastModified()))}")
                                    AndroidView(
                                        factory = { context -> VideoView(context).apply { setVideoURI(Uri.fromFile(arquivo)); setMediaController(MediaController(context).also { it.setAnchorView(this) }) } },
                                        update = { view -> if (view.tag != arquivo.absolutePath) { view.tag = arquivo.absolutePath; view.setVideoURI(Uri.fromFile(arquivo)) } },
                                        modifier = Modifier.fillMaxWidth().height(220.dp)
                                    )
                                    if (uid == autorUid) TextButton(onClick = { excluir(arquivo) }) { Text("Excluir meu vídeo") }
                                }
                            }
                        }
                    }
                    OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
    }

    private fun pastaVideos() = File(filesDir, "videos_eventos/$eventId").apply { mkdirs() }
    private fun carregarVideos() { videos = pastaVideos().listFiles()?.filter { it.isFile && it.extension == "mp4" }?.sortedByDescending { it.lastModified() }.orEmpty() }

    private fun iniciarCaptura(launcher: androidx.activity.result.ActivityResultLauncher<Intent>) {
        try {
            val temp = File(cacheDir, "captura_${System.currentTimeMillis()}.mp4")
            val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", temp)
            videoCaptura = temp
            val intentCamera = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, uri)
                putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60)
                putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            if (intentCamera.resolveActivity(packageManager) != null) launcher.launch(intentCamera)
            else { temp.delete(); videoCaptura = null; Toast.makeText(this, "Nenhum app de câmera disponível.", Toast.LENGTH_LONG).show() }
        } catch (e: Exception) { Toast.makeText(this, "Não foi possível abrir a câmera.", Toast.LENGTH_LONG).show() }
    }

    private fun importarVideo(uri: Uri) {
        try {
            val temp = File.createTempFile("import_", ".mp4", cacheDir)
            contentResolver.openInputStream(uri)?.use { input -> temp.outputStream().use(input::copyTo) }
                ?: throw IllegalArgumentException("Vídeo indisponível")
            salvarVideo(temp, uid)
        } catch (e: Exception) { Toast.makeText(this, "Não foi possível importar o vídeo.", Toast.LENGTH_LONG).show() }
    }

    private fun salvarVideo(origem: File, autor: String?) {
        try {
            if (autor == null) { origem.delete(); avisoLogin(); return }
            if (origem.length() > 30L * 1024 * 1024) throw IllegalArgumentException("O vídeo deve ter até 30 MB.")
            val destino = File(pastaVideos(), "${autor}_${System.currentTimeMillis()}.mp4")
            origem.copyTo(destino, overwrite = true)
            origem.delete(); videoCaptura = null; carregarVideos()
            Toast.makeText(this, "Vídeo publicado neste dispositivo.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            origem.delete(); videoCaptura = null
            Toast.makeText(this, e.message ?: "Não foi possível salvar o vídeo.", Toast.LENGTH_LONG).show()
        }
    }

    private fun excluir(arquivo: File) {
        if (uid != arquivo.name.substringBefore('_')) return
        if (arquivo.delete()) carregarVideos() else Toast.makeText(this, "Não foi possível excluir o vídeo.", Toast.LENGTH_SHORT).show()
    }

    private fun avisoLogin() = Toast.makeText(this, "Faça login para publicar um vídeo.", Toast.LENGTH_SHORT).show()
}
