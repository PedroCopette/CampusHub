package br.com.loginfirebase.activities

import android.os.Bundle
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
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
private val OrganizerPaleBlue = Color(0xFFEAF2FF)

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
            MaterialTheme(colorScheme = lightColorScheme(
                primary = OrganizerBlue,
                onPrimary = Color.White,
                secondary = OrganizerNavy,
                background = Color(0xFFF4F8FF),
                surface = Color.White,
                onSurface = OrganizerNavy,
                outline = Color(0xFFB9CBE8)
            )) {
                var titulo by remember { mutableStateOf("") }
                var data by remember { mutableStateOf("") }
                var horario by remember { mutableStateOf("") }
                var endereco by remember { mutableStateOf("") }
                var descricao by remember { mutableStateOf("") }
                var categoria by remember { mutableStateOf("Acadêmico") }
                var capacidade by remember { mutableStateOf("") }
                var imagemUrl by remember { mutableStateOf("") }
                var imagemSelecionada by remember { mutableStateOf<Uri?>(null) }
                var editId by remember { mutableStateOf<String?>(null) }
                var salvando by remember { mutableStateOf(false) }
                val seletorImagem = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> imagemSelecionada = uri }

                Column(Modifier.fillMaxSize().background(Color(0xFFF4F8FF)).statusBarsPadding().navigationBarsPadding().verticalScroll(rememberScrollState()).padding(18.dp)) {
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = OrganizerNavy)) {
                        Column(Modifier.padding(horizontal = 20.dp, vertical = 22.dp)) {
                            Text("PAINEL DO ORGANIZADOR", style = MaterialTheme.typography.labelMedium, color = Color(0xFFBFD5FF))
                            Spacer(Modifier.height(5.dp))
                            Text("Gerenciar eventos", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                            Text("Crie, publique e acompanhe seus eventos", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFE0EAFF))
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), border = BorderStroke(1.dp, Color(0xFFDCE7F8)), elevation = CardDefaults.cardElevation(3.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(Modifier.padding(16.dp)) {
                            Text(if (editId == null) "Novo evento" else "Editar evento", style = MaterialTheme.typography.titleLarge, color = OrganizerNavy)
                            Text("Preencha os dados para montar a página do evento.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF657694))
                            Spacer(Modifier.height(8.dp))
                            CampoEvento("Título", titulo, { titulo = it })
                            CampoEvento("Descrição", descricao, { descricao = it }, singleLine = false)
                            CampoEvento("Categoria", categoria, { categoria = it })
                            CampoEvento("Data (dd/MM/yyyy)", data, { data = it })
                            CampoEvento("Horário", horario, { horario = it })
                            CampoEvento("Endereço", endereco, { endereco = it })
                            CampoEvento("Capacidade máxima", capacidade, { capacidade = it }, keyboardType = KeyboardType.Number)
                            OutlinedButton(onClick = { seletorImagem.launch("image/*") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, OrganizerBlue)) { Text(if (imagemSelecionada != null) "Trocar foto selecionada" else if (imagemUrl.isNotBlank()) "Trocar foto do evento" else "Escolher foto do dispositivo") }
                            Text(if (imagemSelecionada != null) "Nova foto selecionada" else if (imagemUrl.isNotBlank()) "O evento já possui uma foto" else "Nenhuma foto selecionada", color = Color(0xFF657694), style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(10.dp))
                            Button(onClick = {
                                val uid = user?.uid ?: return@Button
                                val cap = capacidade.toIntOrNull()
                                if (titulo.isBlank() || descricao.isBlank() || categoria.isBlank() || data.isBlank() || horario.isBlank() || endereco.isBlank() || cap == null || cap <= 0) {
                                    Toast.makeText(this@OrganizerEventsActivity, "Preencha os campos e informe uma capacidade válida.", Toast.LENGTH_LONG).show(); return@Button
                                }
                                salvando = true
                                val id = editId ?: db.collection("eventos").document().id
                                val anterior = items.firstOrNull { it.first == id }?.second
                                val uriFoto = imagemSelecionada
                                if (uriFoto != null) {
                                    try {
                                        val imagemBase64 = codificarImagem(uriFoto)
                                        salvarEvento(id, uid, titulo, descricao, categoria, data, horario, endereco, cap, imagemBase64, anterior, onFinish = { salvando=false; titulo="";data="";horario="";endereco="";descricao="";categoria="Acadêmico";capacidade="";imagemUrl="";imagemSelecionada=null;editId=null;carregar() }, onError = { salvando=false; Toast.makeText(this@OrganizerEventsActivity, it, Toast.LENGTH_LONG).show() })
                                    } catch (e: Exception) {
                                        salvando = false
                                        Toast.makeText(this@OrganizerEventsActivity, "Falha ao processar a imagem: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                    }
                                }
                                else null
                                if (uriFoto == null) salvarEvento(id, uid, titulo, descricao, categoria, data, horario, endereco, cap, imagemUrl, anterior, onFinish = { salvando=false;titulo="";data="";horario="";endereco="";descricao="";categoria="Acadêmico";capacidade="";imagemUrl="";editId=null;carregar() }, onError = { salvando=false;Toast.makeText(this@OrganizerEventsActivity,it,Toast.LENGTH_LONG).show() })
                            }, modifier = Modifier.fillMaxWidth().height(50.dp), enabled = !salvando, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = OrganizerBlue)) { Text(if (salvando) "Salvando..." else if (editId == null) "Salvar rascunho" else "Salvar alterações") }
                            if (editId != null) TextButton(onClick = { editId=null;titulo="";data="";horario="";endereco="";descricao="";categoria="Acadêmico";capacidade="";imagemUrl="";imagemSelecionada=null }, modifier = Modifier.fillMaxWidth()) { Text("Cancelar edição") }
                        }
                    }
                    Spacer(Modifier.height(24.dp)); Text("Seus eventos", style = MaterialTheme.typography.titleLarge, color = OrganizerNavy)
                    Text("${items.size} evento(s) no seu painel", style = MaterialTheme.typography.bodySmall, color = Color(0xFF657694))
                    if (items.isEmpty()) Card(Modifier.fillMaxWidth().padding(top = 12.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = OrganizerPaleBlue)) { Text("Você ainda não criou eventos.", color = OrganizerNavy, modifier = Modifier.padding(18.dp)) }
                    items.forEach { (id, d) ->
                        val publicado = d["publicado"] as? Boolean ?: false
                        val encerrado = d["encerrado"] as? Boolean ?: false
                        Card(Modifier.fillMaxWidth().padding(top = 10.dp), shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, Color(0xFFDCE7F8)), elevation = CardDefaults.cardElevation(2.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                            Column(Modifier.padding(16.dp)) {
                                Text(d["nomeEvento"]?.toString() ?: "Evento", style = MaterialTheme.typography.titleMedium, color = OrganizerNavy)
                                Text("${d["categoria"] ?: ""} · ${d["dataEvento"] ?: ""} · ${d["capacidadeMaxima"] ?: "—"} vagas", color = Color(0xFF657694))
                                Surface(shape = RoundedCornerShape(50), color = if (publicado && !encerrado) OrganizerPaleBlue else Color(0xFFF0F3F8), modifier = Modifier.padding(top = 8.dp)) {
                                    Text(when { encerrado -> "Encerrado"; publicado -> "Publicado"; else -> "Rascunho" }, color = if (publicado && !encerrado) OrganizerBlue else Color(0xFF657694), modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium)
                                }
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
                    Spacer(Modifier.height(16.dp)); OutlinedButton(onClick = { finish() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, OrganizerBlue)) { Text("Voltar") }
                }
            }
        }
    }

    private fun codificarImagem(uri: Uri): String {
        val original = contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
            ?: throw IllegalArgumentException("Não foi possível abrir a imagem selecionada.")
        val maiorLado = 1024
        val escala = minOf(1f, maiorLado.toFloat() / maxOf(original.width, original.height))
        val bitmap = if (escala < 1f) Bitmap.createScaledBitmap(original, (original.width * escala).toInt(), (original.height * escala).toInt(), true) else original
        var qualidade = 82
        var bytes: ByteArray
        do {
            val output = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, qualidade, output)
            bytes = output.toByteArray()
            qualidade -= 8
        } while (bytes.size > 650_000 && qualidade >= 34)
        if (bytes.size > 700_000) throw IllegalArgumentException("A imagem ainda ficou grande demais. Escolha uma imagem menor.")
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    @Composable private fun CampoEvento(label: String, value: String, onValueChange: (String) -> Unit, singleLine: Boolean = true, keyboardType: KeyboardType = KeyboardType.Text) {
        OutlinedTextField(value, onValueChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), singleLine = singleLine, minLines = if (singleLine) 1 else 3, keyboardOptions = KeyboardOptions(keyboardType = keyboardType), shape = RoundedCornerShape(14.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = OrganizerBlue, focusedLabelColor = OrganizerBlue, cursorColor = OrganizerBlue))
    }

    private fun atualizarEstado(id: String, campo: String, valor: Boolean) {
        db.collection("eventos").document(id).update(campo, valor)
            .addOnSuccessListener { carregar() }
            .addOnFailureListener { Toast.makeText(this, "Não foi possível atualizar o evento.", Toast.LENGTH_SHORT).show() }
    }

    private fun salvarEvento(
        id: String, uid: String, titulo: String, descricao: String, categoria: String,
        data: String, horario: String, endereco: String, capacidade: Int, imagemUrl: String,
        anterior: Map<String, Any>?, onFinish: () -> Unit, onError: (String) -> Unit
    ) {
        val dados = hashMapOf<String, Any>(
            "nomeEvento" to titulo.trim(), "dataEvento" to data.trim(), "horarioEvento" to horario.trim(),
            "localEvento" to endereco.trim(), "enderecoEvento" to endereco.trim(), "descricaoEvento" to descricao.trim(),
            "categoria" to categoria.trim(), "capacidadeMaxima" to capacidade, "imagemUrl" to imagemUrl,
            "organizadorId" to uid,
            "publicado" to (anterior?.get("publicado") as? Boolean ?: false),
            "encerrado" to (anterior?.get("encerrado") as? Boolean ?: false),
            "vagasOcupadas" to (anterior?.get("vagasOcupadas") as? Number ?: 0),
            "proximaPosicaoLista" to (anterior?.get("proximaPosicaoLista") as? Number ?: 1),
            "filaEspera" to (anterior?.get("filaEspera") ?: emptyMap<String, Long>())
        )
        db.collection("eventos").document(id).set(dados)
            .addOnSuccessListener { onFinish(); Toast.makeText(this, "Evento salvo como rascunho.", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { onError("Falha ao salvar evento: ${it.localizedMessage}") }
    }

    private fun carregar() {
        db.collection("eventos").whereEqualTo("organizadorId", user?.uid).get()
            .addOnSuccessListener { resultado -> items = resultado.documents.map { it.id to (it.data ?: emptyMap()) } }
            .addOnFailureListener { Toast.makeText(this, "Falha ao carregar seus eventos.", Toast.LENGTH_SHORT).show() }
    }
}
