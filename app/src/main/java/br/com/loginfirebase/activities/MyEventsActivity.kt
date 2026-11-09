package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.MyEventsUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class MyEventsActivity : ComponentActivity() {

    companion object {
        fun formatarTextoInscricoes(count: Int): String {
            return MyEventsUtils.formatarContadorInscricoes(count)
        }
        fun ehUsuarioLogado(usuarioId: String?): Boolean = !usuarioId.isNullOrEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
