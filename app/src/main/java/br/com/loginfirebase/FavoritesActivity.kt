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
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class FavoritoEvento(
    val id: String,
    val nome: String,
    val data: String,
    val horario: String,
    val local: String
)

private val FavoritesNavy = Color(0xFF172D59)
private val FavoritesBlue = Color(0xFF2864D7)
private val FavoritesBackground = Color(0xFFF3F6FC)
private val FavoritesText = Color(0xFF24324A)
private val FavoritesMuted = Color(0xFF718096)
private val FavoritesBorder = Color(0xFFE2E8F0)
private val FavoritesIconBackground = Color(0xFFEAF0FF)
private val FavoritesRed = Color(0xFFB42318)
private val FavoritesRedBackground = Color(0xFFFFEEEE)

class FavoritesActivity : ComponentActivity() {

    companion object {
        fun formatarTextoLista(count: Int): String {
            return FavoritesUtils.formatarContadorFavoritos(count)
        }
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var eventosFavoritos by mutableStateOf<List<FavoritoEvento>>(
        emptyList()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FavoritesBackground)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 22.dp)
                ) {

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(FavoritesIconBackground),
                            contentAlignment = Alignment.Center
                        ) {
                            IconeFavoritos(
                                tipo = "star",
                                cor = FavoritesBlue,
                                tamanho = 28.dp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Meus favoritos",
                                color = FavoritesNavy,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Eventos que você salvou",
                                color = FavoritesMuted,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Eventos salvos",
                            color = FavoritesText,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(FavoritesIconBackground)
                                .padding(
                                    horizontal = 14.dp,
                                    vertical = 7.dp
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = eventosFavoritos.size.toString(),
                                color = FavoritesBlue,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (eventosFavoritos.isEmpty()) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(
                                1.dp,
                                FavoritesBorder
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 24.dp,
                                        vertical = 32.dp
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(23.dp))
                                        .background(FavoritesIconBackground),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconeFavoritos(
                                        tipo = "star",
                                        cor = FavoritesBlue,
                                        tamanho = 38.dp
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.height(18.dp)
                                )

                                Text(
                                    text = "Nenhum favorito ainda",
                                    color = FavoritesText,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text = "Os eventos que você favoritar aparecerão aqui.",
                                    color = FavoritesMuted,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 21.sp
                                )
                            }
                        }

                    } else {

                        eventosFavoritos.forEach { evento ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 14.dp),
                                shape = RoundedCornerShape(21.dp),
                                border = BorderStroke(
                                    1.dp,
                                    FavoritesBorder
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 0.dp
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
                                                .size(50.dp)
                                                .clip(RoundedCornerShape(15.dp))
                                                .background(
                                                    FavoritesIconBackground
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            IconeFavoritos(
                                                tipo = "star",
                                                cor = FavoritesBlue,
                                                tamanho = 25.dp
                                            )
                                        }

                                        Spacer(
                                            modifier = Modifier.width(13.dp)
                                        )

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {

                                            Text(
                                                text = evento.nome,
                                                color = FavoritesText,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold
                                            )

                                            Spacer(
                                                modifier = Modifier.height(6.dp)
                                            )

                                            Text(
                                                text = "Salvo nos favoritos",
                                                color = FavoritesBlue,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Spacer(
                                        modifier = Modifier.height(18.dp)
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(FavoritesBackground)
                                            .padding(13.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        IconeFavoritos(
                                            tipo = "calendar",
                                            cor = FavoritesBlue,
                                            tamanho = 23.dp
                                        )

                                        Spacer(
                                            modifier = Modifier.width(12.dp)
                                        )

                                        Column {
                                            Text(
                                                text = "Data e horário",
                                                color = FavoritesMuted,
                                                fontSize = 12.sp
                                            )

                                            Spacer(
                                                modifier = Modifier.height(4.dp)
                                            )

                                            Text(
                                                text = "${evento.data} • ${evento.horario}",
                                                color = FavoritesText,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    Spacer(
                                        modifier = Modifier.height(14.dp)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.Top
                                    ) {

                                        IconeFavoritos(
                                            tipo = "location",
                                            cor = FavoritesBlue,
                                            tamanho = 22.dp
                                        )

                                        Spacer(
                                            modifier = Modifier.width(10.dp)
                                        )

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {

                                            Text(
                                                text = "Local",
                                                color = FavoritesMuted,
                                                fontSize = 12.sp
                                            )

                                            Spacer(
                                                modifier = Modifier.height(4.dp)
                                            )

                                            Text(
                                                text = evento.local,
                                                color = FavoritesText,
                                                fontSize = 14.sp
                                            )
                                        }
                                    }

                                    Spacer(
                                        modifier = Modifier.height(18.dp)
                                    )

                                    Button(
                                        onClick = {
                                            removerFavorito(evento.id)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(13.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = FavoritesRedBackground,
                                            contentColor = FavoritesRed
                                        )
                                    ) {

                                        IconeFavoritos(
                                            tipo = "remove",
                                            cor = FavoritesRed,
                                            tamanho = 17.dp
                                        )

                                        Spacer(
                                            modifier = Modifier.width(9.dp)
                                        )

                                        Text(
                                            text = "Remover dos favoritos",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            finish()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FavoritesNavy,
                            contentColor = Color.White
                        )
                    ) {

                        IconeFavoritos(
                            tipo = "back",
                            cor = Color.White,
                            tamanho = 19.dp
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Voltar",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        carregarFavoritos()
    }

    private fun carregarFavoritos() {

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

        db.collection("favoritos")
            .whereEqualTo("usuarioId", usuario.uid)
            .get()
            .addOnSuccessListener { resultado ->
                val docs = resultado.documents.map { it.data ?: emptyMap() }
                eventosFavoritos = FavoritesManager.mapearListaFavoritos(docs)
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao carregar seus favoritos.",
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

                Toast.makeText(
                    this,
                    "Evento removido dos favoritos!",
                    Toast.LENGTH_SHORT
                ).show()

                carregarFavoritos()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    "Erro ao remover dos favoritos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}

@androidx.compose.runtime.Composable
internal fun IconeFavoritos(
    tipo: String,
    cor: Color,
    tamanho: Dp
) {

    Canvas(
        modifier = Modifier.size(tamanho)
    ) {

        val largura = size.width
        val altura = size.height
        val centroX = largura / 2f
        val centroY = altura / 2f
        val espessura = size.minDimension * 0.075f

        when (tipo) {

            "star" -> {

                val caminho = Path()
                val raioExterno = size.minDimension * 0.44f
                val raioInterno = raioExterno * 0.43f

                for (i in 0 until 10) {

                    val angulo = -PI / 2.0 + i * PI / 5.0
                    val raio = if (i % 2 == 0) {
                        raioExterno
                    } else {
                        raioInterno
                    }

                    val x = centroX +
                            cos(angulo).toFloat() * raio

                    val y = centroY +
                            sin(angulo).toFloat() * raio

                    if (i == 0) {
                        caminho.moveTo(x, y)
                    } else {
                        caminho.lineTo(x, y)
                    }
                }

                caminho.close()

                drawPath(
                    path = caminho,
                    color = cor,
                    style = Stroke(width = espessura)
                )
            }

            "calendar" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(
                        largura * 0.16f,
                        altura * 0.23f
                    ),
                    size = Size(
                        largura * 0.68f,
                        altura * 0.63f
                    ),
                    cornerRadius = CornerRadius(
                        largura * 0.08f
                    ),
                    style = Stroke(width = espessura)
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.16f,
                        altura * 0.43f
                    ),
                    end = Offset(
                        largura * 0.84f,
                        altura * 0.43f
                    ),
                    strokeWidth = espessura
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.36f,
                        altura * 0.12f
                    ),
                    end = Offset(
                        largura * 0.36f,
                        altura * 0.32f
                    ),
                    strokeWidth = espessura
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.64f,
                        altura * 0.12f
                    ),
                    end = Offset(
                        largura * 0.64f,
                        altura * 0.32f
                    ),
                    strokeWidth = espessura
                )
            }

            "location" -> {

                val caminho = Path().apply {

                    moveTo(
                        largura * 0.50f,
                        altura * 0.91f
                    )

                    cubicTo(
                        largura * 0.42f,
                        altura * 0.77f,
                        largura * 0.18f,
                        altura * 0.53f,
                        largura * 0.18f,
                        altura * 0.38f
                    )

                    cubicTo(
                        largura * 0.18f,
                        altura * 0.16f,
                        largura * 0.34f,
                        altura * 0.08f,
                        largura * 0.50f,
                        altura * 0.08f
                    )

                    cubicTo(
                        largura * 0.66f,
                        altura * 0.08f,
                        largura * 0.82f,
                        altura * 0.16f,
                        largura * 0.82f,
                        altura * 0.38f
                    )

                    cubicTo(
                        largura * 0.82f,
                        altura * 0.53f,
                        largura * 0.58f,
                        altura * 0.77f,
                        largura * 0.50f,
                        altura * 0.91f
                    )

                    close()
                }

                drawPath(
                    path = caminho,
                    color = cor,
                    style = Stroke(width = espessura)
                )

                drawCircle(
                    color = cor,
                    radius = size.minDimension * 0.105f,
                    center = Offset(
                        largura * 0.50f,
                        altura * 0.36f
                    ),
                    style = Stroke(width = espessura)
                )
            }

            "remove" -> {

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.22f,
                        altura * 0.22f
                    ),
                    end = Offset(
                        largura * 0.78f,
                        altura * 0.78f
                    ),
                    strokeWidth = espessura,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.78f,
                        altura * 0.22f
                    ),
                    end = Offset(
                        largura * 0.22f,
                        altura * 0.78f
                    ),
                    strokeWidth = espessura,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }

            "back" -> {

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.84f,
                        altura * 0.50f
                    ),
                    end = Offset(
                        largura * 0.19f,
                        altura * 0.50f
                    ),
                    strokeWidth = espessura,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.19f,
                        altura * 0.50f
                    ),
                    end = Offset(
                        largura * 0.48f,
                        altura * 0.22f
                    ),
                    strokeWidth = espessura,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                drawLine(
                    color = cor,
                    start = Offset(
                        largura * 0.19f,
                        altura * 0.50f
                    ),
                    end = Offset(
                        largura * 0.48f,
                        altura * 0.78f
                    ),
                    strokeWidth = espessura,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }
        }
    }
}