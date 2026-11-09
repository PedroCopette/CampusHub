package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.MediaUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class EventPhotosActivity : ComponentActivity() {

    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
        fun ehAutorDaMidia(usuarioId: String?, autorId: String): Boolean = usuarioId != null && usuarioId == autorId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
