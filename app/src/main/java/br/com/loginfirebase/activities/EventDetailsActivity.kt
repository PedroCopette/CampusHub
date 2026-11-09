package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.EventDetailsUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class EventDetailsActivity : ComponentActivity() {

    companion object {
        fun formatarAvisoAvaliacao(media: Double, total: Int): String {
            return EventDetailsUtils.formatarMediaTexto(media, total)
        }
        fun validarTextoComentario(texto: String): Boolean = texto.trim().isNotEmpty()
        fun ehAutorDoComentario(usuarioId: String?, autorId: String): Boolean = usuarioId != null && usuarioId == autorId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
