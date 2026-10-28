package br.com.loginfirebase

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun soma_deveEstarCorreta() {
        assertTrue(2 + 2 == 4)
    }

    @Test
    fun camposPreenchidos_deveRetornarTrue() {
        assertTrue(
            CadastroValidator.camposPreenchidos(
                "Pedro Copette",
                "pedro@email.com",
                "123456"
            )
        )
    }

    @Test
    fun nomeVazio_deveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "",
                "pedro@email.com",
                "123456"
            )
        )
    }

    @Test
    fun emailVazio_deveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "Pedro Copette",
                "",
                "123456"
            )
        )
    }

    @Test
    fun senhaVazia_deveRetornarFalse() {
        assertFalse(
            CadastroValidator.camposPreenchidos(
                "Pedro Copette",
                "pedro@email.com",
                ""
            )
        )
    }

    @Test
    fun senhaComSeisCaracteres_deveRetornarTrue() {
        assertTrue(CadastroValidator.senhaValida("123456"))
    }

    @Test
    fun senhaCurta_deveRetornarFalse() {
        assertFalse(CadastroValidator.senhaValida("12345"))
    }

    @Test
    fun emailValido_deveRetornarTrue() {
        assertTrue(CadastroValidator.emailValido("pedro@email.com"))
    }

    @Test
    fun emailSemArroba_deveRetornarFalse() {
        assertFalse(CadastroValidator.emailValido("pedroemail.com"))
    }

    @Test
    fun emailSemDominio_deveRetornarFalse() {
        assertFalse(CadastroValidator.emailValido("pedro@"))
    }
}
