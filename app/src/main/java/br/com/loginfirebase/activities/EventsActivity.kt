package br.com.loginfirebase.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.CartaoEvento
import br.com.loginfirebase.CartaoFiltro
import br.com.loginfirebase.EventoBusca
import br.com.loginfirebase.EventosUtils
import br.com.loginfirebase.EventsManager
import br.com.loginfirebase.IconeEventos
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val EventsNavy = Color(0xFF172D59)
private val EventsBlue = Color(0xFF2864D7)
private val EventsBackground = Color(0xFFF3F7FD)
private val EventsMuted = Color(0xFF657694)
private val EventsBorder = Color(0xFFE0E9F7)

class EventsActivity : ComponentActivity() {

    companion object {
        fun obterEnderecoPadrao(id: String): String {
            return EventsManager.obterEnderecoPadrao(id)
        }
        fun obterFiltrosCategorias(): List<String> = listOf("Todas", "Acadêmico", "Tecnologia")
        fun obterFiltrosSituacoes(): List<String> = listOf("Todos", "Próximos", "Encerrados")
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var eventosCarregados by mutableStateOf<List<EventoBusca>>(emptyList())
    private var carregandoEventos by mutableStateOf(true)
    private var erroAoCarregar by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {

                var busca by remember { mutableStateOf("") }
                var categoriaSelecionada by remember { mutableStateOf("Todas") }
                var situacaoSelecionada by remember { mutableStateOf("Todos") }
                var menuCategoriaAberto by remember { mutableStateOf(false) }
                var menuSituacaoAberto by remember { mutableStateOf(false) }

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

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Eventos",
                            color = EventsNavy,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = busca,
                            onValueChange = { busca = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text(text = "Buscar evento", fontSize = 14.sp) },
                            leadingIcon = { IconeEventos(tipo = "busca", cor = EventsBlue, tamanho = 23.dp) },
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

                        Spacer(modifier = Modifier.height(22.dp))

                        Text(
                            text = "Filtros",
                            color = EventsNavy,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Box(modifier = Modifier.weight(1f)) {

                                CartaoFiltro(
                                    texto = "Categoria: $categoriaSelecionada",
                                    onClick = { menuCategoriaAberto = true }
                                )

                                DropdownMenu(
                                    expanded = menuCategoriaAberto,
                                    onDismissRequest = { menuCategoriaAberto = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Todas") },
                                        onClick = {
                                            categoriaSelecionada = "Todas"
                                            menuCategoriaAberto = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Acadêmico") },
                                        onClick = {
                                            categoriaSelecionada = "Acadêmico"
                                            menuCategoriaAberto = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Tecnologia") },
                                        onClick = {
                                            categoriaSelecionada = "Tecnologia"
                                            menuCategoriaAberto = false
                                        }
                                    )
                                }
                            }

                            Box(modifier = Modifier.weight(1f)) {

                                CartaoFiltro(
                                    texto = "Situação: $situacaoSelecionada",
                                    onClick = { menuSituacaoAberto = true }
                                )

                                DropdownMenu(
                                    expanded = menuSituacaoAberto,
                                    onDismissRequest = { menuSituacaoAberto = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Todos") },
                                        onClick = {
                                            situacaoSelecionada = "Todos"
                                            menuSituacaoAberto = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Próximos") },
                                        onClick = {
                                            situacaoSelecionada = "Próximos"
                                            menuSituacaoAberto = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Encerrados") },
                                        onClick = {
                                            situacaoSelecionada = "Encerrados"
                                            menuSituacaoAberto = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "${eventosFiltrados.size} evento(s) encontrado(s)",
                            color = EventsMuted,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        eventosFiltrados.forEach { evento ->
                            CartaoEvento(
                                evento = evento,
                                encerrado = EventosUtils.eventoEncerrado(evento.data, evento.horario),
                                onClick = {
                                    abrirDetalhes(evento)
                                }
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { finish() },
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

                        Spacer(modifier = Modifier.height(17.dp))

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

    private fun garantirEventosIniciais() {
        if (auth.currentUser == null) {
            carregandoEventos = false
            erroAoCarregar = true
            Toast.makeText(this, "Faça login para consultar os eventos.", Toast.LENGTH_SHORT).show()
            return
        }

        carregandoEventos = true
        erroAoCarregar = false
        carregarEventosDoBanco()
    }

    private fun carregarEventosDoBanco() {
        db.collection("eventos")
            .get()
            .addOnSuccessListener { resultado ->
                val docs = resultado.documents.map { Pair(it.id, it.data ?: emptyMap()) }
                eventosCarregados = EventsManager.MapearListaDocumentos(
                    docs.filter { (_, dados) -> dados["publicado"] != false && dados["encerrado"] != true }
                )
                carregandoEventos = false
                erroAoCarregar = false
            }
            .addOnFailureListener {
                carregandoEventos = false
                erroAoCarregar = true
            }
    }

    private fun abrirDetalhes(evento: EventoBusca) {
        val intent = Intent(this, EventDetailsActivity::class.java).apply {
            putExtra("idEvento", evento.id)
            putExtra("nomeEvento", evento.nome)
            putExtra("dataEvento", evento.data)
            putExtra("horarioEvento", evento.horario)
            putExtra("localEvento", evento.local)
            putExtra("descricaoEvento", evento.descricao)
            putExtra("enderecoEvento", evento.endereco)
        }
        startActivity(intent)
    }
}
