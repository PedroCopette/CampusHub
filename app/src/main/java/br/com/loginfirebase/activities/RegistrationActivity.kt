package br.com.loginfirebase.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import br.com.loginfirebase.AuthManager
import br.com.loginfirebase.CadastroValidator
import br.com.loginfirebase.PreferencesManager
import br.com.loginfirebase.TelaCadastroUI
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

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
                    registrar = { nome, email, senha, perfil ->
                        cadastrarUsuario(nome, email, senha, perfil)
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
        senha: String,
        perfil: String
    ) {
        val (valido, mensagem) = AuthManager.validarCadastro(nome, email, senha)
        if (!valido) {
            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show()
            return
        }

        val nomeFinal = nome.trim()
        val emailFinal = email.trim()
        val senhaFinal = senha.trim()

        auth.createUserWithEmailAndPassword(emailFinal, senhaFinal)
            .addOnCompleteListener(this) { task ->
                val msg = AuthManager.processarRespostaCadastro(task.isSuccessful, task.exception?.message)
                if (task.isSuccessful && auth.currentUser != null) {
                    val req = UserProfileChangeRequest.Builder().setDisplayName(nomeFinal).build()
                    auth.currentUser!!.updateProfile(req).addOnCompleteListener { profileTask ->
                        if (!profileTask.isSuccessful) {
                            Toast.makeText(this, "Conta criada, mas não foi possível atualizar o nome do perfil.", Toast.LENGTH_LONG).show()
                        }
                        val uid = auth.currentUser!!.uid
                        com.google.firebase.firestore.FirebaseFirestore.getInstance().collection("usuarios").document(uid)
                            .set(mapOf("nome" to nomeFinal, "email" to emailFinal, "perfil" to perfil))
                            .addOnSuccessListener {
                        val dados = PreferencesManager.prepararDadosPreferencias(nomeFinal, emailFinal)
                        val preferencias = getSharedPreferences("CampusHubPrefs", Context.MODE_PRIVATE)
                        preferencias.edit()
                            .putString("nome", dados["nome"])
                            .putString("email", dados["email"])
                            .apply()

                        auth.signOut()
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                        finish()
                            }
                            .addOnFailureListener { error ->
                                Toast.makeText(this, "Conta criada, mas o perfil não foi salvo: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
            }
    }
}

@Composable
fun TelaCadastro(
    registrar: (String, String, String, String) -> Unit,
    voltar: () -> Unit
) {
    TelaCadastroUI(
        registrar = registrar,
        voltar = voltar
    )
}
