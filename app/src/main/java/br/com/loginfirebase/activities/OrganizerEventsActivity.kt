package br.com.loginfirebase.activities

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private val OrganizerNavy = Color(0xFF172D59)
private val OrganizerBlue = Color(0xFF2864D7)

class OrganizerEventsActivity : ComponentActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser
    private var items by mutableStateOf<List<Pair<String, Map<String, Any>>>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user == null) { finish(); return }
        db.collection("usuarios").document(user!!.uid).get().addOnSuccessListener { profile ->
            if (profile.getString("perfil") != "organizador") {
                Toast.makeText(this, "Acesso apenas para organizadores", Toast.LENGTH_SHORT).show(); finish()
            } else carregar()
        }.addOnFailureListener { Toast.makeText(this, "Não foi possível validar seu perfil.", Toast.LENGTH_LONG).show(); finish() }

        setContent {
            MaterialTheme {
                var titulo by remember { mutableStateOf("") }
                var data by remember { mutableStateOf("") }
                var horario by remember { mutableStateOf("") }
                var endereco by remember { mutableStateOf("") }
                var descricao by remember { mutableStateOf("") }
                var categoria by remember { mutableStateOf("Acadêmico") }
                var capacidade by remember { mutableStateOf("") }
                var imagemUrl by remember { mutableStateOf("") }
                var editId by remember { mutableStateOf<String?>(null) }
                var salvando by remember { mutableStateOf(false) }

                Column(Modifier.fillMaxSize().background(Color(0xFFF3F7FD)).statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(20.dp)) {
                    Text("Gerenciar eventos", style = MaterialTheme.typography.headlineMedium, color = OrganizerNavy)
                    Text("Crie, publique e atualize seus eventos", color = Color(0xFF657694))
                    Spacer(Modifier.height(18.dp))
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(if (editId == null) "Novo evento" else "Editar evento", style = MaterialTheme.typography.titleLarge, color = OrganizerNavy)
                            Spacer(Modifier.height(8.dp))
                            CampoEvento("Título", titulo, { titulo = it })
                            CampoEvento("Descrição", descricao, { descricao = it }, singleLine = false)
                            CampoEvento("Categoria", categoria, { categoria = it })
                            CampoEvento("Data (dd/MM/yyyy)", data, { data = it })
                            CampoEvento("Horário", horario, { horario = it })
                            CampoEvento("Endereço", endereco, { endereco = it })
                            CampoEvento("Capacidade máxima", capacidade, { capacidade = it }, keyboardType = KeyboardType.Number)
                            CampoEvento("URL da imagem (https://...)", imagemUrl, { imagemUrl = it }, keyboardType = KeyboardType.Uri)
                            Spacer(Modifier.height(10.dp))
                            Button(onClick = {
                                val uid = user?.uid ?: return@Button
                                val cap = capacidade.toIntOrNull()
                                if (titulo.isBlank() || descricao.isBlank() || categoria.isBlank() || data.isBlank() || horario.isBlank() || endereco.isBlank() || cap == null || cap <= 0) {
                                    Toast.makeText(this@OrganizerEventsActivity, "Preencha os campos e informe uma capacidade válida.", Toast.LENGTH_LONG).show(); return@Button
                                }
                                if (imagemUrl.isNotBlank() && !Patterns.WEB_URL.matcher(imagemUrl).matches()) {
                                    Toast.makeText(this@OrganizerEventsActivity, "Informe uma URL válida para a imagem.", Toast.LENGTH_SHORT).show(); return@Button
                                }
                                salvando = true
                                val id = editId ?: db.collection("eventos").document().id
                                val anterior = items.firstOrNull { it.first == id }?.second
                                val dados = hashMapOf<String, Any>(
                                    "nomeEvento" to titulo.trim(), "dataEvento" to data.trim(), "horarioEvento" to horario.trim(),
                                    "localEvento" to endereco.trim(), "enderecoEvento" to endereco.trim(), "descricaoEvento" to descricao.trim(),
                                    "categoria" to categoria.trim(), "capacidadeMaxima" to cap, "imagemUrl" to imagemUrl.trim(),
                                    "organizadorId" to uid, "publicado" to (anterior?.get("publicado") as? Boolean ?: false),
                                    "encerrado" to (anterior?.get("encerrado") as? Boolean ?: false)
                                )
                                db.collection("eventos").document(id).set(dados).addOnSuccessListener {
                                    salvando = false; titulo="";data="";horario="";endereco="";descricao="";categoria="Acadêmico";capacidade="";imagemUrl="";editId=null;carregar()
                                    Toast.makeText(this@OrganizerEventsActivity, "Evento salvo como rascunho.", Toast.LENGTH_SHORT).show()
                                }.addOnFailureListener { salvando = false; Toast.makeText(this@OrganizerEventsActivity, "Falha ao salvar. Confira as regras do Firestore.", Toast.LENGTH_LONG).show() }
                            }, modifier = Modifier.fillMaxWidth(), enabled = !salvando) { Text(if (salvando) "Salvando..." else if (editId == null) "Salvar rascunho" else "Salvar alterações") }
                            if (editId != null) TextButton(onClick = { editId=null;titulo="";data="";horario="";endereco="";descricao="";categoria="Acadêmico";capacidade="";imagemUrl="" }, modifier = Modifier.fillMaxWidth()) { Text("Cancelar edição") }
                        }
                    }
                    Spacer(Modifier.height(22.dp)); Text("Seus eventos", style = MaterialTheme.typography.titleLarge, color = OrganizerNavy)
                    if (items.isEmpty()) Text("Você ainda não criou eventos.", color = Color(0xFF657694), modifier = Modifier.padding(vertical = 12.dp))
                    items.forEach { (id, d) ->
                        val publicado = d["publicado"] as? Boolean ?: false
                        val encerrado = d["encerrado"] as? Boolean ?: false
                        Card(Modifier.fillMaxWidth().padding(top = 10.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(Modifier.padding(16.dp)) {
                                Text(d["nomeEvento"]?.toString() ?: "Evento", style = MaterialTheme.typography.titleMedium, color = OrganizerNavy)
                                Text("${d["categoria"] ?: ""} · ${d["dataEvento"] ?: ""} · ${d["capacidadeMaxima"] ?: "—"} vagas", color = Color(0xFF657694))
                                Text(when { encerrado -> "Encerrado"; publicado -> "Publicado"; else -> "Rascunho" }, color = if (publicado && !encerrado) OrganizerBlue else Color(0xFF657694))
                                Row {
                                    TextButton(onClick = {
                                        editId=id;titulo=d["nomeEvento"]?.toString().orEmpty();data=d["dataEvento"]?.toString().orEmpty();horario=d["horarioEvento"]?.toString().orEmpty();endereco=d["enderecoEvento"]?.toString() ?: d["localEvento"]?.toString().orEmpty();descricao=d["descricaoEvento"]?.toString().orEmpty();categoria=d["categoria"]?.toString().orEmpty();capacidade=d["capacidadeMaxima"]?.toString().orEmpty();imagemUrl=d["imagemUrl"]?.toString().orEmpty()
                                    }) { Text("Editar") }
                                    if (!encerrado) TextButton(onClick = { atualizarEstado(id, "publicado", !publicado) }) { Text(if (publicado) "Retirar publicação" else "Publicar") }
                                    TextButton(onClick = { atualizarEstado(id, "encerrado", !encerrado) }) { Text(if (encerrado) "Reabrir" else "Encerrar") }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp)); OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth()) { Text("Voltar") }
                }
            }
        }
    }

    @Composable private fun CampoEvento(label: String, value: String, onValueChange: (String) -> Unit, singleLine: Boolean = true, keyboardType: KeyboardType = KeyboardType.Text) {
        OutlinedTextField(value, onValueChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), singleLine = singleLine, minLines = if (singleLine) 1 else 3, keyboardOptions = KeyboardOptions(keyboardType = keyboardType), shape = RoundedCornerShape(14.dp))
    }

    private fun atualizarEstado(id: String, campo: String, valor: Boolean) {
        db.collection("eventos").document(id).update(campo, valor)
            .addOnSuccessListener { carregar() }
            .addOnFailureListener { Toast.makeText(this, "Não foi possível atualizar o evento.", Toast.LENGTH_SHORT).show() }
    }

    private fun carregar() {
        db.collection("eventos").whereEqualTo("organizadorId", user?.uid).get()
            .addOnSuccessListener { resultado -> items = resultado.documents.map { it.id to (it.data ?: emptyMap()) } }
            .addOnFailureListener { Toast.makeText(this, "Falha ao carregar seus eventos.", Toast.LENGTH_SHORT).show() }
    }
}
