package br.com.loginfirebase

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class MainActivityComposeTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    // Teste 1: Verifica se os textos principais da tela de login aparecem
    @Test
    fun telaLoginDeveExibirTextosPrincipais() {

        composeRule.onNodeWithText("CampusHub")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Bem-vindo de volta!")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Seu campus,\nmais conectado.")
            .assertIsDisplayed()
    }

    // Teste 2: Verifica se os campos de e-mail e senha aparecem
    @Test
    fun telaLoginDeveExibirCamposEmailESenha() {

        composeRule.onNodeWithText("E-mail")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Senha")
            .assertIsDisplayed()
    }

    // Teste 3: Verifica se os botões de login e cadastro aparecem
    @Test
    fun telaLoginDeveExibirBotoes() {

        composeRule.onNodeWithText("ENTRAR")
            .assertIsDisplayed()

        composeRule.onNodeWithText("CRIAR CONTA")
            .assertIsDisplayed()

        composeRule.onNodeWithText("Esqueci minha senha")
            .assertIsDisplayed()
    }
}