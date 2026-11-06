package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ComposeUiCoverageTest {

    @Test
    fun testarEstadoDeLogin() {
        val state = LoginState("pedro@email.com", "123456")
        assertNotNull(state)
        assertEquals("pedro@email.com", state.email)
    }

    @Test
    fun testarEstadoDeCadastro() {
        val state = RegistrationState("Pedro", "pedro@email.com", "123456")
        assertNotNull(state)
        assertEquals("Pedro", state.nome)
    }

    @Test
    fun testarEstadoDeEventos() {
        val state = EventsState(busca = "Hackathon")
        assertNotNull(state)
        assertEquals("Hackathon", state.busca)
    }
}
