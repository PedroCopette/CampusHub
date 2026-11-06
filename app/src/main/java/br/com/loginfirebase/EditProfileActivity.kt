package br.com.loginfirebase

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

private val EditNavy = Color(0xFF172554)
private val EditBlue = Color(0xFF3568D4)
private val EditBackground = Color(0xFFF3F6FC)
private val EditTextColor = Color(0xFF24324A)
private val EditMuted = Color(0xFF718096)

class EditProfileActivity : ComponentActivity() {

    companion object {
        fun validarEdicao(nome: String): Boolean {
            return ProfileUtils.validarEdicaoNome(nome)
        }
    }

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usuario = auth.currentUser

        if (usuario == null) {
            finish()
            return
        }

        val nomeAtual = usuario.displayName ?: ""
        val emailAtual = usuario.email ?: ""

        setContent {
            LoginFirebaseTheme {
                TelaEditarPerfil(
                    nomeInicial = nomeAtual,
                    email = emailAtual,

                    salvar = { nome ->
                        val (valido, mensagem) = ProfileManager.validarEdicaoPerfil(nome)
                        if (!valido) {
                            Toast.makeText(this@EditProfileActivity, mensagem, Toast.LENGTH_SHORT).show()
                        } else {
                            val atualizacao = UserProfileChangeRequest.Builder()
                                .setDisplayName(nome.trim())
                                .build()

                            usuario.updateProfile(atualizacao)
                                .addOnCompleteListener { task ->

                                    if (task.isSuccessful) {

                                        Toast.makeText(
                                            this@EditProfileActivity,
                                            "Perfil atualizado!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        finish()

                                    } else {

                                        Toast.makeText(
                                            this@EditProfileActivity,
                                            "Não foi possível atualizar o perfil.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    },

                    voltar = {
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
internal fun TelaEditarPerfil(
    nomeInicial: String,
    email: String,
    salvar: (String) -> Unit,
    voltar: () -> Unit
) {
    var nome by remember {
        mutableStateOf(nomeInicial)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(EditBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        // Cabeçalho azul
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
                            EditNavy,
                            EditBlue
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
                    text = "Editar Perfil",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Mantenha suas informações atualizadas.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        // Formulário
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "Seus dados",
                color = EditTextColor,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Altere seu nome e confira o e-mail cadastrado.",
                color = EditMuted,
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
                        .padding(21.dp)
                ) {

                    // Campo de nome
                    Text(
                        text = "NOME COMPLETO",
                        color = EditMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
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
                            Text("Digite seu nome")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EditBlue,
                            unfocusedBorderColor = Color(0xFFD6DEEA),
                            focusedLabelColor = EditBlue,
                            cursorColor = EditBlue,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(23.dp)
                    )

                    // E-mail da conta
                    Text(
                        text = "E-MAIL DA CONTA",
                        color = EditMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.7.sp
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {},
                        enabled = false,
                        label = {
                            Text("E-mail")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = EditMuted,
                            disabledLabelColor = EditMuted,
                            disabledBorderColor = Color(0xFFE0E6F0),
                            disabledContainerColor = EditBackground
                        )
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "O e-mail é exibido apenas para consulta nesta tela.",
                        color = EditMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            // Salvar
            Button(
                onClick = {
                    salvar(nome)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EditBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "SALVAR ALTERAÇÕES",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            // Voltar
            Button(
                onClick = voltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EditNavy
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
                text = "CAMPUSHUB • URI SANTIAGO",
                color = EditMuted,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}