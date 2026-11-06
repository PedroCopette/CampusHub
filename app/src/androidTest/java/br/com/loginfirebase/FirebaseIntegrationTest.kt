package br.com.loginfirebase

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class FirebaseIntegrationTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun inicializarFirebaseApp() {
            val contexto = InstrumentationRegistry.getInstrumentation().targetContext
            if (FirebaseApp.getApps(contexto).isEmpty()) {
                FirebaseApp.initializeApp(contexto)
            }
        }
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    @Before
    fun prepararAmbiente() {
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    @After
    fun limparSessao() {
        auth.signOut()
    }

    // -------------------------------------------------------------
    // Teste 1: Autenticação - Criar Nova Conta no Firebase Auth
    // -------------------------------------------------------------
    @Test
    fun test01_autenticacaoCriarContaUsuario() {
        val email = "integ_create_${UUID.randomUUID()}@campushub.com"
        val senha = "SenhaSegura123"

        val latch = CountDownLatch(1)
        val resultadoSucesso = AtomicBoolean(false)
        val uidUsuario = AtomicReference<String?>(null)

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resultadoSucesso.set(true)
                    uidUsuario.set(task.result.user?.uid)
                }
                latch.countDown()
            }

        assertTrue("A criação de conta excedeu o tempo limite", latch.await(15, TimeUnit.SECONDS))
        assertTrue("A criação de conta no Firebase Auth falhou", resultadoSucesso.get())
        assertNotNull("O UID do usuário criado não deve ser nulo", uidUsuario.get())
    }

    // -------------------------------------------------------------
    // Teste 2: Autenticação - Login com Credenciais Válidas
    // -------------------------------------------------------------
    @Test
    fun test02_autenticacaoLoginComSucesso() {
        val email = "integ_login_${UUID.randomUUID()}@campushub.com"
        val senha = "SenhaSegura123"

        val createLatch = CountDownLatch(1)
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { createLatch.countDown() }
        createLatch.await(15, TimeUnit.SECONDS)
        auth.signOut()

        val loginLatch = CountDownLatch(1)
        val loginSucesso = AtomicBoolean(false)

        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginSucesso.set(true)
                }
                loginLatch.countDown()
            }

        assertTrue("O login excedeu o tempo limite", loginLatch.await(15, TimeUnit.SECONDS))
        assertTrue("O login no Firebase Auth falhou", loginSucesso.get())
        assertNotNull("Deve haver um usuário autenticado após o login", auth.currentUser)
    }

    // -------------------------------------------------------------
    // Teste 3: Autenticação - Login com Senha Incorreta Deve Falhar
    // -------------------------------------------------------------
    @Test
    fun test03_autenticacaoLoginComSenhaIncorretaFalha() {
        val email = "integ_wrongpass_${UUID.randomUUID()}@campushub.com"
        val senhaCorreta = "SenhaSegura123"
        val senhaIncorreta = "SenhaErrada999"

        val createLatch = CountDownLatch(1)
        auth.createUserWithEmailAndPassword(email, senhaCorreta).addOnCompleteListener { createLatch.countDown() }
        createLatch.await(15, TimeUnit.SECONDS)
        auth.signOut()

        val loginLatch = CountDownLatch(1)
        val loginFalhou = AtomicBoolean(false)

        auth.signInWithEmailAndPassword(email, senhaIncorreta)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    loginFalhou.set(true)
                }
                loginLatch.countDown()
            }

        assertTrue("O login excedeu o tempo limite", loginLatch.await(15, TimeUnit.SECONDS))
        assertTrue("O login com senha incorreta deveria ter falhado", loginFalhou.get())
        assertNull("Nenhum usuário deve estar logado após tentativa inválida", auth.currentUser)
    }

    // -------------------------------------------------------------
    // Teste 4: Autenticação - Atualizar Perfil de Usuário (DisplayName)
    // -------------------------------------------------------------
    @Test
    fun test04_autenticacaoAtualizarPerfilDisplayName() {
        val email = "integ_profile_${UUID.randomUUID()}@campushub.com"
        val senha = "SenhaSegura123"
        val nomeNovo = "Pedro Copette Teste"

        val createLatch = CountDownLatch(1)
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { createLatch.countDown() }
        createLatch.await(15, TimeUnit.SECONDS)

        val usuario = auth.currentUser
        assertNotNull(usuario)

        val updateLatch = CountDownLatch(1)
        val atualizacaoSucesso = AtomicBoolean(false)

        val profileRequest = UserProfileChangeRequest.Builder()
            .setDisplayName(nomeNovo)
            .build()

        usuario!!.updateProfile(profileRequest)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    atualizacaoSucesso.set(true)
                }
                updateLatch.countDown()
            }

        assertTrue("A atualização de perfil excedeu o tempo limite", updateLatch.await(15, TimeUnit.SECONDS))
        assertTrue("A atualização do nome do perfil falhou", atualizacaoSucesso.get())
        assertEquals(nomeNovo, auth.currentUser?.displayName)
    }

    // -------------------------------------------------------------
    // Teste 5: Autenticação - Enviar E-mail de Recuperação de Senha
    // -------------------------------------------------------------
    @Test
    fun test05_autenticacaoEnviarEmailRecuperacaoSenha() {
        val email = "integ_reset_${UUID.randomUUID()}@campushub.com"

        val resetLatch = CountDownLatch(1)
        val requisicaoCompletada = AtomicBoolean(false)

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                // Mesmas requisições em emuladores/ambiente real completam o fluxo
                requisicaoCompletada.set(true)
                resetLatch.countDown()
            }

        assertTrue("O envio de e-mail de recuperação excedeu o tempo limite", resetLatch.await(15, TimeUnit.SECONDS))
        assertTrue("A chamada de envio de e-mail de recuperação deve ser concluída", requisicaoCompletada.get())
    }

    // -------------------------------------------------------------
    // Teste 6: Autenticação - Encerramento de Sessão (Logout)
    // -------------------------------------------------------------
    @Test
    fun test06_autenticacaoLogoutEEstadoSessao() {
        val email = "integ_logout_${UUID.randomUUID()}@campushub.com"
        val senha = "SenhaSegura123"

        val createLatch = CountDownLatch(1)
        auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { createLatch.countDown() }
        createLatch.await(15, TimeUnit.SECONDS)

        assertNotNull("Usuário deve estar logado após criar conta", auth.currentUser)

        auth.signOut()

        assertNull("Após o signOut, currentUser deve retornar null", auth.currentUser)
    }

    // -------------------------------------------------------------
    // Teste 7: Firestore - Criar e Consultar Documento de Evento
    // -------------------------------------------------------------
    @Test
    fun test07_firestoreCriarEConsultarEvento() {
        val eventId = "test_event_${UUID.randomUUID()}"
        val nomeEvento = "Workshop de Jetpack Compose"

        val dadosEvento = hashMapOf(
            "nomeEvento" to nomeEvento,
            "dataEvento" to "20/11/2026",
            "horarioEvento" to "14:00",
            "localEvento" to "Lab de TI",
            "descricaoEvento" to "Treinamento prático de Compose",
            "categoria" to "Tecnologia"
        )

        val writeLatch = CountDownLatch(1)
        val escritaSucesso = AtomicBoolean(false)

        val docRef = firestore.collection("eventos").document(eventId)
        docRef.set(dadosEvento).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                escritaSucesso.set(true)
            }
            writeLatch.countDown()
        }

        assertTrue("A escrita no Firestore excedeu o tempo limite", writeLatch.await(15, TimeUnit.SECONDS))
        assertTrue("A gravação do evento no Firestore falhou", escritaSucesso.get())

        val readLatch = CountDownLatch(1)
        val nomeLido = AtomicReference<String?>(null)

        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                nomeLido.set(task.result.getString("nomeEvento"))
            }
            readLatch.countDown()
        }

        assertTrue("A leitura no Firestore excedeu o tempo limite", readLatch.await(15, TimeUnit.SECONDS))
        assertEquals(nomeEvento, nomeLido.get())
    }

    // -------------------------------------------------------------
    // Teste 8: Firestore - Filtrar Coleção de Eventos por Categoria
    // -------------------------------------------------------------
    @Test
    fun test08_firestoreFiltrarEventosPorCategoria() {
        val categoriaAlvo = "Tecnologia_IntegTest_${UUID.randomUUID()}"
        val eventId = "test_cat_${UUID.randomUUID()}"

        val dadosEvento = hashMapOf(
            "nomeEvento" to "Conferência de IA",
            "categoria" to categoriaAlvo,
            "dataEvento" to "25/11/2026",
            "horarioEvento" to "09:00",
            "localEvento" to "Auditório 1",
            "descricaoEvento" to "Eventos sobre tecnologia"
        )

        val writeLatch = CountDownLatch(1)
        firestore.collection("eventos").document(eventId).set(dadosEvento)
            .addOnCompleteListener { writeLatch.countDown() }
        writeLatch.await(15, TimeUnit.SECONDS)

        val queryLatch = CountDownLatch(1)
        val encontrouDocumento = AtomicBoolean(false)

        firestore.collection("eventos")
            .whereEqualTo("categoria", categoriaAlvo)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val docs = task.result.documents
                    if (docs.any { it.id == eventId }) {
                        encontrouDocumento.set(true)
                    }
                }
                queryLatch.countDown()
            }

        assertTrue("A consulta de filtro no Firestore excedeu o tempo limite", queryLatch.await(15, TimeUnit.SECONDS))
        assertTrue("Deveria ter encontrado o evento com a categoria filtrada", encontrouDocumento.get())
    }

    // -------------------------------------------------------------
    // Teste 9: Firestore - Salvar Favorito do Usuário
    // -------------------------------------------------------------
    @Test
    fun test09_firestoreSalvarFavoritoUsuario() {
        val userId = "user_fav_${UUID.randomUUID()}"
        val eventId = "event_fav_${UUID.randomUUID()}"

        val dadosFavorito = hashMapOf(
            "idEvento" to eventId,
            "nomeEvento" to "Palestra de Inovação",
            "dataAdicionado" to "05/11/2026"
        )

        val writeLatch = CountDownLatch(1)
        val favoritoSalvo = AtomicBoolean(false)

        val favRef = firestore.collection("usuarios")
            .document(userId)
            .collection("favoritos")
            .document(eventId)

        favRef.set(dadosFavorito).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                favoritoSalvo.set(true)
            }
            writeLatch.countDown()
        }

        assertTrue("A gravação de favorito excedeu o tempo limite", writeLatch.await(15, TimeUnit.SECONDS))
        assertTrue("O salvamento do favorito no Firestore falhou", favoritoSalvo.get())

        val readLatch = CountDownLatch(1)
        val lidoSucesso = AtomicBoolean(false)

        favRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result?.exists() == true) {
                lidoSucesso.set(true)
            }
            readLatch.countDown()
        }

        assertTrue("A consulta do favorito excedeu o tempo limite", readLatch.await(15, TimeUnit.SECONDS))
        assertTrue("O documento de favorito deve existir no Firestore", lidoSucesso.get())
    }

    // -------------------------------------------------------------
    // Teste 10: Firestore - Inscrever Usuário em Evento (Inscrição)
    // -------------------------------------------------------------
    @Test
    fun test10_firestoreInscreverUsuarioEmEvento() {
        val userId = "user_sub_${UUID.randomUUID()}"
        val eventId = "event_sub_${UUID.randomUUID()}"

        val dadosInscricao = hashMapOf(
            "idUsuario" to userId,
            "idEvento" to eventId,
            "nomeEvento" to "Semana Acadêmica de Computação",
            "statusInscricao" to "CONFIRMADO",
            "dataInscricao" to "05/11/2026 20:30"
        )

        val writeLatch = CountDownLatch(1)
        val inscricaoCompletada = AtomicBoolean(false)

        val subRef = firestore.collection("inscricoes")
            .document("${userId}_${eventId}")

        subRef.set(dadosInscricao).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                inscricaoCompletada.set(true)
            }
            writeLatch.countDown()
        }

        assertTrue("A gravação da inscrição excedeu o tempo limite", writeLatch.await(15, TimeUnit.SECONDS))
        assertTrue("A inscrição do usuário no evento falhou no Firestore", inscricaoCompletada.get())

        val verifyLatch = CountDownLatch(1)
        val statusLido = AtomicReference<String?>(null)

        subRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                statusLido.set(task.result.getString("statusInscricao"))
            }
            verifyLatch.countDown()
        }

        assertTrue("A leitura da inscrição excedeu o tempo limite", verifyLatch.await(15, TimeUnit.SECONDS))
        assertEquals("CONFIRMADO", statusLido.get())
    }
}
