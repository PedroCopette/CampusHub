package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.ProfileUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import br.com.loginfirebase.TelaEditarPerfilUI
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

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
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser
                TelaEditarPerfilUI(
                    nomeInicial = user?.displayName.orEmpty(),
                    email = user?.email.orEmpty(),
                    voltar = { finish() },
                    salvar = { nome ->
                        if (!validarEdicao(nome)) Toast.makeText(this, "Informe um nome válido.", Toast.LENGTH_SHORT).show()
                        else user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(nome.trim()).build())
                            ?.addOnSuccessListener {
                                FirebaseFirestore.getInstance().collection("usuarios").document(user.uid).update("nome", nome.trim())
                                    .addOnSuccessListener { Toast.makeText(this, "Perfil atualizado.", Toast.LENGTH_SHORT).show(); finish() }
                                    .addOnFailureListener { Toast.makeText(this, "Não foi possível salvar o nome no banco.", Toast.LENGTH_LONG).show() }
                            }?.addOnFailureListener { Toast.makeText(this, "Não foi possível atualizar o perfil.", Toast.LENGTH_SHORT).show() }
                    }
                )
            }
        }
    }
}
