
package br.com.loginfirebase

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class RegistrationActivityComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun telaCadastroDeveExibirTextosPrincipais() {

        composeTestRule.setContent {
            TelaCadastro(
                registrar = { _, _, _ -> },
                voltar = {}
            )
        }

        composeTestRule
            .onNodeWithText("CampusHub")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Crie sua conta")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Vamos começar!")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("CRIAR CONTA")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("VOLTAR")
            .assertIsDisplayed()
    }

    @Test
    fun camposCadastroDevemPermitirPreenchimento() {

        composeTestRule.setContent {
            TelaCadastro(
                registrar = { _, _, _ -> },
                voltar = {}
            )
        }

        val campos = composeTestRule.onAllNodes(
            hasSetTextAction()
        )

        campos[0].performTextInput("Pedro Copette")
        campos[1].performTextInput("pedro@email.com")
        campos[2].performTextInput("123456")

        composeTestRule
            .onNodeWithText("CRIAR CONTA")
            .assertIsDisplayed()
    }

    @Test
    fun botaoCriarContaDeveEnviarDadosPreenchidos() {

        var nomeRecebido = ""
        var emailRecebido = ""
        var senhaRecebida = ""

        composeTestRule.setContent {
            TelaCadastro(
                registrar = { nome, email, senha ->
                    nomeRecebido = nome
                    emailRecebido = email
                    senhaRecebida = senha
                },
                voltar = {}
            )
        }

        val campos = composeTestRule.onAllNodes(
            hasSetTextAction()
        )

        campos[0].performTextInput("Pedro Copette")
        campos[1].performTextInput("pedro@email.com")
        campos[2].performTextInput("123456")

        composeTestRule
            .onNodeWithText("CRIAR CONTA")
            .performClick()

        composeTestRule.runOnIdle {

            assertEquals(
                "Pedro Copette",
                nomeRecebido
            )

            assertEquals(
                "pedro@email.com",
                emailRecebido
            )

            assertEquals(
                "123456",
                senhaRecebida
            )
        }
    }
}
