package br.com.loginfirebase.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import br.com.loginfirebase.EventDetailsUtils
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import br.com.loginfirebase.MyEventsManager
import android.widget.Toast

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
        val auth=FirebaseAuth.getInstance(); val db=FirebaseFirestore.getInstance(); val eventId=intent.getStringExtra("idEvento").orEmpty()
        var inscrito by mutableStateOf(false); var perfil by mutableStateOf("aluno"); var enviando by mutableStateOf(false)
        auth.currentUser?.let { u -> db.collection("inscricoes").document("${u.uid}_$eventId").get().addOnSuccessListener{inscrito=it.exists()}; db.collection("usuarios").document(u.uid).get().addOnSuccessListener{perfil=it.getString("perfil")?:"aluno"} }

        setContent {
            LoginFirebaseTheme {
                Column(Modifier.fillMaxSize().padding(24.dp)) {
                    Text(intent.getStringExtra("nomeEvento") ?: "Detalhes do evento", style=MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(10.dp)); Text("${intent.getStringExtra("dataEvento") ?: ""} · ${intent.getStringExtra("horarioEvento") ?: ""}")
                    Text(intent.getStringExtra("localEvento") ?: ""); Spacer(Modifier.height(12.dp)); Text(intent.getStringExtra("descricaoEvento") ?: "")
                    Spacer(Modifier.height(18.dp))
                    if(perfil=="aluno") Button(onClick={ val u=auth.currentUser ?: return@Button; if(eventId.isBlank()) return@Button; enviando=true; val ref=db.collection("inscricoes").document("${u.uid}_$eventId"); if(inscrito) ref.delete().addOnSuccessListener{inscrito=false;enviando=false}.addOnFailureListener{enviando=false;Toast.makeText(this@EventDetailsActivity,"Falha ao cancelar inscrição",Toast.LENGTH_SHORT).show()} else ref.set(MyEventsManager.criarDadosInscricao(eventId,intent.getStringExtra("nomeEvento").orEmpty(),intent.getStringExtra("dataEvento").orEmpty(),intent.getStringExtra("horarioEvento").orEmpty(),intent.getStringExtra("localEvento").orEmpty(),u.uid,u.email)).addOnSuccessListener{inscrito=true;enviando=false;Toast.makeText(this@EventDetailsActivity,"Inscrição realizada!",Toast.LENGTH_SHORT).show()}.addOnFailureListener{enviando=false;Toast.makeText(this@EventDetailsActivity,"Falha ao se inscrever; confira regras Firestore",Toast.LENGTH_LONG).show()} },modifier=Modifier.fillMaxWidth(),enabled=!enviando){Text(if(enviando)"Aguarde..." else if(inscrito)"Cancelar inscrição" else "Inscrever-se")}
                    Spacer(Modifier.height(12.dp)); OutlinedButton(onClick={finish()},modifier=Modifier.fillMaxWidth()){Text("Voltar")}
                }
            }
        }
    }
}
