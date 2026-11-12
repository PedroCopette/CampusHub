package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.FavoritesUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.com.loginfirebase.FavoritoEvento
import br.com.loginfirebase.FavoritesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesActivity : ComponentActivity() {
    private var favoritos by mutableStateOf<List<Pair<String, FavoritoEvento>>>(emptyList())
    private var carregando by mutableStateOf(true)

    companion object {
        fun formatarTextoLista(count: Int): String {
            return FavoritesUtils.formatarContadorFavoritos(count)
        }
        fun gerarIdFavorito(usuarioId: String, eventoId: String): String = "${usuarioId}_$eventoId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = FirebaseFirestore.getInstance()

        setContent {
            LoginFirebaseTheme {
                Column(Modifier.fillMaxSize().background(Color(0xFFF3F7FD)).statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(20.dp)) {
                    Text("Meus Favoritos", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF172D59))
                    Spacer(Modifier.height(8.dp)); Text(formatarTextoLista(favoritos.size), color = Color(0xFF657694)); Spacer(Modifier.height(16.dp))
                    if (carregando) CircularProgressIndicator()
                    else if (favoritos.isEmpty()) Text("Você ainda não salvou nenhum evento.", color = Color(0xFF657694))
                    else favoritos.forEach { (docId, evento) -> Card(Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) { Column(Modifier.padding(16.dp)) { Text(evento.nome, style = MaterialTheme.typography.titleLarge, color = Color(0xFF172D59)); Text("${evento.data} · ${evento.horario}"); Text(evento.local); TextButton(onClick = { db.collection("favoritos").document(docId).delete().addOnSuccessListener { carregar() }.addOnFailureListener { Toast.makeText(this@FavoritesActivity, "Falha ao remover favorito.", Toast.LENGTH_SHORT).show() } }) { Text("Remover") } } } }
                    Spacer(Modifier.height(12.dp)); OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
        carregar()
    }

    private fun carregar() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) { carregando = false; return }
        carregando = true
        FirebaseFirestore.getInstance().collection("favoritos").whereEqualTo("usuarioId", uid).get()
            .addOnSuccessListener { docs -> favoritos = docs.documents.mapNotNull { doc -> doc.data?.let { FavoritesManager.mapearDocumentoParaFavorito(it)?.let { favorito -> doc.id to favorito } } }; carregando = false }
            .addOnFailureListener { carregando = false; Toast.makeText(this, "Falha ao carregar favoritos.", Toast.LENGTH_LONG).show() }
    }
}
