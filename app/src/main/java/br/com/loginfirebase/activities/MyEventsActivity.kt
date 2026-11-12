package br.com.loginfirebase.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import br.com.loginfirebase.Evento
import br.com.loginfirebase.MyEventsManager
import br.com.loginfirebase.MyEventsUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyEventsActivity : ComponentActivity() {
    companion object {
        fun formatarTextoInscricoes(count: Int): String = MyEventsUtils.formatarContadorInscricoes(count)
        fun ehUsuarioLogado(usuarioId: String?): Boolean = !usuarioId.isNullOrEmpty()
    }

    private var eventos by mutableStateOf<List<Evento>>(emptyList())
    private var carregando by mutableStateOf(true)
    private var erro by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginFirebaseTheme {
                Column(
                    Modifier.fillMaxSize().background(Color(0xFFF3F7FD)).statusBarsPadding()
                        .navigationBarsPadding().verticalScroll(rememberScrollState()).padding(20.dp)
                ) {
                    Text("Meus Eventos", style = MaterialTheme.typography.headlineMedium, color = Color(0xFF172D59))
                    Spacer(Modifier.height(6.dp))
                    Text(formatarTextoInscricoes(eventos.size), color = Color(0xFF657694))
                    Spacer(Modifier.height(20.dp))
                    when {
                        carregando -> CircularProgressIndicator(color = Color(0xFF2864D7))
                        erro -> Text("Não foi possível carregar suas inscrições. Verifique a conexão e tente novamente.", color = Color(0xFF9D2430))
                        eventos.isEmpty() -> Text("Você ainda não está inscrito em nenhum evento.", color = Color(0xFF657694))
                        else -> eventos.forEach { evento ->
                            Card(Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                Column(Modifier.padding(18.dp)) {
                                    Text(evento.nome, style = MaterialTheme.typography.titleLarge, color = Color(0xFF172D59))
                                    Spacer(Modifier.height(8.dp))
                                    Text("${evento.data} · ${evento.horario}", color = Color(0xFF2864D7))
                                    Text(evento.local, color = Color(0xFF657694))
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = { carregarInscricoes() }, modifier = Modifier.fillMaxWidth(), enabled = !carregando) { Text("Atualizar") }
                    Button(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
        carregarInscricoes()
    }

    private fun carregarInscricoes() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            carregando = false; erro = true
            Toast.makeText(this, "Faça login para consultar suas inscrições.", Toast.LENGTH_SHORT).show()
            return
        }
        carregando = true; erro = false
        FirebaseFirestore.getInstance().collection("inscricoes").whereEqualTo("usuarioId", uid).get()
            .addOnSuccessListener { resultado ->
                eventos = MyEventsManager.mapearListaInscricoes(resultado.documents.mapNotNull { it.data })
                carregando = false
            }
            .addOnFailureListener {
                carregando = false; erro = true
                Toast.makeText(this, "Erro ao buscar inscrições no Firestore.", Toast.LENGTH_SHORT).show()
            }
    }
}
