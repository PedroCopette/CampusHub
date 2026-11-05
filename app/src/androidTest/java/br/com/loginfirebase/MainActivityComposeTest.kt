package br.com.loginfirebase

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class MainActivityComposeTest {

    @get:Rule
    val composeRule = createComposeRule()

    // Teste 1: Verifica os textos principais da tela
    @Test
    fun telaLoginDeveExibirTextosPrincipais() {

        composeRule.setContent {
            TelaLogin(
                entrar = { _, _ -> },
                abrirCadastro = {},
                recuperarSenha = {}
            )
        }

        composeRule.onNodeWithText("CampusHub")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Bem-vindo de volta!")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Seu campus,\nmais conectado.")
            .assertIsDisplayed()
    }

    // Teste 2: Verifica os campos de e-mail e senha
    @Test
    fun telaLoginDeveExibirCamposEmailESenha() {

        composeRule.setContent {
            TelaLogin(
                entrar = { _, _ -> },
                abrirCadastro = {},
                recuperarSenha = {}
            )
        }

        composeRule.onNodeWithText("E-mail")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Senha")
            .assertIsDisplayed()
    }

    // Teste 3: Verifica os botões da tela
    @Test
    fun telaLoginDeveExibirBotoes() {

        composeRule.setContent {
            TelaLogin(
                entrar = { _, _ -> },
                abrirCadastro = {},
                recuperarSenha = {}
            )
        }

        composeRule.onNodeWithText("ENTRAR")
            .assertIsDisplayed()

        composeRule.onNodeWithText("CRIAR CONTA")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Esqueci minha senha")
            .assertIsDisplayed()
    }

    // Teste 4: Verifica se o e-mail pode ser digitado
    @Test
    fun campoEmailDeveAceitarDigitacao() {

        composeRule.setContent {
            TelaLogin(
                entrar = { _, _ -> },
                abrirCadastro = {},
                recuperarSenha = {}
            )
        }

        composeRule.onNodeWithText("E-mail")
            .performClick()

        composeRule.onNodeWithText("E-mail")
            .performTextInput("teste@email.com")

        composeRule.onNodeWithText("teste@email.com")
            .assertIsDisplayed()
    }

    // Teste 5: Verifica se o botão de cadastro executa o callback
    @Test
    fun botaoCadastroDeveExecutarCallback() {

        var cadastroFoiExecutado = false

        composeRule.setContent {
            TelaLogin(
                entrar = { _, _ -> },
                abrirCadastro = {
                    cadastroFoiExecutado = true
                },
                recuperarSenha = {}
            )
        }

        composeRule.onNodeWithText("CRIAR CONTA")
            .performClick()

        assertTrue(cadastroFoiExecutado)
    }

    // Teste 6: Verifica se o botão de recuperação executa o callback
    @Test
    fun botaoRecuperacaoDeveExecutarCallback() {

        var recuperacaoFoiExecutada = false

        composeRule.setContent {
            TelaLogin(
                entrar = { _, _ -> },
                abrirCadastro = {},
                recuperarSenha = {
                    recuperacaoFoiExecutada = true
                }
            )
        }

        composeRule.onNodeWithText("Esqueci minha senha")
            .performClick()

        assertTrue(recuperacaoFoiExecutada)
    }

    // Teste 7: Verifica se o botão entrar envia os campos preenchidos
    @Test
    fun botaoEntrarDeveEnviarEmailESenha() {

        var emailRecebido = ""
        var senhaRecebida = ""

        composeRule.setContent {
            TelaLogin(
                entrar = { email, senha ->
                    emailRecebido = email
                    senhaRecebida = senha
                },
                abrirCadastro = {},
                recuperarSenha = {}
            )
        }

        composeRule.onNodeWithText("E-mail")
            .performClick()

        composeRule.onNodeWithText("E-mail")
            .performTextInput("teste@email.com")

        composeRule.onNodeWithText("Senha")
            .performClick()

        composeRule.onNodeWithText("Senha")
            .performTextInput("123456")

        composeRule.onNodeWithText("ENTRAR")
            .performClick()

        composeRule.runOnIdle {
            assertEquals("teste@email.com", emailRecebido)
            assertEquals("123456", senhaRecebida)
        }
    }
}
