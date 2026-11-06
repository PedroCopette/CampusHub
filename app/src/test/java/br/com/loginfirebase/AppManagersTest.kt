package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppManagersTest {

    @Test
    fun authManagerValidarLogin() {
        val (valido, msg) = AuthManager.validarLogin("pedro@email.com", "123456")
        assertTrue(valido)
        assertEquals("OK", msg)

        val (invalidoVazio, msgVazio) = AuthManager.validarLogin("", "123456")
        assertFalse(invalidoVazio)
        assertEquals("Preencha o e-mail e a senha.", msgVazio)

        val (invalidoEmail, msgEmail) = AuthManager.validarLogin("emailinvalido", "123456")
        assertFalse(invalidoEmail)
        assertEquals("Digite um e-mail válido.", msgEmail)
    }

    @Test
    fun authManagerValidarCadastro() {
        val (valido, msg) = AuthManager.validarCadastro("Pedro", "pedro@email.com", "123456")
        assertTrue(valido)
        assertEquals("OK", msg)

        val (invalidoVazio, msgVazio) = AuthManager.validarCadastro("", "pedro@email.com", "123456")
        assertFalse(invalidoVazio)
        assertEquals("Preencha todos os campos.", msgVazio)

        val (invalidoSenha, msgSenha) = AuthManager.validarCadastro("Pedro", "pedro@email.com", "123")
        assertFalse(invalidoSenha)
        assertEquals("A senha deve ter pelo menos 6 caracteres.", msgSenha)
    }

    @Test
    fun recoveryManagerValidarEmailRecuperacao() {
        val (valido, msg) = RecoveryManager.validarEmailRecuperacao("pedro@email.com")
        assertTrue(valido)
        assertEquals("OK", msg)

        val (invalidoVazio, msgVazio) = RecoveryManager.validarEmailRecuperacao("")
        assertFalse(invalidoVazio)
        assertEquals("Digite seu e-mail.", msgVazio)

        val (invalidoEmail, msgEmail) = RecoveryManager.validarEmailRecuperacao("emailinvalido")
        assertFalse(invalidoEmail)
        assertEquals("Digite um e-mail válido.", msgEmail)
    }

    @Test
    fun profileManagerTratarDadosEValidarEdicao() {
        val (nome, email) = ProfileManager.tratarDadosPerfil("Pedro Copette", "pedro@email.com")
        assertEquals("Pedro Copette", nome)
        assertEquals("pedro@email.com", email)

        val (valido, msg) = ProfileManager.validarEdicaoPerfil("Pedro")
        assertTrue(valido)
        assertEquals("OK", msg)

        val (invalido, msgInvalido) = ProfileManager.validarEdicaoPerfil("   ")
        assertFalse(invalido)
        assertEquals("Digite um nome.", msgInvalido)
    }

    @Test
    fun dashboardManagerSaudacaoEMenu() {
        val saudacao = DashboardManager.formatarSaudacaoCompleta("Pedro")
        assertEquals("Bem-vindo(a), Pedro!", saudacao)

        val menu = DashboardManager.obterMenuOpcoesList()
        assertEquals(5, menu.size)
    }
}
