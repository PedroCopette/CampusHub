package br.com.loginfirebase.activities

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import br.com.loginfirebase.MediaUtils
import br.com.loginfirebase.LocalMediaMetadata
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream

class EventPhotosActivity : ComponentActivity() {
    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
        fun ehAutorDaMidia(usuarioId: String?, autorId: String): Boolean = usuarioId != null && usuarioId == autorId
    }

    private val eventId by lazy { sanitizarId(intent.getStringExtra("idEvento").orEmpty()) }
    private val user by lazy { FirebaseAuth.getInstance().currentUser }
    private var authorName by mutableStateOf("")
    private var photos by mutableStateOf<List<File>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authorName = user?.displayName?.takeIf { it.isNotBlank() } ?: user?.email ?: "Usuário"
        user?.uid?.let { uid -> FirebaseFirestore.getInstance().collection("usuarios").document(uid).get().addOnSuccessListener { authorName = it.getString("nome")?.takeIf(String::isNotBlank) ?: authorName } }
        carregarFotos()
        setContent {
            LoginFirebaseTheme {
                val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                    bitmap?.let { salvarBitmap(it) }
                }
                val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                    if (uri != null) salvarDaGaleria(uri)
                }
                Column(Modifier.fillMaxSize().padding(20.dp)) {
                    Text(intent.getStringExtra("nomeEvento") ?: "Fotos do evento", style = MaterialTheme.typography.headlineMedium)
                    Text("As fotos ficam salvas neste dispositivo.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { if (FirebaseAuth.getInstance().currentUser == null) avisoLogin() else camera.launch(null) }, modifier = Modifier.weight(1f)) { Text("Tirar foto") }
                        OutlinedButton(onClick = { if (FirebaseAuth.getInstance().currentUser == null) avisoLogin() else galeria.launch("image/*") }, modifier = Modifier.weight(1f)) { Text("Escolher da galeria") }
                    }
                    Spacer(Modifier.height(12.dp))
                    if (photos.isEmpty()) Text("Ainda não há fotos publicadas para este evento.")
                    else LazyColumn(Modifier.weight(1f)) {
                        items(photos, key = { it.name }) { file ->
                            val bitmap = remember(file.lastModified()) { BitmapFactory.decodeFile(file.absolutePath) }
                            val metadata = remember(file.lastModified()) { LocalMediaMetadata.ler(file) }
                            Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                Column(Modifier.padding(10.dp)) {
                                    val nomeExibido = when {
                                        metadata.usuarioId == user?.uid -> authorName
                                        metadata.usuarioId.isBlank() -> authorName
                                        else -> metadata.nomeAutor
                                    }
                                    Text("Por $nomeExibido · ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(metadata.dataMillis))}")
                                    bitmap?.let { Image(it.asImageBitmap(), contentDescription = "Foto publicada", modifier = Modifier.fillMaxWidth().height(240.dp).padding(vertical = 6.dp)) }
                                    if (user?.uid == metadata.usuarioId) TextButton(onClick = { excluirFoto(file) }) { Text("Excluir minha foto") }
                                }
                            }
                        }
                    }
                    OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
    }

    private fun pastaFotos(): File = File(filesDir, "fotos_eventos/$eventId").apply { mkdirs() }

    private fun carregarFotos() {
        photos = pastaFotos().listFiles()?.filter { it.isFile && it.extension == "jpg" }?.sortedByDescending { it.lastModified() }.orEmpty()
    }

    private fun salvarBitmap(bitmap: Bitmap) {
        try {
            val uid = user?.uid ?: run { avisoLogin(); return }
            val arquivo = File(pastaFotos(), "${uid}_${System.currentTimeMillis()}.jpg")
            FileOutputStream(arquivo).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 88, it) }
            LocalMediaMetadata.salvar(arquivo, uid, authorName)
            carregarFotos()
            Toast.makeText(this, "Foto publicada neste dispositivo.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) { Toast.makeText(this, "Não foi possível salvar a foto.", Toast.LENGTH_LONG).show() }
    }

    private fun excluirFoto(arquivo: File) {
        if (user?.uid != LocalMediaMetadata.ler(arquivo).usuarioId) return
        if (LocalMediaMetadata.excluir(arquivo)) carregarFotos()
        else Toast.makeText(this, "Não foi possível excluir a foto.", Toast.LENGTH_SHORT).show()
    }

    private fun salvarDaGaleria(uri: Uri) {
        try {
            val bitmap = contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
                ?: throw IllegalArgumentException("Imagem indisponível")
            salvarBitmap(bitmap)
        } catch (e: Exception) { Toast.makeText(this, "Não foi possível importar a imagem.", Toast.LENGTH_LONG).show() }
    }

    private fun avisoLogin() = Toast.makeText(this, "Faça login para publicar uma foto.", Toast.LENGTH_SHORT).show()
}
