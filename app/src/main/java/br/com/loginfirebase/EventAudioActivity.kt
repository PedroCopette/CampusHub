package br.com.loginfirebase

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
class EventAudioActivity : ComponentActivity() {

    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
    }

    private val auth = FirebaseAuth.getInstance()

    private var idEvento = ""
    private var nomeEvento = "Evento"

    private var audios by mutableStateOf<List<AudioPublicado>>(
        emptyList()
    )

    private var gravando by mutableStateOf(false)
    private var audioTocandoId by mutableStateOf<String?>(null)

    private var gravador: MediaRecorder? = null
    private var reprodutor: MediaPlayer? = null
    private var arquivoGravacao: File? = null

    private val permissaoMicrofone =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permitido ->
            if (permitido) {
                iniciarGravacao()
            } else {
                mostrarMensagem(
                    "É necessário permitir o uso do microfone."
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idEvento = intent.getStringExtra("idEvento") ?: ""
        nomeEvento = intent.getStringExtra("nomeEvento") ?: "Evento"

        audios = carregarAudios()

        setContent {
            LoginFirebaseTheme {

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Áudios do evento",
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
                            text = "Grave e compartilhe áudios relacionados a este evento.",
                            fontSize = 14.sp
                        )

                        Button(
                            onClick = {
                                if (auth.currentUser == null) {
                                    mostrarMensagem(
                                        "Faça login para publicar áudios."
                                    )
                                } else if (gravando) {
                                    finalizarGravacao()
                                } else {
                                    if (
                                        checkSelfPermission(
                                            Manifest.permission.RECORD_AUDIO
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        iniciarGravacao()
                                    } else {
                                        permissaoMicrofone.launch(
                                            Manifest.permission.RECORD_AUDIO
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = if (gravando) {
                                    "PARAR E PUBLICAR ÁUDIO"
                                } else {
                                    "INICIAR GRAVAÇÃO"
                                }
                            )
                        }

                        if (gravando) {
                            Text(
                                text = "Gravação em andamento. Toque no botão acima para terminar e publicar.",
                                fontWeight = FontWeight.Medium
                            )
                        }

                        TextButton(
                            onClick = {
                                pararReproducao()
                            },
                            enabled = audioTocandoId != null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("PARAR REPRODUÇÃO")
                        }

                        Spacer(
                            modifier = Modifier.height(5.dp)
                        )

                        Text(
                            text = "Publicações (${audios.size})",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (audios.isEmpty()) {
                            Text(
                                text = "Ainda não existem áudios neste evento."
                            )
                        }

                        audios.forEach { audio ->

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors()
                            ) {

                                Column(
                                    modifier = Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {

                                    Text(
                                        text = "Áudio do evento",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Publicado por: ${audio.autor}",
                                        fontWeight = FontWeight.Medium
                                    )

                                    Text(
                                        text = "Data: ${audio.dataPublicacao}",
                                        fontSize = 13.sp
                                    )

                                    Button(
                                        onClick = {
                                            if (audioTocandoId == audio.id) {
                                                pararReproducao()
                                            } else {
                                                reproduzirAudio(audio)
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (
                                                audioTocandoId == audio.id
                                            ) {
                                                "PARAR ÁUDIO"
                                            } else {
                                                "OUVIR ÁUDIO"
                                            }
                                        )
                                    }

                                    if (
                                        audio.usuarioId ==
                                        auth.currentUser?.uid
                                    ) {

                                        OutlinedButton(
                                            onClick = {
                                                excluirAudio(audio)
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("EXCLUIR MEU ÁUDIO")
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                    }
                }
            }
        }
    }

    private fun diretorioAudios(): File {

        val pasta = File(filesDir, "audios_eventos")

        if (!pasta.exists() && !pasta.mkdirs()) {
            throw IllegalStateException(
                "Não foi possível criar a pasta de áudios."
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
            "publicacoes_audios_$idSeguro.json"
        )
    }

    private fun iniciarGravacao() {

        if (auth.currentUser == null) {
            mostrarMensagem("Faça login para gravar um áudio.")
            return
        }

        var novoGravador: MediaRecorder? = null
        var arquivo: File? = null

        try {
            arquivo = File(
                diretorioAudios(),
                "audio_${UUID.randomUUID()}.m4a"
            )

            novoGravador = MediaRecorder(this)

            novoGravador.setAudioSource(
                MediaRecorder.AudioSource.MIC
            )

            novoGravador.setOutputFormat(
                MediaRecorder.OutputFormat.MPEG_4
            )

            novoGravador.setAudioEncoder(
                MediaRecorder.AudioEncoder.AAC
            )

            novoGravador.setOutputFile(
                arquivo.absolutePath
            )

            novoGravador.prepare()
            novoGravador.start()

            gravador = novoGravador
            arquivoGravacao = arquivo
            gravando = true

            mostrarMensagem(
                "Gravação iniciada. Fale perto do microfone."
            )

        } catch (e: Exception) {

            try {
                novoGravador?.release()
            } catch (_: Exception) {
            }

            arquivo?.delete()

            gravador = null
            arquivoGravacao = null
            gravando = false

            mostrarMensagem(
                "Não foi possível iniciar a gravação."
            )
        }
    }

    private fun finalizarGravacao() {

        val gravadorAtual = gravador ?: return
        val arquivoAtual = arquivoGravacao

        var gravacaoValida = false

        try {
            gravadorAtual.stop()
            gravacaoValida = true

        } catch (e: RuntimeException) {

            arquivoAtual?.delete()

            mostrarMensagem(
                "O áudio ficou muito curto. Grave por mais tempo e tente novamente."
            )

        } finally {

            try {
                gravadorAtual.release()
            } catch (_: Exception) {
            }

            gravador = null
            arquivoGravacao = null
            gravando = false
        }

        if (
            !gravacaoValida ||
            arquivoAtual == null ||
            !arquivoAtual.exists() ||
            arquivoAtual.length() == 0L
        ) {
            return
        }

        if (auth.currentUser == null) {
            arquivoAtual.delete()
            mostrarMensagem("Faça login para publicar áudios.")
            return
        }

        val usuario = auth.currentUser ?: return

        val nomeAutor = usuario.displayName
            ?.takeIf { it.isNotBlank() }
            ?: usuario.email?.substringBefore("@")
            ?: "Usuário"

        val dataAtual = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val novoAudio = AudioPublicado(
            id = UUID.randomUUID().toString(),
            arquivo = arquivoAtual.name,
            autor = nomeAutor,
            usuarioId = usuario.uid,
            dataPublicacao = dataAtual
        )

        audios = listOf(novoAudio) + audios
        salvarAudios()

        mostrarMensagem("Áudio publicado com sucesso!")
    }

    private fun carregarAudios(): List<AudioPublicado> {
        val arquivo = arquivoPublicacoes()
        if (!arquivo.exists()) return emptyList()
        return MediaManager.parseAudiosJson(arquivo.readText())
    }

    private fun salvarAudios() {
        try {
            arquivoPublicacoes().writeText(MediaManager.serializarAudiosJson(audios))
        } catch (e: Exception) {
            mostrarMensagem("Não foi possível salvar os dados do áudio.")
        }
    }

    private fun reproduzirAudio(audio: AudioPublicado) {

        pararReproducao()

        try {

            val arquivo = File(
                diretorioAudios(),
                audio.arquivo
            )

            if (!arquivo.exists()) {
                mostrarMensagem("O arquivo de áudio não foi encontrado.")
                return
            }

            val novoReprodutor = MediaPlayer()

            novoReprodutor.setDataSource(
                arquivo.absolutePath
            )

            novoReprodutor.setOnCompletionListener { tocador ->

                if (reprodutor === tocador) {
                    reprodutor = null
                    audioTocandoId = null
                }

                tocador.release()
            }

            novoReprodutor.setOnErrorListener { tocador, _, _ ->

                if (reprodutor === tocador) {
                    reprodutor = null
                    audioTocandoId = null
                }

                tocador.release()

                mostrarMensagem(
                    "Não foi possível reproduzir o áudio."
                )

                true
            }

            novoReprodutor.prepare()

            reprodutor = novoReprodutor
            audioTocandoId = audio.id

            novoReprodutor.start()

        } catch (e: Exception) {

            pararReproducao()

            mostrarMensagem(
                "Erro ao abrir o áudio."
            )
        }
    }

    private fun pararReproducao() {

        val atual = reprodutor

        reprodutor = null
        audioTocandoId = null

        try {
            atual?.stop()
        } catch (_: Exception) {
        }

        try {
            atual?.release()
        } catch (_: Exception) {
        }
    }

    private fun excluirAudio(audio: AudioPublicado) {

        val usuario = auth.currentUser

        if (
            usuario == null ||
            audio.usuarioId != usuario.uid
        ) {
            mostrarMensagem(
                "Você só pode excluir os próprios áudios."
            )
            return
        }

        if (audioTocandoId == audio.id) {
            pararReproducao()
        }

        try {

            val arquivo = File(
                diretorioAudios(),
                audio.arquivo
            )

            if (arquivo.exists()) {
                arquivo.delete()
            }

            audios = audios.filterNot {
                it.id == audio.id
            }

            salvarAudios()

            mostrarMensagem("Áudio excluído.")

        } catch (e: Exception) {

            mostrarMensagem(
                "Não foi possível excluir o áudio."
            )
        }
    }

    private fun mostrarMensagem(mensagem: String) {

        Toast.makeText(
            this,
            mensagem,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun cancelarGravacao() {

        val atual = gravador

        gravador = null
        gravando = false

        try {
            atual?.stop()
        } catch (_: Exception) {
        }

        try {
            atual?.release()
        } catch (_: Exception) {
        }

        arquivoGravacao?.delete()
        arquivoGravacao = null
    }

    override fun onDestroy() {

        if (gravando) {
            cancelarGravacao()
        }

        pararReproducao()

        super.onDestroy()
    }
}

data class AudioPublicado(
    val id: String,
    val arquivo: String,
    val autor: String,
    val usuarioId: String,
    val dataPublicacao: String
)