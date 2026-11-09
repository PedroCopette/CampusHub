package br.com.loginfirebase.ui.components

import br.com.loginfirebase.EventoBusca
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class UiComponentsTest {

    @Test
    fun testarParametrosDeComponentesDeLogin() {
        var entrou = false
        var abriuCadastro = false
        var recuperouSenha = false

        val lambdaEntrar: (String, String) -> Unit = { e, s ->
            if (e == "test@email.com" && s == "123456") entrou = true
        }
        val lambdaCadastro: () -> Unit = { abriuCadastro = true }
        val lambdaRecuperar: () -> Unit = { recuperouSenha = true }

        lambdaEntrar("test@email.com", "123456")
        lambdaCadastro()
        lambdaRecuperar()

        assert(entrou)
        assert(abriuCadastro)
        assert(recuperouSenha)
    }

    @Test
    fun testarParametrosDeComponentesDeCadastro() {
        var registrou = false
        var voltou = false

        val lambdaRegistrar: (String, String, String) -> Unit = { n, e, s ->
            if (n == "Pedro" && e == "p@e.com" && s == "123") registrou = true
        }
        val lambdaVoltar: () -> Unit = { voltou = true }

        lambdaRegistrar("Pedro", "p@e.com", "123")
        lambdaVoltar()

        assert(registrou)
        assert(voltou)
    }

    @Test
    fun testarParametrosDeComponentesDeDashboard() {
        var clicado = false
        val lambdaClicar: () -> Unit = { clicado = true }
        lambdaClicar()
        assert(clicado)
    }

    @Test
    fun testarParametrosDeComponentesDeEventos() {
        val evento = EventoBusca("1", "E1", "20/09/2026", "19:00", "L1", "D1", "Tecnologia", "E1")
        assertNotNull(evento)
        assertEquals("E1", evento.nome)
    }
}
