package br.com.loginfirebase

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.cos
import kotlin.math.sin

private val CampusNavy = Color(0xFF172D59)
private val CampusBlue = Color(0xFF2864D7)
private val CampusBackground = Color(0xFFF3F7FD)
private val CampusText = Color(0xFF142748)
private val CampusMuted = Color(0xFF657694)
private val CampusIconBackground = Color(0xFFEBF2FE)
private val CampusBorder = Color(0xFFE2EAF6)
private val CampusRed = Color(0xFFD42121)
private val CampusRedBackground = Color(0xFFFFF0F0)

class DashboardActivity : ComponentActivity() {

    companion object {
        fun obterTituloArea(): String = "ÁREA DO ALUNO"
        fun tratarCliqueOpcao(opcao: String): String {
            return when (opcao) {
                "Eventos" -> "EventsActivity"
                "Meus Eventos" -> "MyEventsActivity"
                "Meus Favoritos" -> "FavoritesActivity"
                "Meu Perfil" -> "ProfileActivity"
                "Sair" -> "MainActivity"
                else -> "Desconhecido"
            }
        }
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            LoginFirebaseTheme {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CampusBackground)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                ) {

                    // Cabeçalho
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 32.dp,
                                    bottomEnd = 32.dp
                                )
                            )
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1B397F),
                                        Color(0xFF2454B9),
                                        Color(0xFF3479ED)
                                    )
                                )
                            )
                            .padding(
                                horizontal = 26.dp,
                                vertical = 28.dp
                            )
                    ) {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(17.dp))
                                        .background(
                                            Color.White.copy(alpha = 0.15f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = "CH",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.width(14.dp)
                                )

                                Column {

                                    Text(
                                        text = "CampusHub",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "ÁREA DO ALUNO",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 11.sp,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(30.dp)
                            )

                            Text(
                                text = "Bem-vindo ao CampusHub!",
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )

                            Text(
                                text = "Seu espaço acadêmico em um só lugar.",
                                color = Color.White.copy(alpha = 0.88f),
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Menu principal
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 22.dp,
                                vertical = 24.dp
                            )
                    ) {

                        Text(
                            text = "O que você deseja fazer?",
                            color = CampusText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Acesse uma das opções.",
                            color = CampusMuted,
                            fontSize = 14.sp
                        )

                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )

                        // EVENTOS
                        OpcaoDashboard(
                            icone = "calendario",
                            titulo = "Eventos",
                            descricao = "Explore os eventos disponíveis",
                            destaque = true,
                            aoClicar = {
                                startActivity(
                                    Intent(
                                        this@DashboardActivity,
                                        EventsActivity::class.java
                                    )
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        // MEUS EVENTOS
                        OpcaoDashboard(
                            icone = "ingresso",
                            titulo = "Meus Eventos",
                            descricao = "Veja suas inscrições",
                            aoClicar = {
                                startActivity(
                                    Intent(
                                        this@DashboardActivity,
                                        MyEventsActivity::class.java
                                    )
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        // MEUS FAVORITOS
                        OpcaoDashboard(
                            icone = "estrela",
                            titulo = "Meus Favoritos",
                            descricao = "Seus eventos favoritos",
                            aoClicar = {
                                startActivity(
                                    Intent(
                                        this@DashboardActivity,
                                        FavoritesActivity::class.java
                                    )
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        // MEU PERFIL
                        OpcaoDashboard(
                            icone = "perfil",
                            titulo = "Meu Perfil",
                            descricao = "Seus dados pessoais",
                            aoClicar = {
                                startActivity(
                                    Intent(
                                        this@DashboardActivity,
                                        ProfileActivity::class.java
                                    )
                                )
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(13.dp)
                        )

                        // SAIR
                        OpcaoDashboard(
                            icone = "sair",
                            titulo = "Sair",
                            descricao = "Encerrar sessão",
                            sair = true,
                            aoClicar = {
                                auth.signOut()

                                startActivity(
                                    Intent(
                                        this@DashboardActivity,
                                        MainActivity::class.java
                                    )
                                )

                                finish()
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(22.dp)
                        )

                        Text(
                            text = "CAMPUSHUB  •  URI SANTIAGO",
                            color = Color(0xFF8798B6),
                            fontSize = 11.sp,
                            letterSpacing = 1.3.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun OpcaoDashboard(
    icone: String,
    titulo: String,
    descricao: String,
    destaque: Boolean = false,
    sair: Boolean = false,
    aoClicar: () -> Unit
) {

    val corFundo = when {
        destaque -> Color(0xFFF0F5FF)
        sair -> CampusRedBackground
        else -> Color.White
    }

    val corIcone = if (sair) CampusRed else CampusNavy

    val corTexto = if (sair) CampusRed else CampusText

    val corDescricao = if (sair) {
        Color(0xFFBE5555)
    } else {
        CampusMuted
    }

    val corBorda = when {
        destaque -> Color(0xFF91B6FF)
        sair -> Color(0xFFFFD0D0)
        else -> CampusBorder
    }

    Card(
        onClick = aoClicar,
        modifier = Modifier
            .fillMaxWidth()
            .height(94.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = corFundo
        ),
        border = BorderStroke(
            width = if (destaque) 1.5.dp else 1.dp,
            color = corBorda
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (destaque) 1.dp else 2.dp,
            pressedElevation = 3.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Fundo do ícone
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(
                        when {
                            destaque -> Color(0xFFDDE9FF)
                            sair -> Color(0xFFFFDEDE)
                            else -> CampusIconBackground
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {

                IconeLinha(
                    tipo = icone,
                    cor = corIcone
                )
            }

            Spacer(
                modifier = Modifier.width(15.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = corTexto
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = descricao,
                    fontSize = 12.sp,
                    color = corDescricao,
                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            IconeSeta(
                cor = if (sair) CampusRed else CampusNavy
            )
        }
    }
}

// Ícones de contorno simples, sem emojis
@Composable
internal fun IconeLinha(
    tipo: String,
    cor: Color
) {

    Canvas(
        modifier = Modifier.size(30.dp)
    ) {

        val s = size.width

        val traco = Stroke(
            width = 2.4.dp.toPx()
        )

        when (tipo) {

            "calendario" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(s * 0.16f, s * 0.22f),
                    size = Size(s * 0.68f, s * 0.65f),
                    cornerRadius = CornerRadius(s * 0.08f),
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.16f, s * 0.42f),
                    end = Offset(s * 0.84f, s * 0.42f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.34f, s * 0.12f),
                    end = Offset(s * 0.34f, s * 0.31f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.66f, s * 0.12f),
                    end = Offset(s * 0.66f, s * 0.31f),
                    strokeWidth = traco.width
                )
            }

            "ingresso" -> {

                val caminho = Path().apply {
                    moveTo(s * 0.20f, s * 0.22f)
                    lineTo(s * 0.80f, s * 0.22f)
                    lineTo(s * 0.80f, s * 0.38f)

                    cubicTo(
                        s * 0.62f, s * 0.42f,
                        s * 0.62f, s * 0.58f,
                        s * 0.80f, s * 0.62f
                    )

                    lineTo(s * 0.80f, s * 0.78f)
                    lineTo(s * 0.20f, s * 0.78f)
                    lineTo(s * 0.20f, s * 0.62f)

                    cubicTo(
                        s * 0.38f, s * 0.58f,
                        s * 0.38f, s * 0.42f,
                        s * 0.20f, s * 0.38f
                    )

                    close()
                }

                drawPath(
                    path = caminho,
                    color = cor,
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.50f, s * 0.32f),
                    end = Offset(s * 0.50f, s * 0.68f),
                    strokeWidth = traco.width,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(4f, 4f)
                    )
                )
            }

            "estrela" -> {

                val caminho = Path()

                for (i in 0 until 10) {

                    val angulo =
                        Math.PI * i / 5.0 - Math.PI / 2.0

                    val raio = if (i % 2 == 0) {
                        s * 0.42f
                    } else {
                        s * 0.19f
                    }

                    val x = s * 0.5f +
                            cos(angulo).toFloat() * raio

                    val y = s * 0.5f +
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
                    style = traco
                )
            }

            "perfil" -> {

                drawCircle(
                    color = cor,
                    radius = s * 0.16f,
                    center = Offset(s * 0.50f, s * 0.28f),
                    style = traco
                )

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(s * 0.18f, s * 0.53f),
                    size = Size(s * 0.64f, s * 0.34f),
                    cornerRadius = CornerRadius(s * 0.18f),
                    style = traco
                )
            }

            "sair" -> {

                drawRoundRect(
                    color = cor,
                    topLeft = Offset(s * 0.10f, s * 0.16f),
                    size = Size(s * 0.42f, s * 0.68f),
                    cornerRadius = CornerRadius(s * 0.06f),
                    style = traco
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.40f, s * 0.50f),
                    end = Offset(s * 0.88f, s * 0.50f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.68f, s * 0.30f),
                    end = Offset(s * 0.88f, s * 0.50f),
                    strokeWidth = traco.width
                )

                drawLine(
                    color = cor,
                    start = Offset(s * 0.68f, s * 0.70f),
                    end = Offset(s * 0.88f, s * 0.50f),
                    strokeWidth = traco.width
                )
            }
        }
    }
}

@Composable
internal fun IconeSeta(
    cor: Color
) {

    Canvas(
        modifier = Modifier.size(19.dp)
    ) {

        val s = size.width
        val traco = 2.2.dp.toPx()

        drawLine(
            color = cor,
            start = Offset(s * 0.36f, s * 0.18f),
            end = Offset(s * 0.68f, s * 0.50f),
            strokeWidth = traco
        )

        drawLine(
            color = cor,
            start = Offset(s * 0.68f, s * 0.50f),
            end = Offset(s * 0.36f, s * 0.82f),
            strokeWidth = traco
        )
    }
}