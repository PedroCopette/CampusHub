package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DashboardUtilsTest {

    @Test
    fun formatarSaudacaoComNomeValido() {
        val resultado = DashboardUtils.formatarSaudacao("Pedro")
        assertEquals("Bem-vindo(a), Pedro!", resultado)
    }

    @Test
    fun formatarSaudacaoComNomeNuloOuVazio() {
        assertEquals("Bem-vindo ao CampusHub!", DashboardUtils.formatarSaudacao(null))
        assertEquals("Bem-vindo ao CampusHub!", DashboardUtils.formatarSaudacao("   "))
    }

    @Test
    fun obterOpcoesMenuContemTodasAsOpcoes() {
        val opcoes = DashboardUtils.obterOpcoesMenu()
        assertEquals(5, opcoes.size)
        assertTrue(opcoes.contains("Eventos"))
        assertTrue(opcoes.contains("Meus Eventos"))
        assertTrue(opcoes.contains("Meus Favoritos"))
        assertTrue(opcoes.contains("Meu Perfil"))
        assertTrue(opcoes.contains("Sair"))
    }

    @Test
    fun ehOpcaoValidaValidaCorretamente() {
        assertTrue(DashboardUtils.ehOpcaoValida("Eventos"))
        assertFalse(DashboardUtils.ehOpcaoValida("Opcao Inexistente"))
    }
}
