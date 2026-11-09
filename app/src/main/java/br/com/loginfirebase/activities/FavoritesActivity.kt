package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.FavoritesUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme

class FavoritesActivity : ComponentActivity() {

    companion object {
        fun formatarTextoLista(count: Int): String {
            return FavoritesUtils.formatarContadorFavoritos(count)
        }
        fun gerarIdFavorito(usuarioId: String, eventoId: String): String = "${usuarioId}_$eventoId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LoginFirebaseTheme {
            }
        }
    }
}
