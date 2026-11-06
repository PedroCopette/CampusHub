
package br.com.loginfirebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
class EventPhotosActivity : ComponentActivity() {

    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
    }

    private val auth = FirebaseAuth.getInstance()

    private var idEvento = ""
    private var nomeEvento = "Evento"

    private var fotos by mutableStateOf<List<FotoPublicada>>(emptyList())

    private val seletorGaleria =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                salvarFotoGaleria(uri)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                salvarFotoCamera(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idEvento = intent.getStringExtra("idEvento") ?: ""
        nomeEvento = intent.getStringExtra("nomeEvento") ?: "Evento"

        fotos = carregarPublicacoes()

        setContent {
            LoginFirebaseTheme {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Fotos do evento",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                TextButton(
                                    onClick = { finish() }
                                ) {
                                    Text("←", fontSize = 28.sp)
                                }
                            }
                        )
                    }
                ) { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(18.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Text(
                            text = nomeEvento,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Compartilhe os registros deste evento.",
                            fontSize = 14.sp
                        )

                        Button(
                            onClick = {
                                if (auth.currentUser == null) {
                                    mostrarMensagem("Faça login para publicar fotos.")
                                } else {
                                    cameraLauncher.launch(null)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("TIRAR FOTO")
                        }

                        OutlinedButton(
                            onClick = {
                                if (auth.currentUser == null) {
                                    mostrarMensagem("Faça login para publicar fotos.")
                                } else {
                                    seletorGaleria.launch("image/*")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ESCOLHER DA GALERIA")
                        }

                        HorizontalDivider()

                        Text(
                            text = "Publicações (${fotos.size})",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (fotos.isEmpty()) {
                            Text(
                                text = "Ainda não existem fotos neste evento."
                            )
                        }

                        fotos.forEach { foto ->

                            key(foto.id) {

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors()
                                ) {

                                    Column(
                                        modifier = Modifier.padding(14.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {

                                        val imagem = remember(foto.arquivo) {
                                            carregarImagem(foto.arquivo)
                                        }

                                        if (imagem != null) {
                                            Image(
                                                bitmap = imagem.asImageBitmap(),
                                                contentDescription = "Foto publicada no evento",
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(220.dp),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Text("Não foi possível carregar esta imagem.")
                                        }

                                        Text(
                                            text = "Publicado por: ${foto.autor}",
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "Data: ${foto.dataPublicacao}",
                                            fontSize = 13.sp
                                        )

                                        if (foto.usuarioId == auth.currentUser?.uid) {
                                            OutlinedButton(
                                                onClick = {
                                                    excluirFoto(foto)
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("EXCLUIR MINHA FOTO")
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }

    private fun diretorioFotos(): File {
        val pasta = File(filesDir, "fotos_eventos")

        if (!pasta.exists() && !pasta.mkdirs()) {
            throw IllegalStateException("Não foi possível criar a pasta de fotos.")
        }

        return pasta
    }

    private fun arquivoPublicacoes(): File {
        val idSeguro = idEvento.replace(Regex("[^A-Za-z0-9_-]"), "_")
        return File(filesDir, "publicacoes_$idSeguro.json")
    }

    private fun salvarFotoGaleria(uri: Uri) {

        if (auth.currentUser == null) {
            mostrarMensagem("Faça login para publicar fotos.")
            return
        }

        try {
            val arquivo = File(
                diretorioFotos(),
                "foto_${UUID.randomUUID()}.img"
            )

            val entrada = contentResolver.openInputStream(uri)
                ?: throw IllegalStateException("Não foi possível abrir a imagem.")

            entrada.use { origem ->
                arquivo.outputStream().use { destino ->
                    origem.copyTo(destino)
                }
            }

            registrarPublicacao(arquivo)

        } catch (e: Exception) {
            mostrarMensagem("Não foi possível salvar a foto da galeria.")
        }
    }

    private fun salvarFotoCamera(bitmap: Bitmap) {

        if (auth.currentUser == null) {
            mostrarMensagem("Faça login para publicar fotos.")
            return
        }

        try {
            val arquivo = File(
                diretorioFotos(),
                "foto_${UUID.randomUUID()}.jpg"
            )

            FileOutputStream(arquivo).use { saida ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, saida)
            }

            registrarPublicacao(arquivo)

        } catch (e: Exception) {
            mostrarMensagem("Não foi possível salvar a foto da câmera.")
        }
    }

    private fun registrarPublicacao(arquivo: File) {

        val usuario = auth.currentUser ?: return

        val nomeAutor = usuario.displayName
            ?.takeIf { it.isNotBlank() }
            ?: usuario.email?.substringBefore("@")
            ?: "Usuário"

        val dataAtual = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val novaFoto = FotoPublicada(
            id = UUID.randomUUID().toString(),
            arquivo = arquivo.name,
            autor = nomeAutor,
            usuarioId = usuario.uid,
            dataPublicacao = dataAtual
        )

        fotos = listOf(novaFoto) + fotos
        salvarPublicacoes()

        mostrarMensagem("Foto publicada!")
    }

    private fun carregarPublicacoes(): List<FotoPublicada> {
        val arquivo = arquivoPublicacoes()
        if (!arquivo.exists()) return emptyList()
        return MediaManager.parseFotosJson(arquivo.readText())
    }

    private fun salvarPublicacoes() {
        try {
            arquivoPublicacoes().writeText(MediaManager.serializarFotosJson(fotos))
        } catch (e: Exception) {
            mostrarMensagem("Não foi possível salvar as publicações.")
        }
    }

    private fun carregarImagem(nomeArquivo: String): Bitmap? {

        val arquivo = File(diretorioFotos(), nomeArquivo)

        if (!arquivo.exists()) {
            return null
        }

        val opcoes = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }

        BitmapFactory.decodeFile(arquivo.absolutePath, opcoes)

        var amostra = 1

        while (
            opcoes.outWidth / amostra > 1200 ||
            opcoes.outHeight / amostra > 1200
        ) {
            amostra *= 2
        }

        val opcoesFinais = BitmapFactory.Options().apply {
            inSampleSize = amostra
        }

        return BitmapFactory.decodeFile(
            arquivo.absolutePath,
            opcoesFinais
        )
    }

    private fun excluirFoto(foto: FotoPublicada) {

        val usuario = auth.currentUser

        if (usuario == null || foto.usuarioId != usuario.uid) {
            mostrarMensagem("Você só pode excluir as próprias fotos.")
            return
        }

        val arquivo = File(diretorioFotos(), foto.arquivo)

        if (arquivo.exists()) {
            arquivo.delete()
        }

        fotos = fotos.filterNot { it.id == foto.id }
        salvarPublicacoes()

        mostrarMensagem("Foto excluída.")
    }

    private fun mostrarMensagem(mensagem: String) {
        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()
    }
}

data class FotoPublicada(
    val id: String,
    val arquivo: String,
    val autor: String,
    val usuarioId: String,
    val dataPublicacao: String
)
