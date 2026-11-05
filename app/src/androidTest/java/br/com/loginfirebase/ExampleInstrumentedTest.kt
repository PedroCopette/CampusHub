package br.com.loginfirebase

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    companion object {

        @BeforeClass
        @JvmStatic
        fun configurarEmuladoresFirebase() {

            val contexto = InstrumentationRegistry
                .getInstrumentation()
                .targetContext

            if (FirebaseApp.getApps(contexto).isEmpty()) {
                FirebaseApp.initializeApp(contexto)
            }

            FirebaseAuth.getInstance().apply {
                useEmulator("127.0.0.1", 9099)
                firebaseAuthSettings.forceRecaptchaFlowForTesting(false)
            }

            FirebaseFirestore.getInstance()
                .useEmulator("10.0.2.2", 8080)
        }
    }

    @Test
    fun aplicativoDeveUsarOPacoteCorreto() {

        val appContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        assertEquals(
            "br.com.loginfirebase",
            appContext.packageName
        )
    }

    @Test
    fun aplicativoDeveTerContextoDisponivel() {

        val appContext = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        assertNotNull(appContext)
    }

    @Test
    fun firebaseDeveEstarInicializado() {

        val firebaseApp = FirebaseApp.getInstance()

        assertNotNull(firebaseApp)
    }

    @Test
    fun autenticacaoFirebaseDeveEstarDisponivel() {

        val autenticacao = FirebaseAuth.getInstance()

        assertNotNull(autenticacao)
    }

    @Test
    fun firestoreDeveEstarDisponivel() {

        val firestore = FirebaseFirestore.getInstance()

        assertNotNull(firestore)
    }

    @Test
    fun authEmulatorDeveResponderViaHttp() {

        val url = URL(
            "http://127.0.0.1:9099/identitytoolkit.googleapis.com/v1/accounts:signUp?key=fake-api-key"
        )

        val conexao = url.openConnection() as HttpURLConnection

        try {

            conexao.requestMethod = "POST"
            conexao.connectTimeout = 15000
            conexao.readTimeout = 15000
            conexao.doOutput = true

            conexao.setRequestProperty(
                "Content-Type",
                "application/json"
            )

            val email =
                "diagnostico${System.currentTimeMillis()}@campushub.com"

            val corpo = """
            {
                "email": "$email",
                "password": "Teste123456",
                "returnSecureToken": true
            }
        """.trimIndent()

            conexao.outputStream.use { output ->
                output.write(
                    corpo.toByteArray(Charsets.UTF_8)
                )
            }

            val codigoResposta = conexao.responseCode

            val resposta = if (codigoResposta >= 400) {

                conexao.errorStream
                    ?.bufferedReader()
                    ?.use { it.readText() }
                    ?: "Sem detalhes do erro."

            } else {

                conexao.inputStream
                    .bufferedReader()
                    .use { it.readText() }
            }

            assertEquals(
                "O Auth Emulator respondeu com erro: $resposta",
                200,
                codigoResposta
            )

        } finally {

            conexao.disconnect()
        }
    }

    @Test
    fun firestoreDevePermitirConsultarEventos() {

        val autenticacao = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()

        val resultadoLogin = CountDownLatch(1)

        val usuarioCriado = AtomicReference<String?>(null)
        val erroLogin = AtomicReference<String?>(null)

        val email =
            "teste${System.currentTimeMillis()}@campushub.com"

        val senha = "Teste123456"

        autenticacao.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { tarefa ->

                if (tarefa.isSuccessful) {

                    usuarioCriado.set(
                        tarefa.result.user?.uid
                    )

                } else {

                    erroLogin.set(
                        tarefa.exception?.toString()
                            ?: "Erro desconhecido"
                    )

                    android.util.Log.e(
                        "FirebaseTeste",
                        "Erro ao criar usuário no Auth Emulator",
                        tarefa.exception
                    )
                }

                resultadoLogin.countDown()
            }

        assertTrue(
            "A criação do usuário demorou mais de 60 segundos.",
            resultadoLogin.await(60, TimeUnit.SECONDS)
        )

        assertNotNull(
            "Não foi possível autenticar no emulador: ${erroLogin.get()}",
            usuarioCriado.get()
        )

        val resultadoConsulta = CountDownLatch(1)

        val consultaFuncionou = AtomicBoolean(false)
        val mensagemErro = AtomicReference<String?>(null)

        firestore.collection("eventos")
            .limit(1)
            .get()
            .addOnCompleteListener { tarefa ->

                consultaFuncionou.set(tarefa.isSuccessful)

                if (!tarefa.isSuccessful) {

                    mensagemErro.set(
                        tarefa.exception?.message
                            ?: "Erro desconhecido ao consultar o Firestore."
                    )
                }

                resultadoConsulta.countDown()
            }

        assertTrue(
            "A consulta demorou mais de 60 segundos.",
            resultadoConsulta.await(60, TimeUnit.SECONDS)
        )

        assertTrue(
            "A consulta ao Firestore falhou: ${mensagemErro.get()}",
            consultaFuncionou.get()
        )

        autenticacao.signOut()
    }
}
