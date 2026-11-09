package br.com.loginfirebase.activities

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.AuthManager
import br.com.loginfirebase.CadastroValidator
import br.com.loginfirebase.RecoveryManager
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth

private val RecoveryNavy = Color(0xFF172554)
private val RecoveryBlue = Color(0xFF3568D4)
private val RecoveryBackground = Color(0xFFF3F6FC)
private val RecoveryText = Color(0xFF24324A)
private val RecoveryMuted = Color(0xFF718096)

class RecoveryActivity : ComponentActivity() {

    companion object {
        fun validarEmailRecuperacao(email: String): Boolean {
            return email.trim().isNotEmpty() && CadastroValidator.emailValido(email)
        }
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            LoginFirebaseTheme {

                var email by remember {
                    mutableStateOf("")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(RecoveryBackground)
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
                                        RecoveryNavy,
                                        RecoveryBlue
                                    )
                                )
                            )
                            .padding(
                                horizontal = 24.dp,
                                vertical = 25.dp
                            )
                    ) {

                        Column {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(
                                            RoundedCornerShape(15.dp)
                                        )
                                        .background(
                                            Color.White.copy(alpha = 0.17f)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Text(
                                        text = "CH",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(
                                    modifier = Modifier.width(12.dp)
                                )

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
                                        fontSize = 10.sp,
                                        letterSpacing = 1.sp
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(23.dp)
                            )

                            Text(
                                text = "Recuperar senha",
                                color = Color.White,
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(6.dp)
                            )

                            Text(
                                text = "Vamos ajudar você a recuperar o acesso.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {

                        Spacer(
                            modifier = Modifier.height(27.dp)
                        )

                        Text(
                            text = "Esqueceu sua senha?",
                            color = RecoveryText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(7.dp)
                        )

                        Text(
                            text = "Informe o e-mail cadastrado. Enviaremos um link para você redefinir sua senha.",
                            color = RecoveryMuted,
                            fontSize = 13.sp
                        )

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

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
                                    .padding(21.dp)
                            ) {

                                Text(
                                    text = "E-MAIL DA CONTA",
                                    color = RecoveryMuted,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.7.sp
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                OutlinedTextField(
                                    value = email,
                                    onValueChange = {
                                        email = it
                                    },
                                    label = {
                                        Text("E-mail")
                                    },
                                    placeholder = {
                                        Text("seu@email.com")
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email,
                                        imeAction = ImeAction.Done
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = RecoveryBlue,
                                        unfocusedBorderColor = Color(0xFFD6DEEA),
                                        focusedLabelColor = RecoveryBlue,
                                        cursorColor = RecoveryBlue,
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White
                                    )
                                )

                                Spacer(
                                    modifier = Modifier.height(22.dp)
                                )

                                Button(
                                    onClick = {
                                        enviarRecuperacao(email)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = RecoveryBlue,
                                        contentColor = Color.White
                                    )
                                ) {

                                    Text(
                                        text = "ENVIAR LINK DE RECUPERAÇÃO",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
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
                                        containerColor = RecoveryBackground,
                                        contentColor = RecoveryNavy
                                    )
                                ) {

                                    Text(
                                        text = "VOLTAR AO LOGIN",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(20.dp)
                        )

                        Text(
                            text = "CAMPUSHUB • URI SANTIAGO",
                            color = RecoveryMuted,
                            fontSize = 10.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    private fun enviarRecuperacao(email: String) {
        val (valido, mensagem) = RecoveryManager.validarEmailRecuperacao(email)
        if (!valido) {
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email.trim())
            .addOnCompleteListener(this) { task ->
                val msg = AuthManager.processarRespostaRecuperacao(task.isSuccessful)
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                if (task.isSuccessful) finish()
            }
    }
}
