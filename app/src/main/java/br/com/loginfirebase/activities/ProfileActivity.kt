package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.ProfileUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import br.com.loginfirebase.TelaPerfilCampusUI
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

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
                val user = FirebaseAuth.getInstance().currentUser
                TelaPerfilCampusUI(
                    nome = ProfileUtils.formatarNomeExibicao(user?.displayName, user?.email),
                    email = user?.email ?: "",
                    editar = { startActivity(Intent(this, EditProfileActivity::class.java)) },
                    voltar = { finish() }
                )
            }
        }
    }
}
