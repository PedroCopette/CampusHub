package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.MediaUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class EventVideoActivity : ComponentActivity() {

    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
        fun validarStatusVideo(tocando: Boolean): String = if (tocando) "Reproduzindo" else "Pausado"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
