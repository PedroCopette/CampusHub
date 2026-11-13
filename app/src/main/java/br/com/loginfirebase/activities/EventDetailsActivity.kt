package br.com.loginfirebase.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.loginfirebase.EventDetailsUtils
import br.com.loginfirebase.MyEventsManager
import br.com.loginfirebase.ui.theme.LoginFirebaseTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EventDetailsActivity : ComponentActivity() {
    companion object {
        fun formatarAvisoAvaliacao(media: Double, total: Int) = EventDetailsUtils.formatarMediaTexto(media, total)
        fun validarTextoComentario(texto: String) = texto.trim().isNotEmpty()
        fun ehAutorDoComentario(usuarioId: String?, autorId: String) = usuarioId != null && usuarioId == autorId
    }

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val eventId by lazy { intent.getStringExtra("idEvento").orEmpty() }
    private var inscrito by mutableStateOf(false)
    private var aguardando by mutableStateOf(false)
    private var posicaoFila by mutableStateOf<Int?>(null)
    private var primeiroDaFila by mutableStateOf(false)
    private var vagas by mutableStateOf(0)
    private var capacidade by mutableStateOf(0)
    private var perfil by mutableStateOf("aluno")
    private var carregando by mutableStateOf(true)
    private var operando by mutableStateOf(false)
    private var organizador by mutableStateOf(false)
    private var carregandoInscritos by mutableStateOf(false)
    private data class InscritoPainel(val id: String, val nome: String, val checkin: Boolean)
    private var inscritos by mutableStateOf<List<InscritoPainel>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carregarEstado()
        setContent {
            LoginFirebaseTheme {
                Column(Modifier.fillMaxSize().padding(24.dp)) {
                    Text(intent.getStringExtra("nomeEvento") ?: "Detalhes do evento", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(10.dp))
                    Text("${intent.getStringExtra("dataEvento") ?: ""} · ${intent.getStringExtra("horarioEvento") ?: ""}")
                    Text(intent.getStringExtra("localEvento") ?: "")
                    Spacer(Modifier.height(12.dp)); Text(intent.getStringExtra("descricaoEvento") ?: "")
                    Spacer(Modifier.height(18.dp))
                    Text(if (capacidade > 0) "Vagas: $vagas de $capacidade" else "Vagas limitadas", style = MaterialTheme.typography.titleMedium)
                    if (organizador) {
                        Text("Acompanhamento · ${inscritos.size} inscritos", style = MaterialTheme.typography.titleLarge)
                        Text("Check-in realizado: ${inscritos.count { it.checkin }}")
                        if (carregandoInscritos) CircularProgressIndicator()
                        else LazyColumn(Modifier.weight(1f, fill = false)) {
                            items(inscritos) { inscrito ->
                                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Column(Modifier.weight(1f).padding(end = 8.dp)) {
                                        Text(inscrito.nome, style = MaterialTheme.typography.bodyMedium)
                                        Text(if (inscrito.checkin) "Check-in realizado" else "Pendente", style = MaterialTheme.typography.bodySmall)
                                    }
                                    OutlinedButton(onClick = { atualizarCheckin(inscrito) }, enabled = !operando) {
                                        Text(if (inscrito.checkin) "Desfazer" else "Marcar presença")
                                    }
                                }
                            }
                        }
                    }
                    if (carregando) CircularProgressIndicator()
                    else if (perfil == "aluno") {
                        when {
                            inscrito -> Button(onClick = { cancelarInscricao() }, modifier = Modifier.fillMaxWidth(), enabled = !operando) { Text(if (operando) "Aguarde..." else "Cancelar inscrição") }
                            aguardando -> {
                                Text("Na lista de espera${posicaoFila?.let { " · posição $it" } ?: ""}")
                                Button(onClick = { confirmarVaga() }, modifier = Modifier.fillMaxWidth(), enabled = primeiroDaFila && vagas < capacidade && !operando) { Text(if (operando) "Aguarde..." else if (primeiroDaFila && vagas < capacidade) "Confirmar vaga" else "Aguarde sua vez") }
                                OutlinedButton(onClick = { cancelarEspera() }, modifier = Modifier.fillMaxWidth(), enabled = !operando) { Text("Sair da lista de espera") }
                            }
                            else -> Button(onClick = { inscreverOuEntrarNaFila() }, modifier = Modifier.fillMaxWidth(), enabled = !operando) { Text(if (operando) "Aguarde..." else if (vagas >= capacidade && capacidade > 0) "Entrar na lista de espera" else "Inscrever-se") }
                        }
                    }
                    Spacer(Modifier.height(12.dp)); OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
    }

    private fun carregarEstado() {
        val user = auth.currentUser ?: run { carregando = false; return }
        if (eventId.isBlank()) { carregando = false; return }
        val eventRef = db.collection("eventos").document(eventId)
        val signupRef = db.collection("inscricoes").document("${user.uid}_$eventId")
        db.collection("usuarios").document(user.uid).get().addOnSuccessListener { perfil = it.getString("perfil") ?: "aluno" }
        db.runTransaction { tx ->
            val event = tx.get(eventRef); val signup = tx.get(signupRef)
            val queue = event.get("filaEspera") as? Map<*, *> ?: emptyMap<Any, Any>()
            val pos = (queue[user.uid] as? Number)?.toInt()
            val first = queue.entries.minByOrNull { (it.value as? Number)?.toLong() ?: Long.MAX_VALUE }?.key == user.uid
            Triple(signup.exists(), pos, Pair(Pair(event.intValue("vagasOcupadas") ?: 0, event.intValue("capacidadeMaxima") ?: 0), first))
        }.addOnSuccessListener { state ->
            inscrito = state.first; aguardando = state.second != null; posicaoFila = state.second
            vagas = state.third.first.first; capacidade = state.third.first.second; carregando = false
            primeiroDaFila = aguardando && state.third.second
            eventRef.get().addOnSuccessListener { event ->
                organizador = event.getString("organizadorId") == user.uid
                if (organizador) carregarInscritos()
            }
        }.addOnFailureListener { carregando = false; Toast.makeText(this, "Falha ao consultar vagas.", Toast.LENGTH_SHORT).show() }
    }

    private fun carregarInscritos() {
        carregandoInscritos = true
        db.collection("inscricoes").whereEqualTo("idEvento", eventId).get()
            .addOnSuccessListener { docs ->
                inscritos = docs.documents.map { doc ->
                    val nome = doc.getString("usuarioNome") ?: doc.getString("nomeUsuario") ?: doc.getString("usuarioEmail") ?: doc.getString("usuarioId") ?: "Aluno"
                    InscritoPainel(doc.id, nome, doc.getBoolean("checkin") ?: doc.getBoolean("checkIn") ?: doc.getBoolean("presente") ?: false)
                }.sortedBy { it.nome.lowercase() }
                carregandoInscritos = false
            }
            .addOnFailureListener { carregandoInscritos = false; Toast.makeText(this, "Falha ao carregar inscritos.", Toast.LENGTH_SHORT).show() }
    }

    private fun atualizarCheckin(inscrito: InscritoPainel) {
        operando = true
        db.collection("inscricoes").document(inscrito.id).update("checkin", !inscrito.checkin)
            .addOnSuccessListener {
                operando = false
                carregarInscritos()
            }
            .addOnFailureListener { erro ->
                operando = false
                Toast.makeText(this, "Falha ao atualizar presença: ${erro.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun inscreverOuEntrarNaFila() {
        val user = auth.currentUser ?: return
        operando = true
        val eventRef = db.collection("eventos").document(eventId); val signupRef = db.collection("inscricoes").document("${user.uid}_$eventId")
        db.runTransaction { tx ->
            val event = tx.get(eventRef); val signup = tx.get(signupRef)
            val queue = (event.get("filaEspera") as? Map<*, *>)?.entries?.associate { it.key.toString() to (it.value as Number).toLong() }?.toMutableMap() ?: mutableMapOf()
            check(!signup.exists() && user.uid !in queue) { "Você já está inscrito ou na lista de espera." }
            val cap = event.intValue("capacidadeMaxima") ?: 0; val count = event.intValue("vagasOcupadas") ?: 0
            check(!event.getBoolean("encerrado").orFalse()) { "Este evento foi encerrado." }
            check(cap > 0) { "Este evento não possui capacidade válida para inscrições." }
            if (count < cap && queue.isEmpty()) {
                tx.set(signupRef, MyEventsManager.criarDadosInscricao(eventId, intent.getStringExtra("nomeEvento").orEmpty(), intent.getStringExtra("dataEvento").orEmpty(), intent.getStringExtra("horarioEvento").orEmpty(), intent.getStringExtra("localEvento").orEmpty(), user.uid, user.email) + mapOf("status" to "confirmada"))
                tx.update(eventRef, "vagasOcupadas", count + 1); "INSCRITO"
            } else {
                val pos = event.getLong("proximaPosicaoLista") ?: 1L
                queue[user.uid] = pos
                tx.update(eventRef, mapOf("filaEspera" to queue, "proximaPosicaoLista" to pos + 1)); "FILA"
            }
        }.addOnSuccessListener { result -> operando=false; Toast.makeText(this, if(result=="FILA") "Evento lotado. Você entrou na lista de espera." else "Inscrição confirmada!", Toast.LENGTH_LONG).show(); carregarEstado() }
            .addOnFailureListener { erro ->
                Log.e("EventRegistration", "Falha ao inscrever no evento $eventId", erro)
                operando = false
                Toast.makeText(this, "Falha ao inscrever: ${erro.message ?: erro.javaClass.simpleName}", Toast.LENGTH_LONG).show()
            }
    }

    private fun cancelarInscricao() {
        val user = auth.currentUser ?: return; operando=true
        val e=db.collection("eventos").document(eventId); val s=db.collection("inscricoes").document("${user.uid}_$eventId")
        db.runTransaction { tx ->
            val event=tx.get(e); val signup=tx.get(s)
            check(signup.exists()) { "Inscrição não encontrada." }
            val queue=(event.get("filaEspera") as? Map<*,*>)?.entries?.associate { it.key.toString() to ((it.value as? Number)?.toLong() ?: Long.MAX_VALUE) }?.toMutableMap() ?: mutableMapOf()
            val count=((event.intValue("vagasOcupadas") ?: 1)-1).coerceAtLeast(0)
            tx.delete(s)
            tx.update(e,mapOf("vagasOcupadas" to count,"filaEspera" to queue))
            true
        }
            .addOnSuccessListener { operando=false; Toast.makeText(this,"Inscrição cancelada. A vaga está disponível para a fila.",Toast.LENGTH_LONG).show(); carregarEstado() }
            .addOnFailureListener { erro ->
                Log.e("EventRegistration", "Falha ao cancelar inscrição no evento $eventId", erro)
                operando = false
                val detalhe = erro.message ?: erro.javaClass.simpleName
                Toast.makeText(this, "Falha ao cancelar: $detalhe", Toast.LENGTH_LONG).show()
            }
    }

    private fun confirmarVaga() {
        val user=auth.currentUser ?: return; operando=true
        val e=db.collection("eventos").document(eventId); val s=db.collection("inscricoes").document("${user.uid}_$eventId")
        db.runTransaction { tx ->
            val event=tx.get(e); val signup=tx.get(s)
            val queue=(event.get("filaEspera") as? Map<*,*>)?.entries?.associate { it.key.toString() to (it.value as Number).toLong() }?.toMutableMap() ?: mutableMapOf()
            check(user.uid in queue && !signup.exists()) { "Sua vaga na fila não está disponível." }
            val first=queue.minByOrNull { it.value }
            check(first?.key==user.uid) { "Ainda há alunos antes de você na fila." }
            val count=(event.getLong("vagasOcupadas")?:0L).toInt(); val cap=(event.getLong("capacidadeMaxima")?:0L).toInt()
            check(count<cap) { "Ainda não há vaga disponível." }
            tx.set(s,MyEventsManager.criarDadosInscricao(eventId,intent.getStringExtra("nomeEvento").orEmpty(),intent.getStringExtra("dataEvento").orEmpty(),intent.getStringExtra("horarioEvento").orEmpty(),intent.getStringExtra("localEvento").orEmpty(),user.uid,user.email)+mapOf("status" to "confirmada"))
            queue.remove(user.uid); tx.update(e,mapOf("vagasOcupadas" to count+1,"filaEspera" to queue)); true
        }.addOnSuccessListener { operando=false; Toast.makeText(this,"Vaga confirmada!",Toast.LENGTH_LONG).show(); carregarEstado() }
            .addOnFailureListener { erro ->
                Log.e("EventRegistration", "Falha ao confirmar vaga no evento $eventId", erro)
                operando = false
                Toast.makeText(this, "Não foi possível confirmar: ${erro.message ?: erro.javaClass.simpleName}", Toast.LENGTH_LONG).show()
                carregarEstado()
            }
    }

    private fun cancelarEspera() {
        val user=auth.currentUser ?: return; operando=true
        val eventRef=db.collection("eventos").document(eventId)
        db.runTransaction { tx -> val event=tx.get(eventRef); val queue=(event.get("filaEspera") as? Map<*,*>)?.entries?.associate { it.key.toString() to (it.value as Number).toLong() }?.toMutableMap() ?: mutableMapOf(); queue.remove(user.uid); tx.update(eventRef,"filaEspera",queue); true }
            .addOnSuccessListener { operando=false; Toast.makeText(this,"Você saiu da lista de espera.",Toast.LENGTH_SHORT).show(); carregarEstado() }
            .addOnFailureListener { operando=false; Toast.makeText(this,"Falha ao sair da lista.",Toast.LENGTH_SHORT).show() }
    }
    private fun Boolean?.orFalse() = this ?: false
    private fun com.google.firebase.firestore.DocumentSnapshot.intValue(field: String): Int? =
        when (val value = get(field)) {
            is Number -> value.toInt()
            is String -> value.toIntOrNull()
            else -> null
        }
}
