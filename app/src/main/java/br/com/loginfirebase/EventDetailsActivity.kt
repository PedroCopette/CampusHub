package br.com.loginfirebase

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val DetailsNavy = Color(0xFF172D59)
private val DetailsBlue = Color(0xFF2864D7)
private val DetailsBackground = Color(0xFFF3F7FD)
private val DetailsText = Color(0xFF24324A)
private val DetailsMuted = Color(0xFF657694)
private val DetailsBorder = Color(0xFFE0E9F7)

@OptIn(ExperimentalMaterial3Api::class)
class EventDetailsActivity : ComponentActivity() {

    companion object {
        fun formatarAvisoAvaliacao(media: Double, total: Int): String {
            return EventDetailsUtils.formatarMediaTexto(media, total)
        }
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var inscrito by mutableStateOf(false)
    private var favorito by mutableStateOf(false)

    private var comentarios by mutableStateOf<List<Comentario>>(emptyList())
    private var textoComentario by mutableStateOf("")
    private var comentarioEditandoId by mutableStateOf<String?>(null)

    private var notaSelecionada by mutableStateOf(0)
    private var notaUsuario by mutableStateOf(0)
    private var mediaAvaliacoes by mutableStateOf(0.0)
    private var quantidadeAvaliacoes by mutableStateOf(0)

    private var eventoEncerrado by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val idEvento = intent.getStringExtra("idEvento") ?: ""
        val nomeEvento = intent.getStringExtra("nomeEvento") ?: "Evento"
        val dataEvento = intent.getStringExtra("dataEvento")
            ?: "Data não informada"
        val horarioEvento = intent.getStringExtra("horarioEvento")
            ?: "Horário não informado"
        val localEvento = intent.getStringExtra("localEvento")
            ?: "Local não informado"
        val descricaoEvento = intent.getStringExtra("descricaoEvento")
            ?: "Descrição não informada"
        val enderecoEvento = intent.getStringExtra("enderecoEvento")
            ?: "Av. Batista Bonotto Sobrinho, 733 - Santiago/RS"

        verificarInscricao(idEvento)
        verificarFavorito(idEvento)
        carregarComentarios(idEvento)
        verificarEventoEncerrado(dataEvento, horarioEvento)
        carregarAvaliacaoUsuario(idEvento)
        carregarMediaAvaliacoes(idEvento)

        setContent {
            LoginFirebaseTheme {

                Scaffold(
                    containerColor = DetailsBackground,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Detalhes do evento",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
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
                                        fontSize = 28.sp,
                                        color = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = DetailsNavy,
                                titleContentColor = Color.White,
                                navigationIconContentColor = Color.White
                            )
                        )
                    }
                ) { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(DetailsBackground)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )

                        // NOME DO EVENTO

                        CartaoDetalhes {

                            Text(
                                text = nomeEvento,
                                color = DetailsNavy,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Informações do evento",
                                color = DetailsMuted,
                                fontSize = 14.sp
                            )
                        }

                        // INFORMAÇÕES E LOCALIZAÇÃO

