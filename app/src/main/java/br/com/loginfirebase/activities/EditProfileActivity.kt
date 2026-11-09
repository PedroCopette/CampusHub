package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.ProfileUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class EditProfileActivity : ComponentActivity() {

    companion object {
        fun validarEdicao(nome: String): Boolean {
            return ProfileUtils.validarEdicaoNome(nome)
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
