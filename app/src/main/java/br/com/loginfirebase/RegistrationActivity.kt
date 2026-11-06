package br.com.loginfirebase

import android.content.Context
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
import com.google.firebase.auth.UserProfileChangeRequest

private val RegistrationNavy = Color(0xFF172554)
private val RegistrationBlue = Color(0xFF3568D4)
private val RegistrationBackground = Color(0xFFF3F6FC)
private val RegistrationText = Color(0xFF24324A)
private val RegistrationMuted = Color(0xFF718096)

class RegistrationActivity : ComponentActivity() {

    companion object {
        fun validarCadastro(nome: String, email: String, senha: String): Boolean {
            return CadastroValidator.camposPreenchidos(nome, email, senha) && CadastroValidator.senhaValida(senha)
        }
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        setContent {
            LoginFirebaseTheme {
                TelaCadastro(
                    registrar = { nome, email, senha ->
                        cadastrarUsuario(nome, email, senha)
                    },
                    voltar = {
                        finish()
                    }
                )
            }
        }
    }

    private fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String
    ) {
        val (valido, mensagem) = AuthManager.validarCadastro(nome, email, senha)
        if (!valido) {
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
            return
        }

        val nomeFinal = nome.trim()
        val emailFinal = email.trim()
        val senhaFinal = senha.trim()

        auth.createUserWithEmailAndPassword(
            emailFinal,
            senhaFinal
        ).addOnCompleteListener(this) { task ->

            if (task.isSuccessful) {

                val usuario = auth.currentUser

                if (usuario != null) {

                    val atualizacaoPerfil =
                        UserProfileChangeRequest.Builder()
                            .setDisplayName(nomeFinal)
                            .build()

                    usuario.updateProfile(atualizacaoPerfil)
                        .addOnCompleteListener {

                            val preferencias = getSharedPreferences(
                                "CampusHubPrefs",
                                Context.MODE_PRIVATE
                            )

                            preferencias.edit()
                                .putString("nome", nomeFinal)
                                .putString("email", emailFinal)
                                .apply()

                            auth.signOut()

                            Toast.makeText(
                                this,
                                "Conta criada com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()

                            finish()
                        }

                } else {

                    Toast.makeText(
                        this,
                        "Erro ao criar o perfil.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                Toast.makeText(
                    this,
                    "Erro ao criar conta: ${task.exception?.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

@Composable
fun TelaCadastro(
    registrar: (String, String, String) -> Unit,
    voltar: () -> Unit
) {

    var nome by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var senha by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RegistrationBackground)
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
                            RegistrationNavy,
                            RegistrationBlue
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
                            .clip(RoundedCornerShape(15.dp))
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
                    text = "Crie sua conta",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Faça parte da comunidade CampusHub.",
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
                modifier = Modifier.height(25.dp)
            )

            Text(
                text = "Vamos começar!",
                color = RegistrationText,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Preencha seus dados para criar sua conta.",
                color = RegistrationMuted,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
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
                        .padding(20.dp)
                ) {

                    Text(
                        text = "NOME COMPLETO",
                        color = RegistrationMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    OutlinedTextField(
                        value = nome,
                        onValueChange = {
                            nome = it
                        },
                        label = {
                            Text("Nome")
                        },
                        placeholder = {
                            Text("Digite seu nome completo")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RegistrationBlue,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = RegistrationBlue,
                            cursorColor = RegistrationBlue,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "E-MAIL",
                        color = RegistrationMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
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
                            imeAction = ImeAction.Next
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RegistrationBlue,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = RegistrationBlue,
                            cursorColor = RegistrationBlue,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    Text(
                        text = "SENHA",
                        color = RegistrationMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(7.dp)
                    )

                    OutlinedTextField(
                        value = senha,
                        onValueChange = {
                            senha = it
                        },
                        label = {
                            Text("Senha")
                        },
                        placeholder = {
                            Text("Mínimo de 6 caracteres")
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = RegistrationBlue,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = RegistrationBlue,
                            cursorColor = RegistrationBlue,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    Button(
                        onClick = {
                            registrar(nome, email, senha)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RegistrationBlue,
                            contentColor = Color.White
                        )
                    ) {

                        Text(
                            text = "CRIAR CONTA",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {
                            voltar()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RegistrationBackground,
                            contentColor = RegistrationNavy
                        )
                    ) {

                        Text(
                            text = "VOLTAR",
                            fontSize = 14.sp,
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
                color = RegistrationMuted,
                fontSize = 10.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}