                        CartaoDetalhes {

                            Text(
                                text = "Informações",
                                color = DetailsNavy,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            HorizontalDivider(
                                color = DetailsBorder
                            )

                            CampoDetalhe(
                                titulo = "Data",
                                valor = dataEvento
                            )

                            CampoDetalhe(
                                titulo = "Horário",
                                valor = horarioEvento
                            )

                            CampoDetalhe(
                                titulo = "Local",
                                valor = localEvento
                            )

                            CampoDetalhe(
                                titulo = "Endereço",
                                valor = enderecoEvento
                            )

                            Button(
                                onClick = {
                                    abrirMapa(localEvento, enderecoEvento)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DetailsBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    text = "VER LOCALIZAÇÃO NO MAPA",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // FOTOS, ÁUDIOS E VÍDEOS

                        CartaoDetalhes {

                            Text(
                                text = "Materiais do evento",
                                color = DetailsNavy,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Fotos, áudios e vídeos publicados para este evento.",
                                color = DetailsMuted,
                                fontSize = 13.sp
                            )

                            BotaoMaterial(
                                texto = "FOTOS DO EVENTO"
                            ) {

                                val intentFotos = Intent(
                                    this@EventDetailsActivity,
                                    EventPhotosActivity::class.java
                                )

                                intentFotos.putExtra(
                                    "idEvento",
                                    idEvento
                                )

                                intentFotos.putExtra(
                                    "nomeEvento",
                                    nomeEvento
                                )

                                startActivity(intentFotos)
                            }

                            BotaoMaterial(
                                texto = "ÁUDIOS DO EVENTO"
                            ) {

                                val intentAudios = Intent(
                                    this@EventDetailsActivity,
                                    EventAudioActivity::class.java
                                )

                                intentAudios.putExtra(
                                    "idEvento",
                                    idEvento
                                )

                                intentAudios.putExtra(
                                    "nomeEvento",
                                    nomeEvento
                                )

                                startActivity(intentAudios)
                            }

                            BotaoMaterial(
                                texto = "VÍDEOS DO EVENTO"
                            ) {

                                val intentVideos = Intent(
                                    this@EventDetailsActivity,
                                    EventVideoActivity::class.java
                                )

                                intentVideos.putExtra(
                                    "idEvento",
                                    idEvento
                                )

                                intentVideos.putExtra(
                                    "nomeEvento",
                                    nomeEvento
                                )

                                startActivity(intentVideos)
                            }
                        }

                        // DESCRIÇÃO

                        CartaoDetalhes {

                            Text(
                                text = "Sobre o evento",
                                color = DetailsNavy,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = descricaoEvento,
                                color = DetailsText,
                                fontSize = 15.sp,
                                lineHeight = 23.sp
                            )
                        }

                        // INSCRIÇÃO

                        Button(
                            onClick = {

                                if (inscrito) {

                                    cancelarInscricao(idEvento)

                                } else {

                                    fazerInscricao(
                                        idEvento,
                                        nomeEvento,
                                        dataEvento,
                                        horarioEvento,
                                        localEvento
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DetailsNavy,
                                contentColor = Color.White
                            )
                        ) {

                            Text(
                                text = if (inscrito) {
                                    "CANCELAR INSCRIÇÃO"
                                } else {
                                    "INSCREVER-SE"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // FAVORITOS

                        OutlinedButton(
                            onClick = {

                                if (favorito) {

                                    removerFavorito(idEvento)

                                } else {

                                    adicionarFavorito(
                                        idEvento,
                                        nomeEvento,
                                        dataEvento,
                                        horarioEvento,
                                        localEvento
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(15.dp),
                            border = BorderStroke(
                                1.dp,
                                DetailsBlue
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = DetailsBlue
                            )
                        ) {

                            Text(
                                text = if (favorito) {
                                    "REMOVER DOS FAVORITOS"
                                } else {
                                    "ADICIONAR AOS FAVORITOS"
                                },
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // AVALIAÇÕES

                        CartaoDetalhes {

                            Text(
                                text = "Avaliações",
                                color = DetailsNavy,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            if (mediaAvaliacoes > 0) {

                                Text(
                                    text = "Média %.1f · %d avaliações".format(
                                        mediaAvaliacoes,
                                        quantidadeAvaliacoes
                                    ),
                                    color = DetailsText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )

                            } else {

                                Text(
                                    text = "Ainda não existem avaliações.",
                                    color = DetailsMuted,
                                    fontSize = 14.sp
                                )
                            }

                            if (eventoEncerrado && inscrito) {

                                HorizontalDivider(
                                    color = DetailsBorder
                                )

                                Text(
                                    text = "Sua avaliação",
                                    color = DetailsText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = if (notaSelecionada == 0) {
                                        "Escolha uma nota de 1 a 5"
                                    } else {
                                        "Nota selecionada: $notaSelecionada"
                                    },
                                    color = DetailsMuted,
                                    fontSize = 14.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    for (i in 1..5) {

                                        TextButton(
                                            onClick = {
                                                notaSelecionada = i
                                            }
                                        ) {

                                            Text(
                                                text = if (i <= notaSelecionada) {
                                                    "★"
                                                } else {
                                                    "☆"
                                                },
                                                color = if (i <= notaSelecionada) {
                                                    Color(0xFFE0A020)
                                                } else {
                                                    DetailsMuted
                                                },
                                                fontSize = 31.sp
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = {
                                        salvarAvaliacao(idEvento)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = DetailsBlue,
                                        contentColor = Color.White
                                    )
                                ) {

                                    Text(
                                        text = if (notaUsuario == 0) {
                                            "AVALIAR EVENTO"
                                        } else {
                                            "ALTERAR AVALIAÇÃO"
                                        }
                                    )
                                }

                            } else if (!eventoEncerrado) {

                                Text(
                                    text = "A avaliação fica disponível após o encerramento do evento.",
                                    color = DetailsMuted,
                                    fontSize = 14.sp
                                )

                            } else {

                                Text(
                                    text = "Somente alunos inscritos podem avaliar este evento.",
                                    color = DetailsMuted,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // COMENTÁRIOS

                        CartaoDetalhes {

                            Text(
                                text = "Comentários",
                                color = DetailsNavy,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = textoComentario,
                                onValueChange = {
                                    textoComentario = it
                                },
                                modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text(
                                        if (comentarioEditandoId == null) {
                                            "Escreva um comentário"
                                        } else {
                                            "Editar comentário"
                                        }
                                    )
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = DetailsBlue,
                                    unfocusedBorderColor = DetailsBorder,
                                    focusedLabelColor = DetailsBlue,
                                    cursorColor = DetailsBlue,
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )

                            Button(
                                onClick = {

                                    if (comentarioEditandoId == null) {

                                        publicarComentario(
                                            idEvento,
                                            nomeEvento
                                        )

                                    } else {

                                        editarComentario()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DetailsBlue,
                                    contentColor = Color.White
                                )
                            ) {

                                Text(
                                    text = if (comentarioEditandoId == null) {
                                        "PUBLICAR COMENTÁRIO"
                                    } else {
                                        "SALVAR EDIÇÃO"
                                    }
                                )
                            }

                            if (comentarioEditandoId != null) {

                                TextButton(
                                    onClick = {
                                        textoComentario = ""
                                        comentarioEditandoId = null
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "CANCELAR EDIÇÃO",
                                        color = DetailsBlue
                                    )
                                }
                            }

                            HorizontalDivider(
                                color = DetailsBorder
                            )

                            if (comentarios.isEmpty()) {

                                Text(
                                    text = "Ainda não existem comentários.",
                                    color = DetailsMuted,
                                    fontSize = 14.sp
                                )

                            } else {

                                comentarios.forEach { comentario ->

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = DetailsBackground,
                                                shape = RoundedCornerShape(14.dp)
                                            )
                                            .padding(13.dp),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {

                                        Text(
                                            text = comentario.autorNome,
                                            color = DetailsNavy,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp
                                        )

                                        Text(
                                            text = comentario.dataPublicacao,
                                            color = DetailsMuted,
                                            fontSize = 12.sp
                                        )

                                        Text(
                                            text = comentario.texto,
                                            color = DetailsText,
                                            fontSize = 14.sp
                                        )

                                        if (
                                            comentario.usuarioId ==
                                            auth.currentUser?.uid
                                        ) {

                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {

                                                TextButton(
                                                    onClick = {
                                                        textoComentario =
                                                            comentario.texto

                                                        comentarioEditandoId =
                                                            comentario.id
                                                    }
                                                ) {

                                                    Text(
                                                        text = "EDITAR",
                                                        color = DetailsBlue
                                                    )
                                                }

                                                TextButton(
                                                    onClick = {
                                                        excluirComentario(
                                                            comentario.id
                                                        )
                                                    }
                                                ) {

                                                    Text(
                                                        text = "EXCLUIR",
                                                        color = Color(0xFFBD3333)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }
        }
    }

    // INSCRIÇÃO

    private fun verificarInscricao(idEvento: String) {

        val usuario = auth.currentUser ?: return
        val idInscricao = "${usuario.uid}_$idEvento"

        db.collection("inscricoes")
            .document(idInscricao)
            .get()
            .addOnSuccessListener { documento ->
                inscrito = documento.exists()
            }
    }

    private fun fazerInscricao(
        idEvento: String,
        nomeEvento: String,
        dataEvento: String,
        horarioEvento: String,
        localEvento: String
    ) {

        val usuario = auth.currentUser

        if (usuario == null) {

            Toast.makeText(
                this,
                "Faça login para se inscrever.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val idInscricao = "${usuario.uid}_$idEvento"

        val dados = hashMapOf(
            "idEvento" to idEvento,
            "nomeEvento" to nomeEvento,
            "dataEvento" to dataEvento,
            "horarioEvento" to horarioEvento,
            "localEvento" to localEvento,
            "usuarioId" to usuario.uid,
            "usuarioEmail" to usuario.email
        )

        db.collection("inscricoes")
            .document(idInscricao)
            .set(dados)
            .addOnSuccessListener {

                inscrito = true

                Toast.makeText(
                    this,
                    "Inscrição realizada!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao realizar inscrição.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun cancelarInscricao(idEvento: String) {

        val usuario = auth.currentUser ?: return
        val idInscricao = "${usuario.uid}_$idEvento"

        db.collection("inscricoes")
            .document(idInscricao)
            .delete()
            .addOnSuccessListener {

                inscrito = false

                Toast.makeText(
                    this,
                    "Inscrição cancelada!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao cancelar inscrição.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // FAVORITOS

    private fun verificarFavorito(idEvento: String) {

        val usuario = auth.currentUser ?: return
        val idFavorito = "${usuario.uid}_$idEvento"

        db.collection("favoritos")
            .document(idFavorito)
            .get()
            .addOnSuccessListener { documento ->
                favorito = documento.exists()
            }
    }

    private fun adicionarFavorito(
        idEvento: String,
        nomeEvento: String,
        dataEvento: String,
        horarioEvento: String,
        localEvento: String
    ) {

        val usuario = auth.currentUser

        if (usuario == null) {

            Toast.makeText(
                this,
                "Faça login para favoritar eventos.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val idFavorito = "${usuario.uid}_$idEvento"

        val dados = hashMapOf(
            "idEvento" to idEvento,
            "nomeEvento" to nomeEvento,
            "dataEvento" to dataEvento,
            "horarioEvento" to horarioEvento,
            "localEvento" to localEvento,
            "usuarioId" to usuario.uid,
            "usuarioEmail" to usuario.email
        )

        db.collection("favoritos")
            .document(idFavorito)
            .set(dados)
            .addOnSuccessListener {

                favorito = true

                Toast.makeText(
                    this,
                    "Evento adicionado aos favoritos!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao adicionar aos favoritos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun removerFavorito(idEvento: String) {

        val usuario = auth.currentUser ?: return
        val idFavorito = "${usuario.uid}_$idEvento"

        db.collection("favoritos")
            .document(idFavorito)
            .delete()
            .addOnSuccessListener {

                favorito = false

                Toast.makeText(
                    this,
                    "Evento removido dos favoritos!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao remover dos favoritos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // COMENTÁRIOS

    private fun publicarComentario(
        idEvento: String,
        nomeEvento: String
    ) {

        val usuario = auth.currentUser

        if (usuario == null) {

            Toast.makeText(
                this,
                "Faça login para comentar.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val texto = textoComentario.trim()

        if (texto.isEmpty()) {

            Toast.makeText(
                this,
                "Digite um comentário.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val comentarioId = db.collection("comentarios")
            .document()
            .id

        val autorNome = usuario.displayName
            ?: usuario.email
            ?: "Usuário"

        val dataAtual = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val dados = hashMapOf(
            "idEvento" to idEvento,
            "nomeEvento" to nomeEvento,
            "texto" to texto,
            "usuarioId" to usuario.uid,
            "autorNome" to autorNome,
            "dataPublicacao" to dataAtual
        )

        db.collection("comentarios")
            .document(comentarioId)
            .set(dados)
            .addOnSuccessListener {

                textoComentario = ""

                Toast.makeText(
                    this,
                    "Comentário publicado!",
                    Toast.LENGTH_SHORT
                ).show()

                carregarComentarios(idEvento)
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao publicar comentário.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun carregarComentarios(idEvento: String) {
        db.collection("comentarios")
            .whereEqualTo("idEvento", idEvento)
            .get()
            .addOnSuccessListener { resultado ->
                val docs = resultado.documents.map { Pair(it.id, it.data ?: emptyMap()) }
                comentarios = EventDetailsManager.mapearListaComentarios(docs)
            }
    }

    private fun editarComentario() {

        val idComentario = comentarioEditandoId ?: return
        val texto = textoComentario.trim()

        if (texto.isEmpty()) {

            Toast.makeText(
                this,
                "Digite um comentário.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        db.collection("comentarios")
            .document(idComentario)
            .update("texto", texto)
            .addOnSuccessListener {

                textoComentario = ""
                comentarioEditandoId = null

                Toast.makeText(
                    this,
                    "Comentário atualizado!",
                    Toast.LENGTH_SHORT
                ).show()

                val idEvento =
                    intent.getStringExtra("idEvento") ?: ""

                carregarComentarios(idEvento)
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao editar comentário.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun excluirComentario(idComentario: String) {

        db.collection("comentarios")
            .document(idComentario)
            .delete()
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Comentário excluído!",
                    Toast.LENGTH_SHORT
                ).show()

                val idEvento =
                    intent.getStringExtra("idEvento") ?: ""

                carregarComentarios(idEvento)
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao excluir comentário.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    // AVALIAÇÕES

    private fun verificarEventoEncerrado(
        dataEvento: String,
        horarioEvento: String
    ) {

        try {

            val formato = SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
            )

            val dataEventoCompleta = "$dataEvento $horarioEvento"
            val data = formato.parse(dataEventoCompleta)

            if (data != null) {
                eventoEncerrado = Date().after(data)
            }

        } catch (e: Exception) {

            eventoEncerrado = false
        }
    }

    private fun carregarAvaliacaoUsuario(idEvento: String) {

        val usuario = auth.currentUser ?: return
        val idAvaliacao = "${usuario.uid}_$idEvento"

        db.collection("avaliacoes")
            .document(idAvaliacao)
            .get()
            .addOnSuccessListener { documento ->

                if (documento.exists()) {

                    val nota = documento.getLong("nota")?.toInt() ?: 0

                    notaUsuario = nota
                    notaSelecionada = nota
                }
            }
    }

    private fun salvarAvaliacao(idEvento: String) {

        val usuario = auth.currentUser

        if (usuario == null) {

            Toast.makeText(
                this,
                "Faça login para avaliar.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (!inscrito) {

            Toast.makeText(
                this,
                "Você precisa estar inscrito no evento.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (!eventoEncerrado) {

            Toast.makeText(
                this,
                "O evento ainda não foi encerrado.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (notaSelecionada !in 1..5) {

            Toast.makeText(
                this,
                "Selecione uma nota de 1 a 5.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val idAvaliacao = "${usuario.uid}_$idEvento"

        val dados = hashMapOf(
            "idEvento" to idEvento,
            "usuarioId" to usuario.uid,
            "usuarioEmail" to usuario.email,
            "nota" to notaSelecionada
        )

        db.collection("avaliacoes")
            .document(idAvaliacao)
            .set(dados)
            .addOnSuccessListener {

                notaUsuario = notaSelecionada

                Toast.makeText(
                    this,
                    "Avaliação salva!",
                    Toast.LENGTH_SHORT
                ).show()

                carregarMediaAvaliacoes(idEvento)
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao salvar avaliação.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun carregarMediaAvaliacoes(idEvento: String) {

        db.collection("avaliacoes")
            .whereEqualTo("idEvento", idEvento)
            .get()
            .addOnSuccessListener { resultado ->

                if (resultado.isEmpty) {

                    mediaAvaliacoes = 0.0
                    quantidadeAvaliacoes = 0

                    return@addOnSuccessListener
                }

                val notas = resultado.documents.mapNotNull {
                    it.getLong("nota")?.toInt()
                }

                if (notas.isNotEmpty()) {

                    mediaAvaliacoes = notas.average()
                    quantidadeAvaliacoes = notas.size
                }
            }
    }

    // GOOGLE MAPS

    private fun abrirMapa(
        local: String,
        endereco: String
    ) {

        val origem = Uri.encode(
            "Santiago, Rio Grande do Sul, Brasil"
        )

        val destino = if (endereco.isNotBlank()) {
            "$local, $endereco"
        } else {
            "$local, Santiago, Rio Grande do Sul, Brasil"
        }

        val destinoCodificado = Uri.encode(destino)

        val uriRota = Uri.parse(
            "https://www.google.com/maps/dir/?api=1" +
                    "&origin=$origem" +
                    "&destination=$destinoCodificado" +
                    "&travelmode=driving"
        )

        try {

            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    uriRota
                )
            )

        } catch (e: Exception) {

            Toast.makeText(
                this,
                "Não foi possível abrir o Google Maps.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
internal fun CartaoDetalhes(
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            DetailsBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

@Composable
internal fun CampoDetalhe(
    titulo: String,
    valor: String
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {

        Text(
            text = titulo,
            color = DetailsMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = valor,
            color = DetailsText,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun BotaoMaterial(
    texto: String,
    onClick: () -> Unit
) {

    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            1.dp,
            DetailsBorder
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = DetailsBlue
        )
    ) {

        Text(
            text = texto,
            fontWeight = FontWeight.SemiBold
        )
    }
}

data class Comentario(
    val id: String,
    val texto: String,
    val usuarioId: String,
    val autorNome: String,
    val dataPublicacao: String
)