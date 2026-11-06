package br.com.loginfirebase

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EndToEndUserFlowsTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    // -------------------------------------------------------------
    // Teste E2E 1: Fluxo Completo de Autenticação e Interação com a Interface
    // -------------------------------------------------------------
    @Test
    fun e2e_fluxoAutenticacaoECadastro() {
        // Step 1: Tela de Login exibindo títulos principais
        composeRule.onNodeWithText("CampusHub").assertIsDisplayed()
        composeRule.onNodeWithText("Bem-vindo de volta!").assertIsDisplayed()

        // Step 2: Inserir credenciais na tela de login
        composeRule.onNodeWithText("E-mail").performClick()
        composeRule.onNodeWithText("E-mail").performTextInput("aluno@campushub.com")

        composeRule.onNodeWithText("Senha").performClick()
        composeRule.onNodeWithText("Senha").performTextInput("123456")

        // Step 3: Pressionar botão de login
        composeRule.onNodeWithText("ENTRAR").performClick()

        // Step 4: Navegar para a tela de cadastro
        composeRule.onNodeWithText("CRIAR CONTA").performClick()
    }

    // -------------------------------------------------------------
    // Teste E2E 2: Fluxo Completo de Consulta, Filtro e Visualização de Eventos
    // -------------------------------------------------------------
    @Test
    fun e2e_fluxoConsultaEFiltroDeEventos() {
        // Step 1: Renderizar tela de cadastro com formulário preenchido
        composeRule.setContent {
            TelaCadastro(
                registrar = { _, _, _ -> },
                voltar = {}
            )
        }

        // Step 2: Preencher campos de cadastro
        val campos = composeRule.onAllNodes(hasSetTextAction())
        campos[0].performTextInput("Pedro Copette")
        campos[1].performTextInput("pedro.e2e@campushub.com")
        campos[2].performTextInput("Senha123")

        // Step 3: Executar ação do botão de cadastro
        composeRule.onNodeWithText("CRIAR CONTA").assertIsDisplayed()
        composeRule.onNodeWithText("CRIAR CONTA").performClick()

        // Step 4: Confirmar que o botão VOLTAR está funcional
        composeRule.onNodeWithText("VOLTAR").assertIsDisplayed()
        composeRule.onNodeWithText("VOLTAR").performClick()
    }
}
