package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.ProfileUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class ProfileActivity : ComponentActivity() {

    companion object {
        fun formatarNome(nome: String?, email: String?): String {
            return ProfileUtils.formatarNomeExibicao(nome, email)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
