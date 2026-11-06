package br.com.loginfirebase

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth

private val ProfileNavy = Color(0xFF172554)
private val ProfileBlue = Color(0xFF3568D4)
private val ProfileBackground = Color(0xFFF3F6FC)
private val ProfileText = Color(0xFF24324A)
private val ProfileMuted = Color(0xFF718096)

class ProfileActivity : ComponentActivity() {

    companion object {
        fun formatarNome(nome: String?, email: String?): String {
            return ProfileUtils.formatarNomeExibicao(nome, email)
        }
    }

    private val auth = FirebaseAuth.getInstance()

    private var nomeUsuario by mutableStateOf("Nome não informado")
    private var emailUsuario by mutableStateOf("E-mail não informado")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        carregarPerfil()

        setContent {
            LoginFirebaseTheme {
                TelaPerfilCampus(
                    nome = nomeUsuario,
                    email = emailUsuario,
                    editar = {
                        startActivity(
                            Intent(
                                this@ProfileActivity,
                                EditProfileActivity::class.java
                            )
                        )
                    },
                    voltar = {
                        finish()
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        carregarPerfil()
    }

    private fun carregarPerfil() {
        val usuario = auth.currentUser
        val (nome, email) = ProfileManager.tratarDadosPerfil(usuario?.displayName, usuario?.email)
        nomeUsuario = nome
        emailUsuario = email
    }
}

@Composable
internal fun TelaPerfilCampus(
    nome: String,
    email: String,
    editar: () -> Unit,
    voltar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ProfileBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        bottomStart = 30.dp,
                        bottomEnd = 30.dp
                    )
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ProfileNavy,
                            ProfileBlue
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(Color.White.copy(alpha = 0.17f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "CH",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "CampusHub",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "ÁREA DO ALUNO",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Meu Perfil",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Consulte seus dados pessoais.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(Color(0xFFEAF0FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👤",
                            fontSize = 40.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = nome,
                        color = ProfileText,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(7.dp))

                    Text(
                        text = "Estudante CampusHub",
                        color = ProfileMuted,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    CampoPerfil(
                        titulo = "NOME COMPLETO",
                        valor = nome
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    CampoPerfil(
                        titulo = "E-MAIL DA CONTA",
                        valor = email
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = editar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProfileBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "EDITAR PERFIL",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = voltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = ProfileNavy
                )
            ) {
                Text(
                    text = "VOLTAR",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "CAMPUSHUB • URI SANTIAGO",
                color = ProfileMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
internal fun CampoPerfil(
    titulo: String,
    valor: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = titulo,
            color = ProfileMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(ProfileBackground)
                .padding(14.dp)
        ) {
            Text(
                text = valor,
                color = ProfileText,
                fontSize = 14.sp
            )
        }
    }
}