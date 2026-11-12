package br.com.loginfirebase.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
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
import br.com.loginfirebase.OpcaoDashboard
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val CampusBackground = Color(0xFFF3F7FD)
private val CampusText = Color(0xFF142748)
private val CampusMuted = Color(0xFF657694)

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
        auth.currentUser?.uid?.let { uid ->
            FirebaseFirestore.getInstance().collection("usuarios").document(uid).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists() && snapshot.getString("perfil") != null) perfilUsuario = snapshot.getString("perfil")!!
                    else solicitarPerfil = true
                }
                .addOnFailureListener { android.widget.Toast.makeText(this, "Não foi possível carregar seu perfil. Confira as regras do Firestore.", android.widget.Toast.LENGTH_LONG).show() }
        }

        setContent {
            LoginFirebaseTheme {

                if (solicitarPerfil) {
                    AlertDialog(
                        onDismissRequest = {},
                        title = { Text("Complete seu perfil") },
                        text = { Text("Escolha o tipo da sua conta para liberar as funções correspondentes.") },
                        confirmButton = {
                            TextButton(onClick = { salvarPerfil("organizador") }) { Text("Sou organizador") }
                        },
                        dismissButton = {
                            TextButton(onClick = { salvarPerfil("aluno") }) { Text("Sou aluno") }
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CampusBackground)
                        .statusBarsPadding()
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF1B397F),
                                        Color(0xFF2454B9),
                                        Color(0xFF3479ED)
                                    )
                                )
                            )
                            .padding(horizontal = 26.dp, vertical = 28.dp)
                    ) {

                        Column {

                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(17.dp))
                                        .background(Color.White.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "CH",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

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

                            Spacer(modifier = Modifier.height(30.dp))

                            Text(
                                text = "Bem-vindo ao CampusHub!",
                                color = Color.White,
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Seu espaço acadêmico em um só lugar.",
                                color = Color.White.copy(alpha = 0.88f),
                                fontSize = 15.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 24.dp)
                    ) {

                        Text(
                            text = "O que você deseja fazer?",
                            color = CampusText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Acesse uma das opções.",
                            color = CampusMuted,
                            fontSize = 14.sp
                        )

                        if (perfilUsuario == "organizador") {
                            OpcaoDashboard(icone = "calendario", titulo = "Gerenciar Eventos", descricao = "Crie e edite seus eventos", aoClicar = { startActivity(Intent(this@DashboardActivity, OrganizerEventsActivity::class.java)) })
                            Spacer(modifier = Modifier.height(13.dp))
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        OpcaoDashboard(
                            icone = "calendario",
                            titulo = "Eventos",
                            descricao = "Explore os eventos disponíveis",
                            destaque = true,
                            aoClicar = {
                                startActivity(Intent(this@DashboardActivity, EventsActivity::class.java))
                            }
                        )

                        Spacer(modifier = Modifier.height(13.dp))

                        OpcaoDashboard(
                            icone = "ingresso",
                            titulo = "Meus Eventos",
                            descricao = "Veja suas inscrições",
                            aoClicar = {
                                startActivity(Intent(this@DashboardActivity, MyEventsActivity::class.java))
                            }
                        )

                        Spacer(modifier = Modifier.height(13.dp))

                        OpcaoDashboard(
                            icone = "estrela",
                            titulo = "Meus Favoritos",
                            descricao = "Seus eventos favoritos",
                            aoClicar = {
                                startActivity(Intent(this@DashboardActivity, FavoritesActivity::class.java))
                            }
                        )

                        Spacer(modifier = Modifier.height(13.dp))

                        OpcaoDashboard(
                            icone = "perfil",
                            titulo = "Meu Perfil",
                            descricao = "Seus dados pessoais",
                            aoClicar = {
                                startActivity(Intent(this@DashboardActivity, ProfileActivity::class.java))
                            }
                        )

                        Spacer(modifier = Modifier.height(13.dp))

                        OpcaoDashboard(
                            icone = "sair",
                            titulo = "Sair",
                            descricao = "Encerrar sessão",
                            sair = true,
                            aoClicar = {
                                auth.signOut()
                                startActivity(Intent(this@DashboardActivity, MainActivity::class.java))
                                finish()
                            }
                        )

                        Spacer(modifier = Modifier.height(22.dp))

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

    private var perfilUsuario by mutableStateOf("aluno")
    private var solicitarPerfil by mutableStateOf(false)

    private fun salvarPerfil(perfil: String) {
        val user = auth.currentUser ?: return
        val dados = mapOf("nome" to (user.displayName ?: ""), "email" to (user.email ?: ""), "perfil" to perfil)
        FirebaseFirestore.getInstance().collection("usuarios").document(user.uid).set(dados)
            .addOnSuccessListener { perfilUsuario = perfil; solicitarPerfil = false }
            .addOnFailureListener { erro -> android.widget.Toast.makeText(this, "Perfil não salvo: ${erro.localizedMessage}. Publique as regras do Firestore.", android.widget.Toast.LENGTH_LONG).show() }
    }
}
