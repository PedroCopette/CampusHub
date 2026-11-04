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
}