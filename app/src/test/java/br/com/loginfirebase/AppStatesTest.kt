package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AppStatesTest {

    // Testes de LoginState

    @Test
    fun loginStateValidaCamposCorretamente() {
        val stateInvalido = LoginState()
        assertFalse(stateInvalido.eValido())
        assertEquals("Preencha o e-mail e a senha.", stateInvalido.validarComMensagem())

        val stateEmailInvalido = LoginState(email = "emailsemarroba", senha = "123")
        assertEquals("Digite um e-mail válido.", stateEmailInvalido.validarComMensagem())

        val stateValido = LoginState(email = "pedro@email.com", senha = "123456")
        assertTrue(stateValido.eValido())
        assertNull(stateValido.validarComMensagem())
    }

    // Testes de RegistrationState

    @Test
    fun registrationStateValidaCamposECredenciais() {
        val stateVazio = RegistrationState()
        assertEquals("Preencha todos os campos.", stateVazio.validar())

        val stateEmailInvalido = RegistrationState(nome = "Pedro", email = "pedroemail", senha = "123456")
        assertEquals("Digite um e-mail válido.", stateEmailInvalido.validar())

        val stateSenhaCurta = RegistrationState(nome = "Pedro", email = "pedro@email.com", senha = "123")
        assertEquals("A senha deve ter pelo menos 6 caracteres.", stateSenhaCurta.validar())

        val stateValido = RegistrationState(nome = "Pedro", email = "pedro@email.com", senha = "123456")
        assertNull(stateValido.validar())
    }

    // Testes de EventsState

    @Test
    fun eventsStateFiltraEContaEventos() {
        val eventos = listOf(
            EventoBusca("1", "Semana Acadêmica", "20/09/2026", "19:00", "URI", "Desc", "Acadêmico", "Santiago"),
            EventoBusca("2", "Hackathon CampusHub", "25/09/2026", "08:00", "Lab TI", "Desc", "Tecnologia", "Santiago")
        )

        val stateSemFiltro = EventsState(todosEventos = eventos)
        assertEquals(2, stateSemFiltro.quantidadeEncontrados())

        val stateFiltroTecnologia = EventsState(todosEventos = eventos, categoriaSelecionada = "Tecnologia")
        assertEquals(1, stateFiltroTecnologia.quantidadeEncontrados())
        assertEquals("Hackathon CampusHub", stateFiltroTecnologia.eventosFiltrados()[0].nome)
    }
}
