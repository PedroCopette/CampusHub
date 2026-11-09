package br.com.loginfirebase.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import br.com.loginfirebase.AuthManager
import br.com.loginfirebase.CadastroValidator
import br.com.loginfirebase.TelaLoginUI
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    companion object {
        fun validarLogin(email: String, senha: String): Boolean {
            return CadastroValidator.loginCamposPreenchidos(email, senha)
        }
    }

    private lateinit var auth: FirebaseAuth

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
                        startActivity(Intent(this, RegistrationActivity::class.java))
                    },
                    recuperarSenha = {
                        startActivity(Intent(this, RecoveryActivity::class.java))
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

        auth.signInWithEmailAndPassword(email.trim(), senha.trim())
            .addOnCompleteListener(this) { task ->
                val msg = AuthManager.processarRespostaLogin(task.isSuccessful)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                if (task.isSuccessful) {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
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
    TelaLoginUI(
        entrar = entrar,
        abrirCadastro = abrirCadastro,
        recuperarSenha = recuperarSenha
    )
}
