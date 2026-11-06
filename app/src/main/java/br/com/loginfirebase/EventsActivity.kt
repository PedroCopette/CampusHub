package br.com.loginfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val EventsNavy = Color(0xFF172D59)
private val EventsBlue = Color(0xFF2864D7)
private val EventsBackground = Color(0xFFF3F7FD)
private val EventsText = Color(0xFF142748)
private val EventsMuted = Color(0xFF657694)
private val EventsIconBackground = Color(0xFFEAF1FE)
private val EventsBorder = Color(0xFFE0E9F7)

data class EventoBusca(
    val id: String,
    val nome: String,
    val data: String,
    val horario: String,
    val local: String,
    val descricao: String,
    val categoria: String,
    val endereco: String
)

class EventsActivity : ComponentActivity() {

    companion object {
        fun obterEnderecoPadrao(id: String): String {
            return when (id) {
                "semana_academica" -> "Av. Batista Bonotto Sobrinho, 733, Santiago - RS, Brasil"
                "hackathon_campushub" -> "Rua Tio Virgílio, 423, Santiago - RS, Brasil"
                "palestra_tecnologia" -> "Rua Bento Gonçalves, 1642, Santiago - RS, Brasil"
                else -> "Santiago, Rio Grande do Sul, Brasil"
            }
        }
        fun obterFiltrosCategorias(): List<String> = listOf("Todas", "Acadêmico", "Tecnologia")
        fun obterFiltrosSituacoes(): List<String> = listOf("Todos", "Próximos", "Encerrados")
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var eventosCarregados by mutableStateOf<List<EventoBusca>>(
        emptyList()
    )

    private var carregandoEventos by mutableStateOf(true)
    private var erroAoCarregar by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {

                var busca by remember {
                    mutableStateOf("")
                }

                var categoriaSelecionada by remember {
                    mutableStateOf("Todas")
                }

                var situacaoSelecionada by remember {
                    mutableStateOf("Todos")
                }

                var menuCategoriaAberto by remember {
                    mutableStateOf(false)
                }

                var menuSituacaoAberto by remember {
                    mutableStateOf(false)
                }

                val eventosFiltrados = EventosUtils.filtrarEventos(
                    eventos = eventosCarregados,
                    busca = busca,
                    categoriaSelecionada = categoriaSelecionada,
                    situacaoSelecionada = situacaoSelecionada
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(EventsBackground)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        Text(
                            text = "Eventos",
                            color = EventsNavy,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )

                        OutlinedTextField(
                            value = busca,
                            onValueChange = {
                                busca = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Buscar evento",
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                IconeEventos(
                                    tipo = "busca",
                                    cor = EventsBlue,
                                    tamanho = 23.dp
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(17.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EventsBlue,
                                unfocusedBorderColor = Color(0xFFD3E1FA),
                                focusedLabelColor = EventsBlue,
                                cursorColor = EventsBlue,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )

                        Text(
                            text = "Filtros",
                            color = EventsNavy,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Box(
                                modifier = Modifier.weight(1f)
                            ) {

                                CartaoFiltro(
                                    texto = "Categoria: $categoriaSelecionada",
                                    onClick = {
                                        menuCategoriaAberto = true
                                    }
                                )

                                DropdownMenu(
                                    expanded = menuCategoriaAberto,
                                    onDismissRequest = {
                                        menuCategoriaAberto = false
                                    }
                                ) {

                                    DropdownMenuItem(
                                        text = {
                                            Text("Todas")
                                        },
                                        onClick = {
                                            categoriaSelecionada = "Todas"
                                            menuCategoriaAberto = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text("Acadêmico")
                                        },
                                        onClick = {
                                            categoriaSelecionada = "Acadêmico"
                                            menuCategoriaAberto = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text("Tecnologia")
                                        },
                                        onClick = {
                                            categoriaSelecionada = "Tecnologia"
                                            menuCategoriaAberto = false
                                        }
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier.weight(1f)
                            ) {

                                CartaoFiltro(
                                    texto = "Situação: $situacaoSelecionada",
                                    onClick = {
                                        menuSituacaoAberto = true
                                    }
                                )

                                DropdownMenu(
                                    expanded = menuSituacaoAberto,
                                    onDismissRequest = {
                                        menuSituacaoAberto = false
                                    }
                                ) {

                                    DropdownMenuItem(
                                        text = {
                                            Text("Todos")
                                        },
                                        onClick = {
                                            situacaoSelecionada = "Todos"
                                            menuSituacaoAberto = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text("Próximos")
                                        },
                                        onClick = {
                                            situacaoSelecionada = "Próximos"
                                            menuSituacaoAberto = false
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = {
                                            Text("Encerrados")
                                        },
                                        onClick = {
                                            situacaoSelecionada = "Encerrados"
                                            menuSituacaoAberto = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(24.dp)
                        )

                        Text(
                            text = "${eventosFiltrados.size} evento(s) encontrado(s)",
                            color = EventsMuted,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(14.dp)
                        )
                        when {

                            carregandoEventos -> {

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        EventsBorder
                                    )
                                ) {

                                    Text(
                                        text = "Carregando eventos...",
                                        color = EventsMuted,
                                        modifier = Modifier.padding(24.dp)
                                    )
                                }
                            }

                            erroAoCarregar -> {

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        EventsBorder
                                    )
                                ) {

                                    Text(
                                        text = "Não foi possível carregar os eventos. Verifique a conexão e as regras do Firestore.",
                                        color = EventsText,
                                        modifier = Modifier.padding(24.dp)
                                    )
                                }
                            }

                            eventosFiltrados.isEmpty() -> {

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        EventsBorder
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(25.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        Text(
                                            text = "Nenhum evento encontrado",
                                            color = EventsText,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )

                                        Spacer(
                                            modifier = Modifier.height(5.dp)
                                        )

                                        Text(
                                            text = "Tente mudar a busca ou os filtros.",
                                            color = EventsMuted,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            else -> {

                                eventosFiltrados.forEach { evento ->

                                    CartaoEvento(
                                        evento = evento,
                                        encerrado = EventosUtils.eventoEncerrado(
                                            evento.data,
                                            evento.horario
                                        ),
                                        onClick = {
                                            abrirDetalhes(
                                                id = evento.id,
                                                nome = evento.nome,
                                                data = evento.data,
                                                horario = evento.horario,
                                                local = evento.local,
                                                descricao = evento.descricao,
                                                endereco = evento.endereco
                                            )
                                        }
                                    )

                                    Spacer(
                                        modifier = Modifier.height(14.dp)
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        Button(
                            onClick = {
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(17.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EventsNavy,
                                contentColor = Color.White
                            )
                        ) {

                            Text(
                                text = "VOLTAR",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(17.dp)
                        )

                        Text(
                            text = "CAMPUSHUB  •  URI SANTIAGO",
                            color = Color(0xFF8798B6),
                            fontSize = 10.sp,
                            letterSpacing = 1.2.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        garantirEventosIniciais()
    }

    private fun enderecoPadrao(id: String): String = EventsManager.obterEnderecoPadrao(id)

    private fun obterEventosIniciais(): List<EventoBusca> = EventsManager.obterEventosIniciais()

    private fun garantirEventosIniciais() {

        if (auth.currentUser == null) {

            carregandoEventos = false
            erroAoCarregar = true

            Toast.makeText(
                this,
                "Faça login para consultar os eventos.",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        carregandoEventos = true
        erroAoCarregar = false

        verificarOuCriarEvento(
            eventos = obterEventosIniciais(),
            indice = 0
        )
    }

    private fun verificarOuCriarEvento(
        eventos: List<EventoBusca>,
        indice: Int
    ) {

        if (indice >= eventos.size) {
            carregarEventosDoBanco()
            return
        }

        val evento = eventos[indice]

        val referencia = db.collection("eventos")
            .document(evento.id)

        referencia.get()
            .addOnSuccessListener { documento ->

                if (documento.exists()) {

                    verificarOuCriarEvento(
                        eventos,
                        indice + 1
                    )

                } else {

                    val dados = hashMapOf(
                        "nomeEvento" to evento.nome,
                        "dataEvento" to evento.data,
                        "horarioEvento" to evento.horario,
                        "localEvento" to evento.local,
                        "descricaoEvento" to evento.descricao,
                        "categoria" to evento.categoria
                    )

                    referencia.set(dados)
                        .addOnSuccessListener {

                            verificarOuCriarEvento(
                                eventos,
                                indice + 1
                            )
                        }
                        .addOnFailureListener {

                            mostrarErroAoCarregar()
                        }
                }
            }
            .addOnFailureListener {

                mostrarErroAoCarregar()
            }
    }

    private fun carregarEventosDoBanco() {
        db.collection("eventos")
            .get()
            .addOnSuccessListener { resultado ->
                val docs = resultado.documents.map { Pair(it.id, it.data ?: emptyMap()) }
                eventosCarregados = EventsManager.MapearListaDocumentos(docs)
                carregandoEventos = false
                erroAoCarregar = false
            }
            .addOnFailureListener {
                mostrarErroAoCarregar()
            }
    }

    private fun mostrarErroAoCarregar() {

        carregandoEventos = false
        erroAoCarregar = true

        Toast.makeText(
            this,
            "Não foi possível carregar os eventos do Firebase.",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun abrirDetalhes(
        id: String,
        nome: String,
        data: String,
        horario: String,
        local: String,
        descricao: String,
        endereco: String
    ) {

        val intent = Intent(
            this,
            EventDetailsActivity::class.java
        )

        intent.putExtra("idEvento", id)
        intent.putExtra("nomeEvento", nome)
        intent.putExtra("dataEvento", data)
        intent.putExtra("horarioEvento", horario)
        intent.putExtra("localEvento", local)
        intent.putExtra("descricaoEvento", descricao)
        intent.putExtra("enderecoEvento", endereco)

        startActivity(intent)
    }
}

@Composable
internal fun CartaoFiltro(
    texto: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            Color(0xFFD3E1FA)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = texto,
                modifier = Modifier.weight(1f),
                color = EventsNavy,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )

            IconeEventos(
                tipo = "chevron",
                cor = EventsBlue,
                tamanho = 19.dp
            )
        }
    }
}

@Composable
internal fun CartaoEvento(
    evento: EventoBusca,
    encerrado: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(23.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            1.dp,
            EventsBorder
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(EventsIconBackground),
                    contentAlignment = Alignment.Center
                ) {

                    IconeEventos(
                        tipo = if (evento.categoria == "Acadêmico") {
                            "academico"
                        } else {
                            "tecnologia"
                        },
                        cor = EventsNavy,
                        tamanho = 35.dp
                    )
                }

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = evento.nome,
                        color = EventsNavy,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFE8F0FF))
                            .padding(
                                horizontal = 13.dp,
                                vertical = 5.dp
                            )
                    ) {

                        Text(
                            text = evento.categoria,
                            color = EventsBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(17.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconeEventos(
                    tipo = "calendario",
                    cor = EventsBlue,
                    tamanho = 24.dp
                )

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Text(
                    text = "${evento.data}  ·  ${evento.horario}",
                    color = EventsText,
                    fontSize = 14.sp
                )
            }

            Spacer(
                modifier = Modifier.height(13.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconeEventos(
                    tipo = "localizacao",
                    cor = EventsBlue,
                    tamanho = 24.dp
                )

                Spacer(
                    modifier = Modifier.width(15.dp)
                )

                Text(
                    text = evento.local,
                    color = EventsMuted,
                    fontSize = 14.sp
                )
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(EventsBorder)
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "VER DETALHES",
                        color = EventsBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.width(10.dp)
                    )

                    IconeEventos(
                        tipo = "chevron",
                        cor = EventsBlue,
                        tamanho = 18.dp
                    )
                }

                if (encerrado) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(Color(0xFFFFE8E8))
                            .padding(
                                horizontal = 11.dp,
                                vertical = 6.dp
                            )
                    ) {

                        Text(
                            text = "ENCERRADO",
                            color = Color(0xFFB42318),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun IconeEventos(
    tipo: String,
    cor: Color,
    tamanho: Dp
) {

    Canvas(
        modifier = Modifier.size(tamanho)
    ) {

        val largura = size.width
        val altura = size.height

        when (tipo) {

            "busca" -> {

                drawCircle(
                    color = cor,
                    radius = largura * 0.32f,
                    center = Offset(
                        largura * 0.42f,
                        altura * 0.42f
                    ),
                    style = Stroke(width = largura * 0.10f)
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.65f,
                        altura * 0.65f
                    ),
                    end = Offset(
                        largura * 0.90f,
                        altura * 0.90f
                    ),
                    strokeWidth = largura * 0.12f
                )
            }

            "chevron" -> {

                val path = Path().apply {
                    moveTo(largura * 0.35f, altura * 0.20f)
                    lineTo(largura * 0.65f, altura * 0.50f)
                    lineTo(largura * 0.35f, altura * 0.80f)
                }

                drawPath(
                    path = path,
                    color = cor,
                    style = Stroke(
                        width = largura * 0.12f
                    )
                )
            }

            "calendario" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(
                        largura * 0.12f,
                        altura * 0.20f
                    ),
                    size = Size(
                        largura * 0.76f,
                        altura * 0.68f
                    ),
                    cornerRadius = CornerRadius(
                        largura * 0.10f
                    ),
                    style = Stroke(
                        width = largura * 0.09f
                    )
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.12f,
                        altura * 0.40f
                    ),
                    end = Offset(
                        largura * 0.88f,
                        altura * 0.40f
                    ),
                    strokeWidth = largura * 0.09f
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.32f,
                        altura * 0.08f
                    ),
                    end = Offset(
                        largura * 0.32f,
                        altura * 0.30f
                    ),
                    strokeWidth = largura * 0.09f
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.68f,
                        altura * 0.08f
                    ),
                    end = Offset(
                        largura * 0.68f,
                        altura * 0.30f
                    ),
                    strokeWidth = largura * 0.09f
                )
            }

            "localizacao" -> {

                val path = Path().apply {
                    moveTo(largura * 0.50f, altura * 0.92f)
                    cubicTo(
                        largura * 0.40f,
                        altura * 0.75f,
                        largura * 0.15f,
                        altura * 0.50f,
                        largura * 0.15f,
                        altura * 0.35f
                    )
                    cubicTo(
                        largura * 0.15f,
                        altura * 0.10f,
                        largura * 0.85f,
                        altura * 0.10f,
                        largura * 0.85f,
                        altura * 0.35f
                    )
                    cubicTo(
                        largura * 0.85f,
                        altura * 0.50f,
                        largura * 0.60f,
                        altura * 0.75f,
                        largura * 0.50f,
                        altura * 0.92f
                    )
                }

                drawPath(
                    path = path,
                    color = cor,
                    style = Stroke(
                        width = largura * 0.09f
                    )
                )

                drawCircle(
                    color = cor,
                    radius = largura * 0.12f,
                    center = Offset(
                        largura * 0.50f,
                        altura * 0.35f
                    ),
                    style = Stroke(
                        width = largura * 0.09f
                    )
                )
            }

            "academico" -> {

                val path = Path().apply {
                    moveTo(largura * 0.08f, altura * 0.35f)
                    lineTo(largura * 0.50f, altura * 0.12f)
                    lineTo(largura * 0.92f, altura * 0.35f)
                    lineTo(largura * 0.50f, altura * 0.58f)
                    close()
                }

                drawPath(
                    path = path,
                    color = cor
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.22f,
                        altura * 0.45f
                    ),
                    end = Offset(
                        largura * 0.22f,
                        altura * 0.78f
                    ),
                    strokeWidth = largura * 0.08f
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.78f,
                        altura * 0.45f
                    ),
                    end = Offset(
                        largura * 0.78f,
                        altura * 0.78f
                    ),
                    strokeWidth = largura * 0.08f
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.22f,
                        altura * 0.78f
                    ),
                    end = Offset(
                        largura * 0.78f,
                        altura * 0.78f
                    ),
                    strokeWidth = largura * 0.08f
                )
            }

            "tecnologia" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(
                        largura * 0.15f,
                        altura * 0.15f
                    ),
                    size = Size(
                        largura * 0.70f,
                        altura * 0.70f
                    ),
                    cornerRadius = CornerRadius(
                        largura * 0.08f
                    ),
                    style = Stroke(
                        width = largura * 0.09f
                    )
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.35f,
                        altura * 0.35f
                    ),
                    end = Offset(
                        largura * 0.65f,
                        altura * 0.65f
                    ),
                    strokeWidth = largura * 0.10f
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.65f,
                        altura * 0.35f
                    ),
                    end = Offset(
                        largura * 0.35f,
                        altura * 0.65f
                    ),
                    strokeWidth = largura * 0.10f
                )
            }
        }
    }
}