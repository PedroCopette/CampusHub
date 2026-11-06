
package br.com.loginfirebase

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth

private val CampusNavy = Color(0xFF172554)
private val CampusBlue = Color(0xFF3568D4)
private val CampusBackground = Color(0xFFF3F6FC)
private val CampusText = Color(0xFF24324A)
private val CampusMuted = Color(0xFF718096)

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    companion object {
        fun validarLogin(email: String, senha: String): Boolean {
            return CadastroValidator.loginCamposPreenchidos(email, senha)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            LoginFirebaseTheme {

                TelaLogin(
                    entrar = { email, senha ->
                        fazerLogin(email, senha)
                    },
                    abrirCadastro = {
                        startActivity(
                            Intent(
                                this,
                                RegistrationActivity::class.java
                            )
                        )
                    },
                    recuperarSenha = {
                        startActivity(
                            Intent(
                                this,
                                RecoveryActivity::class.java
                            )
                        )
                    }
                )
            }
        }
    }

    private fun fazerLogin(email: String, senha: String) {
        val (valido, mensagem) = AuthManager.validarLogin(email, senha)
        if (!valido) {
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(
            email.trim(),
            senha.trim()
        ).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                Toast.makeText(
                    this,
                    "Login realizado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(
                    Intent(
                        this,
                        DashboardActivity::class.java
                    )
                )

                finish()

            } else {

                Toast.makeText(
                    this,
                    "E-mail ou senha incorretos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

@Composable
fun TelaLogin(
    entrar: (String, String) -> Unit,
    abrirCadastro: () -> Unit,
    recuperarSenha: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CampusBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(245.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 34.dp,
                        bottomEnd = 34.dp
                    )
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CampusNavy,
                            CampusBlue
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp),
                verticalArrangement = Arrangement.Center
            ) {

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

                    Spacer(modifier = Modifier.size(12.dp))

                    Column {
                        Text(
                            text = "CampusHub",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "SUA UNIVERSIDADE CONECTADA",
                            color = Color.White.copy(alpha = 0.82f),
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Seu campus,\nmais conectado.",
                    color = Color.White,
                    fontSize = 28.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = "Eventos, atividades e oportunidades num só lugar.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-24).dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 7.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = "Bem-vindo de volta!",
                    color = CampusText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Entre com sua conta para continuar.",
                    color = CampusMuted,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(22.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    placeholder = { Text("seu@email.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CampusBlue,
                        unfocusedBorderColor = Color(0xFFD6DEEA),
                        focusedLabelColor = CampusBlue,
                        cursorColor = CampusBlue
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    label = { Text("Senha") },
                    placeholder = { Text("Digite sua senha") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CampusBlue,
                        unfocusedBorderColor = Color(0xFFD6DEEA),
                        focusedLabelColor = CampusBlue,
                        cursorColor = CampusBlue
                    )
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = { entrar(email, senha) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CampusBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "ENTRAR",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = abrirCadastro,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEAF0FF),
                        contentColor = CampusNavy
                    )
                ) {
                    Text(
                        text = "CRIAR CONTA",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                TextButton(
                    onClick = recuperarSenha,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Esqueci minha senha",
                        color = CampusBlue,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Text(
            text = "CAMPUSHUB • URI SANTIAGO",
            color = CampusMuted,
            fontSize = 11.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(
                start = 12.dp,
                end = 12.dp,
                bottom = 18.dp
            )
        )
    }
}
