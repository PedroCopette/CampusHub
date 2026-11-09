package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.MediaUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class EventAudioActivity : ComponentActivity() {

    companion object {
        fun sanitizarId(id: String) = MediaUtils.sanitizarIdEvento(id)
        fun validarStatusGravacao(gravando: Boolean): String = if (gravando) "Gravando" else "Parado"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
