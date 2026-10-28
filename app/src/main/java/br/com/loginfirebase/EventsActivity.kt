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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

                val eventosFiltrados = eventosCarregados.filter { evento ->

                    val correspondeBusca = evento.nome.contains(
                        busca,
                        ignoreCase = true
                    )

                    val correspondeCategoria =
                        categoriaSelecionada == "Todas" ||
                                evento.categoria == categoriaSelecionada

                    val correspondeSituacao =
                        when (situacaoSelecionada) {
                            "Próximos" -> !eventoEncerrado(
                                evento.data,
                                evento.horario
                            )

                            "Encerrados" -> eventoEncerrado(
                                evento.data,
                                evento.horario
                            )

                            else -> true
                        }

                    correspondeBusca &&
                            correspondeCategoria &&
                            correspondeSituacao
                }

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
                                        encerrado = eventoEncerrado(
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

    private fun enderecoPadrao(id: String): String {
        return when (id) {

            "semana_academica" ->
                "Av. Batista Bonotto Sobrinho, 733, Santiago - RS, Brasil"

            "hackathon_campushub" ->
                "Rua Tio Virgílio, 423, Santiago - RS, Brasil"

            "palestra_tecnologia" ->
                "Rua Bento Gonçalves, 1642, Santiago - RS, Brasil"

            else ->
                "Santiago, Rio Grande do Sul, Brasil"
        }
    }

    private fun obterEventosIniciais(): List<EventoBusca> {

        return listOf(

            EventoBusca(
                id = "semana_academica",
                nome = "Semana Acadêmica",
                data = "20/09/2026",
                horario = "19:00",
                local = "Auditório da URI",
                descricao = "Evento acadêmico com palestras, apresentações e atividades para os estudantes.",
                categoria = "Acadêmico",
                endereco = enderecoPadrao("semana_academica")
            ),

            EventoBusca(
                id = "hackathon_campushub",
                nome = "Hackathon CampusHub",
                data = "25/09/2026",
                horario = "08:00",
                local = "Laboratório de Informática",
                descricao = "Competição de programação e desenvolvimento de soluções tecnológicas.",
                categoria = "Tecnologia",
                endereco = enderecoPadrao("hackathon_campushub")
            ),

            EventoBusca(
                id = "palestra_tecnologia",
                nome = "Palestra de Tecnologia",
                data = "30/09/2026",
                horario = "20:00",
                local = "Sala 12",
                descricao = "Palestra sobre tecnologia, inovação e tendências da área de computação.",
                categoria = "Tecnologia",
                endereco = enderecoPadrao("palestra_tecnologia")
            )
        )
    }

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

                val lista = resultado.documents.mapNotNull { documento ->

                    val nome = documento.getString("nomeEvento")
                    val data = documento.getString("dataEvento")
                    val horario = documento.getString("horarioEvento")
                    val local = documento.getString("localEvento")
                    val descricao = documento.getString("descricaoEvento")
                    val categoria = documento.getString("categoria")

                    val endereco = documento.getString("enderecoEvento")
                        ?: enderecoPadrao(documento.id)

                    if (
                        nome != null &&
                        data != null &&
                        horario != null &&
                        local != null &&
                        descricao != null &&
                        categoria != null
                    ) {

                        EventoBusca(
                            id = documento.id,
                            nome = nome,
                            data = data,
                            horario = horario,
                            local = local,
                            descricao = descricao,
                            categoria = categoria,
                            endereco = endereco
                        )

                    } else {
                        null
                    }

                }.sortedBy { evento ->
                    ordenarData(evento.data)
                }

                eventosCarregados = lista
                carregandoEventos = false
                erroAoCarregar = false
            }
            .addOnFailureListener {

                mostrarErroAoCarregar()
            }
    }

    private fun ordenarData(data: String): Long {

        return try {

            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).parse(data)?.time ?: Long.MAX_VALUE

        } catch (e: Exception) {

            Long.MAX_VALUE
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

    private fun eventoEncerrado(
        data: String,
        horario: String
    ): Boolean {

        val formato = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        )

        val dataEvento = formato.parse(
            "$data $horario"
        ) ?: return false

        return Date().after(dataEvento)
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
private fun CartaoFiltro(
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
private fun CartaoEvento(
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
                        tipo = "seta",
                        cor = EventsBlue,
                        tamanho = 22.dp
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(27.dp)
                        .background(EventsBorder)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                if (encerrado) {
                                    Color(0xFFE8EDF5)
                                } else {
                                    Color(0xFFE7F6EE)
                                }
                            )
                            .padding(
                                horizontal = 12.dp,
                                vertical = 7.dp
                            )
                    ) {

                        Text(
                            text = if (encerrado) {
                                "ENCERRADO"
                            } else {
                                "PRÓXIMO"
                            },
                            color = if (encerrado) {
                                EventsMuted
                            } else {
                                Color(0xFF237A4B)
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconeEventos(
    tipo: String,
    cor: Color,
    tamanho: Dp
) {

    Canvas(
        modifier = Modifier.size(tamanho)
    ) {

        val s = size.width

        val traco = Stroke(
            width = 2.3.dp.toPx()
        )

        when (tipo) {

            "academico" -> {

                val cap = Path().apply {
                    moveTo(s * 0.08f, s * 0.36f)
                    lineTo(s * 0.50f, s * 0.14f)
                    lineTo(s * 0.92f, s * 0.36f)
                    lineTo(s * 0.50f, s * 0.58f)
                    close()
                }

                drawPath(
                    path = cap,
                    color = cor,
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.24f, s * 0.46f),
                    end = Offset(s * 0.24f, s * 0.64f),
                    strokeWidth = traco.width
                )

                drawArc(
                    color = cor,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(s * 0.24f, s * 0.49f),
                    size = Size(s * 0.52f, s * 0.38f),
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.91f, s * 0.37f),
                    end = Offset(s * 0.91f, s * 0.70f),
                    strokeWidth = traco.width
                )
            }

            "tecnologia" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(s * 0.20f, s * 0.12f),
                    size = Size(s * 0.60f, s * 0.58f),
                    cornerRadius = CornerRadius(s * 0.05f),
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.12f, s * 0.82f),
                    end = Offset(s * 0.88f, s * 0.82f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.20f, s * 0.70f),
                    end = Offset(s * 0.12f, s * 0.82f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.80f, s * 0.70f),
                    end = Offset(s * 0.88f, s * 0.82f),
                    strokeWidth = traco.width
                )
            }

            "calendario" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(s * 0.16f, s * 0.20f),
                    size = Size(s * 0.68f, s * 0.66f),
                    cornerRadius = CornerRadius(s * 0.08f),
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.16f, s * 0.41f),
                    end = Offset(s * 0.84f, s * 0.41f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.34f, s * 0.10f),
                    end = Offset(s * 0.34f, s * 0.29f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.66f, s * 0.10f),
                    end = Offset(s * 0.66f, s * 0.29f),
                    strokeWidth = traco.width
                )
            }

            "localizacao" -> {

                val marcador = Path().apply {

                    moveTo(s * 0.50f, s * 0.91f)

                    cubicTo(
                        s * 0.43f, s * 0.78f,
                        s * 0.18f, s * 0.54f,
                        s * 0.18f, s * 0.39f
                    )

                    cubicTo(
                        s * 0.18f, s * 0.19f,
                        s * 0.34f, s * 0.08f,
                        s * 0.50f, s * 0.08f
                    )

                    cubicTo(
                        s * 0.66f, s * 0.08f,
                        s * 0.82f, s * 0.19f,
                        s * 0.82f, s * 0.39f
                    )

                    cubicTo(
                        s * 0.82f, s * 0.54f,
                        s * 0.57f, s * 0.78f,
                        s * 0.50f, s * 0.91f
                    )

                    close()
                }

                drawPath(
                    path = marcador,
                    color = cor,
                    style = traco
                )

                drawCircle(
                    color = cor,
                    radius = s * 0.10f,
                    center = Offset(s * 0.50f, s * 0.38f),
                    style = traco
                )
            }

            "busca" -> {

                drawCircle(
                    color = cor,
                    radius = s * 0.29f,
                    center = Offset(s * 0.40f, s * 0.40f),
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.61f, s * 0.61f),
                    end = Offset(s * 0.88f, s * 0.88f),
                    strokeWidth = traco.width
                )
            }

            "chevron" -> {

                drawLine(
                    color = cor,
                    start = Offset(s * 0.24f, s * 0.36f),
                    end = Offset(s * 0.50f, s * 0.63f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.50f, s * 0.63f),
                    end = Offset(s * 0.76f, s * 0.36f),
                    strokeWidth = traco.width
                )
            }

            "seta" -> {

                drawLine(
                    color = cor,
                    start = Offset(s * 0.12f, s * 0.50f),
                    end = Offset(s * 0.84f, s * 0.50f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.57f, s * 0.22f),
                    end = Offset(s * 0.84f, s * 0.50f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.57f, s * 0.78f),
                    end = Offset(s * 0.84f, s * 0.50f),
                    strokeWidth = traco.width
                )
            }
        }
    }
}