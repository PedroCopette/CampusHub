package br.com.loginfirebase.activities

import android.Manifest
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import br.com.loginfirebase.MediaUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import java.io.File

class EventAudioActivity : ComponentActivity() {
    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
        fun validarStatusGravacao(gravando: Boolean): String = if (gravando) "Gravando" else "Parado"
    }

    private val eventId by lazy { sanitizarId(intent.getStringExtra("idEvento").orEmpty()) }
    private val uid by lazy { FirebaseAuth.getInstance().currentUser?.uid }
    private var audios by mutableStateOf<List<File>>(emptyList())
    private var gravando by mutableStateOf(false)
    private var reproduzindo by mutableStateOf<String?>(null)
    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carregarAudios()
        setContent {
            LoginFirebaseTheme {
                val pedirPermissao = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedida ->
                    if (concedida) iniciarGravacao() else Toast.makeText(this, "Permita o acesso ao microfone para gravar.", Toast.LENGTH_LONG).show()
                }
                Column(Modifier.fillMaxSize().padding(20.dp)) {
                    Text(intent.getStringExtra("nomeEvento") ?: "Áudios do evento", style = MaterialTheme.typography.headlineMedium)
                    Text("Grave um áudio curto relacionado ao evento. Os arquivos ficam neste dispositivo.")
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            if (uid == null) Toast.makeText(this@EventAudioActivity, "Faça login para publicar áudio.", Toast.LENGTH_SHORT).show()
                            else if (gravando) pararGravacao()
                            else pedirPermissao.launch(Manifest.permission.RECORD_AUDIO)
                        }, modifier = Modifier.fillMaxWidth()
                    ) { Text(if (gravando) "Parar gravação" else "Gravar áudio") }
                    if (gravando) Text("Gravando… toque em parar para publicar.", color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(12.dp))
                    if (audios.isEmpty()) Text("Ainda não há áudios publicados para este evento.")
                    else LazyColumn(Modifier.weight(1f)) {
                        items(audios, key = { it.name }) { arquivo ->
                            val autorUid = arquivo.name.substringBefore('_')
                            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                                Column(Modifier.padding(12.dp)) {
                                    Text("Áudio · ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(arquivo.lastModified()))}")
                                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        OutlinedButton(onClick = { if (reproduzindo == arquivo.name) pararReproducao() else reproduzir(arquivo) }, modifier = Modifier.weight(1f)) { Text(if (reproduzindo == arquivo.name) "Parar áudio" else "Reproduzir") }
                                        if (uid == autorUid) TextButton(onClick = { excluir(arquivo) }) { Text("Excluir") }
                                    }
                                }
                            }
                        }
                    }
                    OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
    }

    private fun pastaAudios() = File(filesDir, "audios_eventos/$eventId").apply { mkdirs() }
    private fun carregarAudios() { audios = pastaAudios().listFiles()?.filter { it.isFile && it.extension == "m4a" }?.sortedByDescending { it.lastModified() }.orEmpty() }

    private fun iniciarGravacao() {
        val usuario = uid ?: return
        try {
            val arquivo = File(pastaAudios(), "${usuario}_${System.currentTimeMillis()}.m4a")
            val novoRecorder = criarRecorder()
            novoRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            novoRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            novoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            novoRecorder.setMaxDuration(60_000)
            novoRecorder.setOutputFile(arquivo.absolutePath)
            novoRecorder.setOnInfoListener { _, info, _ -> if (info == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) pararGravacao() }
            novoRecorder.prepare(); novoRecorder.start()
            recorder = novoRecorder; gravando = true
        } catch (e: Exception) {
            recorder?.release(); recorder = null; gravando = false
            Toast.makeText(this, "Não foi possível iniciar a gravação.", Toast.LENGTH_LONG).show()
        }
    }

    @Suppress("DEPRECATION")
    private fun criarRecorder(): MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(this) else MediaRecorder()

    private fun pararGravacao() {
        val atual = recorder ?: return
        try {
            atual.stop()
            Toast.makeText(this, "Áudio publicado neste dispositivo.", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {
            Toast.makeText(this, "Gravação muito curta; tente gravar novamente.", Toast.LENGTH_SHORT).show()
        } finally {
            atual.reset(); atual.release(); recorder = null; gravando = false; carregarAudios()
        }
    }

    private fun reproduzir(arquivo: File) {
        player?.release(); player = null; reproduzindo = arquivo.name
        try {
            player = MediaPlayer().apply {
                setAudioAttributes(AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build())
                setDataSource(arquivo.absolutePath)
                setOnCompletionListener { it.release(); player = null; reproduzindo = null }
                setOnErrorListener { mp, what, extra ->
                    android.util.Log.e("EventAudio", "Erro ao reproduzir áudio: $what / $extra")
                    mp.release(); player = null; reproduzindo = null
                    Toast.makeText(this@EventAudioActivity, "Falha ao reproduzir este áudio.", Toast.LENGTH_LONG).show()
                    true
                }
                prepare(); start()
            }
        } catch (e: Exception) { android.util.Log.e("EventAudio", "Falha ao iniciar reprodução", e); player?.release(); player = null; reproduzindo = null; Toast.makeText(this, "Não foi possível reproduzir o áudio.", Toast.LENGTH_SHORT).show() }
    }

    private fun pararReproducao() {
        player?.run { if (isPlaying) stop(); release() }
        player = null
        reproduzindo = null
    }

    private fun excluir(arquivo: File) {
        if (uid != arquivo.name.substringBefore('_')) return
        if (arquivo.delete()) { if (reproduzindo == arquivo.name) { player?.release(); player = null; reproduzindo = null }; carregarAudios() }
        else Toast.makeText(this, "Não foi possível excluir o áudio.", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        if (gravando) { try { recorder?.stop() } catch (_: Exception) {}; recorder?.release(); recorder = null }
        player?.release(); player = null
        super.onDestroy()
    }
}
