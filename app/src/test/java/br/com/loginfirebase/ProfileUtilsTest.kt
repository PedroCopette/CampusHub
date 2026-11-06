package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileUtilsTest {

    @Test
    fun formatarNomeExibicaoPriorizaNome() {
        val resultado = ProfileUtils.formatarNomeExibicao("Pedro Copette", "pedro@email.com")
        assertEquals("Pedro Copette", resultado)
    }

    @Test
    fun formatarNomeExibicaoUsaEmailQuandoNomeVazio() {
        val resultado = ProfileUtils.formatarNomeExibicao("", "pedro@email.com")
        assertEquals("pedro", resultado)
    }

    @Test
    fun formatarNomeExibicaoUsaPadraoQuandoDadosInvalidos() {
        val resultado = ProfileUtils.formatarNomeExibicao(null, null)
        assertEquals("Estudante CampusHub", resultado)
    }

    @Test
    fun formatarEmailExibicaoRetornaEmailOuMensagemPadrao() {
        assertEquals("pedro@email.com", ProfileUtils.formatarEmailExibicao("pedro@email.com"))
        assertEquals("E-mail não informado", ProfileUtils.formatarEmailExibicao(null))
        assertEquals("E-mail não informado", ProfileUtils.formatarEmailExibicao("   "))
    }

    @Test
    fun validarEdicaoNomeValidaNomesValidosEErros() {
        assertTrue(ProfileUtils.validarEdicaoNome("Pedro"))
        assertFalse(ProfileUtils.validarEdicaoNome("   "))
        assertFalse(ProfileUtils.validarEdicaoNome(""))
    }
}
