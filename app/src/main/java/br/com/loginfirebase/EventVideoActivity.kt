package br.com.loginfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
class EventVideoActivity : ComponentActivity() {

    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
    }

    private val auth = FirebaseAuth.getInstance()

    private var idEvento = ""
    private var nomeEvento = "Evento"

    private var videos by mutableStateOf<List<VideoPublicado>>(
        emptyList()
    )

    private var videoTocandoId by mutableStateOf<String?>(null)
    private var videoView: VideoView? = null

    private val seletorGaleria =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                copiarEPublicarVideo(uri)
            }
        }

    private val gravadorDeVideo =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { resultado ->

            if (resultado.resultCode == Activity.RESULT_OK) {

                val uri = resultado.data?.data
                    ?: resultado.data?.clipData?.getItemAt(0)?.uri

                if (uri != null) {
                    copiarEPublicarVideo(uri)
                } else {
                    mostrarMensagem(
                        "Não foi possível acessar o vídeo gravado."
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idEvento = intent.getStringExtra("idEvento") ?: ""
        nomeEvento = intent.getStringExtra("nomeEvento") ?: "Evento"

        videos = carregarVideos()

        setContent {
            LoginFirebaseTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Vídeos do evento",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                TextButton(
                                    onClick = {
                                        finish()
                                    }
                                ) {
                                    Text(
                                        text = "←",
                                        fontSize = 28.sp
                                    )
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
                            text = "Compartilhe vídeos relacionados a este evento.",
                            fontSize = 14.sp
                        )

                        Button(
                            onClick = {
                                if (auth.currentUser == null) {
                                    mostrarMensagem(
                                        "Faça login para publicar vídeos."
                                    )
                                } else {
                                    abrirCamera()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("GRAVAR VÍDEO")
                        }

                        OutlinedButton(
                            onClick = {
                                if (auth.currentUser == null) {
                                    mostrarMensagem(
                                        "Faça login para publicar vídeos."
                                    )
                                } else {
                                    seletorGaleria.launch("video/*")
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("ESCOLHER DA GALERIA")
                        }

                        HorizontalDivider()

                        Text(
                            text = "Publicações (${videos.size})",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (videos.isEmpty()) {
                            Text(
                                text = "Ainda não existem vídeos neste evento."
                            )
                        }

                        videos.forEach { video ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors()
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    Text(
                                        text = "Vídeo do evento",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Publicado por: ${video.autor}",
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = "Data: ${video.dataPublicacao}",
                                        fontSize = 13.sp
                                    )

                                    if (videoTocandoId == video.id) {

                                        AndroidView(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(220.dp),
                                            factory = { contexto ->

                                                VideoView(contexto).apply {

                                                    val estaView = this
                                                    videoView = this

                                                    val arquivo = File(
                                                        diretorioVideos(),
                                                        video.arquivo
                                                    )

                                                    setVideoURI(
                                                        Uri.fromFile(arquivo)
                                                    )

                                                    setOnPreparedListener { player ->
                                                        player.isLooping = false
                                                        start()
                                                    }

                                                    setOnCompletionListener {

                                                        if (videoView === estaView) {
                                                            videoView = null
                                                        }

                                                        videoTocandoId = null
                                                    }

                                                    setOnErrorListener { _, _, _ ->

                                                        if (videoView === estaView) {
                                                            videoView = null
                                                        }

                                                        videoTocandoId = null

                                                        mostrarMensagem(
                                                            "Não foi possível reproduzir este vídeo."
                                                        )

                                                        true
                                                    }
                                                }
                                            }
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            if (videoTocandoId == video.id) {
                                                pararVideo()
                                            } else {
                                                pararVideo()

                                                val arquivo = File(
                                                    diretorioVideos(),
                                                    video.arquivo
                                                )

                                                if (arquivo.exists()) {
                                                    videoTocandoId = video.id
                                                } else {
                                                    mostrarMensagem(
                                                        "O arquivo de vídeo não foi encontrado."
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (
                                                videoTocandoId == video.id
                                            ) {
                                                "PARAR VÍDEO"
                                            } else {
                                                "ASSISTIR VÍDEO"
                                            }
                                        )
                                    }

                                    if (
                                        auth.currentUser?.uid == video.usuarioId
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                excluirVideo(video)
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text("EXCLUIR VÍDEO")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun abrirCamera() {

        try {

            val intent = Intent(
                MediaStore.ACTION_VIDEO_CAPTURE
            ).apply {
                putExtra(
                    MediaStore.EXTRA_DURATION_LIMIT,
                    30
                )

                putExtra(
                    MediaStore.EXTRA_VIDEO_QUALITY,
                    1
                )
            }

            gravadorDeVideo.launch(intent)

        } catch (e: Exception) {

            mostrarMensagem(
                "Não foi possível abrir a câmera para gravar vídeo."
            )
        }
    }

    private fun diretorioVideos(): File {

        val pasta = File(
            filesDir,
            "videos_eventos"
        )

        if (!pasta.exists() && !pasta.mkdirs()) {
            throw IllegalStateException(
                "Não foi possível criar a pasta de vídeos."
            )
        }

        return pasta
    }

    private fun arquivoPublicacoes(): File {

        val idSeguro = idEvento.replace(
            Regex("[^A-Za-z0-9_-]"),
            "_"
        )

        return File(
            filesDir,
            "publicacoes_videos_$idSeguro.json"
        )
    }

    private fun copiarEPublicarVideo(uri: Uri) {

        if (auth.currentUser == null) {
            mostrarMensagem(
                "Faça login para publicar vídeos."
            )
            return
        }

        var arquivo: File? = null

        try {

            arquivo = File(
                diretorioVideos(),
                "video_${UUID.randomUUID()}.mp4"
            )

            val entrada = contentResolver.openInputStream(uri)
                ?: throw IllegalStateException(
                    "Não foi possível abrir o vídeo."
                )

            entrada.use { origem ->
                arquivo.outputStream().use { destino ->
                    origem.copyTo(destino)
                }
            }

            if (arquivo.length() == 0L) {
                arquivo.delete()

                mostrarMensagem(
                    "O arquivo de vídeo está vazio."
                )

                return
            }

            registrarPublicacao(arquivo)

        } catch (e: Exception) {

            arquivo?.delete()

            mostrarMensagem(
                "Não foi possível salvar o vídeo selecionado."
            )
        }
    }

    private fun registrarPublicacao(arquivo: File) {

        val usuario = auth.currentUser ?: return

        val autor = usuario.displayName
            ?.takeIf { it.isNotBlank() }
            ?: usuario.email?.substringBefore("@")
            ?: "Usuário"

        val dataAtual = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val novoVideo = VideoPublicado(
            id = UUID.randomUUID().toString(),
            arquivo = arquivo.name,
            autor = autor,
            usuarioId = usuario.uid,
            dataPublicacao = dataAtual
        )

        videos = listOf(novoVideo) + videos

        salvarVideos()

        mostrarMensagem(
            "Vídeo publicado com sucesso!"
        )
    }

    private fun carregarVideos(): List<VideoPublicado> {
        val arquivo = arquivoPublicacoes()
        if (!arquivo.exists()) return emptyList()
        return MediaManager.parseVideosJson(arquivo.readText())
    }

    private fun salvarVideos() {
        try {
            arquivoPublicacoes().writeText(MediaManager.serializarVideosJson(videos))
        } catch (e: Exception) {
            mostrarMensagem("Não foi possível salvar os dados do vídeo.")
        }
    }

    private fun excluirVideo(video: VideoPublicado) {

        val usuario = auth.currentUser

        if (usuario == null) {
            mostrarMensagem(
                "Faça login para excluir vídeos."
            )
            return
        }

        if (usuario.uid != video.usuarioId) {
            mostrarMensagem(
                "Você só pode excluir seus próprios vídeos."
            )
            return
        }

        try {

            if (videoTocandoId == video.id) {
                pararVideo()
            }

            val arquivo = File(
                diretorioVideos(),
                video.arquivo
            )

            if (arquivo.exists() && !arquivo.delete()) {
                mostrarMensagem(
                    "Não foi possível excluir o arquivo de vídeo."
                )
                return
            }

            videos = videos.filterNot {
                it.id == video.id
            }

            salvarVideos()

            mostrarMensagem(
                "Vídeo excluído com sucesso!"
            )

        } catch (e: Exception) {

            mostrarMensagem(
                "Ocorreu um erro ao excluir o vídeo."
            )
        }
    }

    private fun pararVideo() {

        val atual = videoView

        videoView = null
        videoTocandoId = null

        try {
            atual?.stopPlayback()
        } catch (e: Exception) {
        }
    }

    private fun mostrarMensagem(mensagem: String) {

        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {

        pararVideo()

        super.onDestroy()
    }
}

data class VideoPublicado(
    val id: String,
    val arquivo: String,
    val autor: String,
    val usuarioId: String,
    val dataPublicacao: String
)