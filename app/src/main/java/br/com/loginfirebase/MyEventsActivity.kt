package br.com.loginfirebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val MyEventsNavy = Color(0xFF172D59)
private val MyEventsBlue = Color(0xFF2864D7)
private val MyEventsBackground = Color(0xFFF3F7FD)
private val MyEventsText = Color(0xFF24324A)
private val MyEventsMuted = Color(0xFF657694)
private val MyEventsBorder = Color(0xFFE0E9F7)
private val MyEventsIconBackground = Color(0xFFEAF1FE)
private val MyEventsGreen = Color(0xFF237A4B)

class MyEventsActivity : ComponentActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var eventosInscritos by mutableStateOf<List<Evento>>(
        emptyList()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MyEventsBackground)
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
                            modifier = Modifier.height(22.dp)
                        )

                        Text(
                            text = "Meus eventos",
                            color = MyEventsNavy,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Suas inscrições",
                            color = MyEventsMuted,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(25.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Eventos inscritos",
                                color = MyEventsText,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0xFFE5EDFC))
                                    .padding(
                                        horizontal = 13.dp,
                                        vertical = 7.dp
                                    )
                            ) {

                                Text(
                                    text = "${eventosInscritos.size}",
                                    color = MyEventsBlue,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(16.dp)
                        )

                        if (eventosInscritos.isEmpty()) {

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    MyEventsBorder
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(25.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(MyEventsIconBackground),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        IconeMeusEventos(
                                            tipo = "calendario",
                                            cor = MyEventsNavy,
                                            tamanho = 34.dp
                                        )
                                    }

                                    Text(
                                        text = "Nenhuma inscrição",
                                        color = MyEventsText,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Seus eventos inscritos aparecerão aqui.",
                                        color = MyEventsMuted,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }

                        } else {

                            eventosInscritos.forEach { evento ->

                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(22.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        MyEventsBorder
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 2.dp
                                    )
                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(17.dp),
                                        verticalArrangement = Arrangement.spacedBy(14.dp)
                                    ) {

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Box(
                                                modifier = Modifier
                                                    .size(54.dp)
                                                    .clip(RoundedCornerShape(17.dp))
                                                    .background(MyEventsIconBackground),
                                                contentAlignment = Alignment.Center
                                            ) {

                                                IconeMeusEventos(
                                                    tipo = "calendario",
                                                    cor = MyEventsNavy,
                                                    tamanho = 30.dp
                                                )
                                            }

                                            Spacer(
                                                modifier = Modifier.width(14.dp)
                                            )

                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(5.dp)
                                            ) {

                                                Text(
                                                    text = evento.nome,
                                                    color = MyEventsText,
                                                    fontSize = 17.sp,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    text = "INSCRIÇÃO REALIZADA",
                                                    color = MyEventsGreen,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    letterSpacing = 0.5.sp
                                                )
                                            }
                                        }

                                        HorizontalDivider(
                                            color = MyEventsBorder
                                        )

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            IconeMeusEventos(
                                                tipo = "calendario",
                                                cor = MyEventsBlue,
                                                tamanho = 23.dp
                                            )

                                            Spacer(
                                                modifier = Modifier.width(13.dp)
                                            )

                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {

                                                Text(
                                                    text = "Data e horário",
                                                    color = MyEventsMuted,
                                                    fontSize = 12.sp
                                                )

                                                Text(
                                                    text = "${evento.data}  •  ${evento.horario}",
                                                    color = MyEventsText,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            IconeMeusEventos(
                                                tipo = "localizacao",
                                                cor = MyEventsBlue,
                                                tamanho = 23.dp
                                            )

                                            Spacer(
                                                modifier = Modifier.width(13.dp)
                                            )

                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(3.dp)
                                            ) {

                                                Text(
                                                    text = "Local",
                                                    color = MyEventsMuted,
                                                    fontSize = 12.sp
                                                )

                                                Text(
                                                    text = evento.local,
                                                    color = MyEventsText,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Button(
                            onClick = {
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MyEventsNavy,
                                contentColor = Color.White
                            )
                        ) {

                            Text(
                                text = "VOLTAR",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(20.dp)
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
    }

    override fun onResume() {
        super.onResume()
        carregarEventos()
    }

    private fun carregarEventos() {

        val usuario = auth.currentUser

        if (usuario == null) {

            Toast.makeText(
                this,
                "Usuário não está logado.",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        db.collection("inscricoes")
            .whereEqualTo("usuarioId", usuario.uid)
            .get()
            .addOnSuccessListener { resultado ->

                val lista = resultado.documents.mapNotNull { documento ->

                    val nome = documento.getString("nomeEvento")
                    val data = documento.getString("dataEvento")
                    val horario = documento.getString("horarioEvento")
                    val local = documento.getString("localEvento")

                    if (
                        nome != null &&
                        data != null &&
                        horario != null &&
                        local != null
                    ) {

                        Evento(
                            nome = nome,
                            data = data,
                            horario = horario,
                            local = local
                        )

                    } else {
                        null
                    }
                }

                eventosInscritos = lista
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao carregar seus eventos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

@Composable
private fun IconeMeusEventos(
    tipo: String,
    cor: Color,
    tamanho: androidx.compose.ui.unit.Dp
) {

    Canvas(
        modifier = Modifier.size(tamanho)
    ) {

        val s = size.width

        val traco = Stroke(
            width = 2.3.dp.toPx()
        )

        when (tipo) {

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

                drawCircle(
                    color = cor,
                    radius = s * 0.035f,
                    center = Offset(s * 0.34f, s * 0.56f)
                )

                drawCircle(
                    color = cor,
                    radius = s * 0.035f,
                    center = Offset(s * 0.52f, s * 0.56f)
                )

                drawCircle(
                    color = cor,
                    radius = s * 0.035f,
                    center = Offset(s * 0.68f, s * 0.56f)
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
                    radius = s * 0.11f,
                    center = Offset(s * 0.50f, s * 0.38f),
                    style = traco
                )
            }
        }
    }
}

data class Evento(
    val id: String = "",
    val nome: String,
    val data: String,
    val horario: String,
    val local: String
)