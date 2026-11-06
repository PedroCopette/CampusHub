package br.com.loginfirebase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CadastroValidatorTest {

    // Testes para campos preenchidos

    @Test
    fun camposPreenchidosDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.camposPreenchidos(
                "Pedro",
                "pedro@email.com",
                "123456"
            )
        )
    }

    @Test
    fun camposPreenchidosComNomeVazioDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "",
                "pedro@email.com",
                "123456"
            )
        )
    }

    @Test
    fun camposPreenchidosComEmailVazioDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "Pedro",
                "",
                "123456"
            )
        )
    }

    @Test
    fun camposPreenchidosComSenhaVaziaDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "Pedro",
                "pedro@email.com",
                ""
            )
        )
    }

    @Test
    fun camposPreenchidosComEspacosDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "   ",
                "pedro@email.com",
                "123456"
            )
        )
    }

    @Test
    fun camposPreenchidosComEspacosNasExtremidadesDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.camposPreenchidos(
                " Pedro ",
                " pedro@email.com ",
                " 123456 "
            )
        )
    }

    // Testes para validação de senha

    @Test
    fun senhaValidaComSeisCaracteresDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.senhaValida("123456")
        )
    }

    @Test
    fun senhaValidaComMenosDeSeisCaracteresDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.senhaValida("12345")
        )
    }

    @Test
    fun senhaValidaComEspacosDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.senhaValida("      ")
        )
    }

    @Test
    fun senhaValidaComSeteCaracteresDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.senhaValida("1234567")
        )
    }

    @Test
    fun senhaValidaComEspacosNasExtremidadesDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.senhaValida(" 123456 ")
        )
    }

    @Test
    fun senhaVaziaDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.senhaValida("")
        )
    }

    // Testes para validação de e-mail

    @Test
    fun emailValidoDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.emailValido("pedro@email.com")
        )
    }

    @Test
    fun emailValidoComEmailIncorretoDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.emailValido("pedroemail.com")
        )
    }

    @Test
    fun emailValidoSemDominioDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.emailValido("pedro@")
        )
    }

    @Test
    fun emailValidoComEspacosDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.emailValido("     ")
        )
    }

    @Test
    fun emailValidoComDominioDeDoisCaracteresDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.emailValido("pedro@email.br")
        )
    }

    @Test
    fun emailValidoComDominioDeUmCaracterDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.emailValido("pedro@email.c")
        )
    }

    @Test
    fun emailValidoComEspacosNasExtremidadesDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.emailValido(" pedro@email.com ")
        )
    }

    // Testes para validação dos campos de login

    @Test
    fun loginCamposPreenchidosComDadosValidosRetornaTrue() {
        assertTrue(
            CadastroValidator.loginCamposPreenchidos(
                "pedro@email.com",
                "123456"
            )
        )
    }

    @Test
    fun loginCamposPreenchidosComEmailVazioRetornaFalse() {
        assertFalse(
            CadastroValidator.loginCamposPreenchidos(
                "",
                "123456"
            )
        )
    }

    @Test
    fun loginCamposPreenchidosComSenhaVaziaRetornaFalse() {
        assertFalse(
            CadastroValidator.loginCamposPreenchidos(
                "pedro@email.com",
                ""
            )
        )
    }

    @Test
    fun loginCamposPreenchidosComEspacosRetornaFalse() {
        assertFalse(
            CadastroValidator.loginCamposPreenchidos(
                "   ",
                "   "
            )
        )
    }

    // Testes de borda adicionais para alta cobertura

    @Test
    fun emailValidoComMuitosArrobasDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.emailValido("pedro@@email.com")
        )
    }

    @Test
    fun emailValidoComCaracteresEspeciaisCorretosDeveRetornarTrue() {
        assertTrue(
            CadastroValidator.emailValido("pedro.copette+test@sub.domain.com")
        )
    }

    @Test
    fun senhaValidaComEspacosEAbasDeveRetornarFalseSeMenorQueSeisAposTrim() {
        assertFalse(
            CadastroValidator.senhaValida("  123  ")
        )
    }

    @Test
    fun loginCamposPreenchidosComQuebraDeLinhaVaziaDeveRetornarFalse() {
        assertFalse(
            CadastroValidator.loginCamposPreenchidos(
                "\n\t",
                "\n"
            )
        )
    }
}