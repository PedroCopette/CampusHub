package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UiStateCalculatorsTest {

    @Test
    fun calcularCoresFundoHex() {
        assertEquals(0xFFF0F5FF, UiStateCalculators.calcularCorFundoHex(destaque = true, sair = false))
        assertEquals(0xFFFFF0F0, UiStateCalculators.calcularCorFundoHex(destaque = false, sair = true))
        assertEquals(0xFFFFFFFF, UiStateCalculators.calcularCorFundoHex(destaque = false, sair = false))
    }

    @Test
    fun calcularCoresIconeETextoHex() {
        assertEquals(0xFFD42121, UiStateCalculators.calcularCorIconeHex(sair = true))
        assertEquals(0xFF172D59, UiStateCalculators.calcularCorIconeHex(sair = false))

        assertEquals(0xFFD42121, UiStateCalculators.calcularCorTextoHex(sair = true))
        assertEquals(0xFF142748, UiStateCalculators.calcularCorTextoHex(sair = false))

        assertEquals(0xFFBE5555, UiStateCalculators.calcularCorDescricaoHex(sair = true))
        assertEquals(0xFF657694, UiStateCalculators.calcularCorDescricaoHex(sair = false))
    }

    @Test
    fun calcularCoresBordaHex() {
        assertEquals(0xFF91B6FF, UiStateCalculators.calcularCorBordaHex(destaque = true, sair = false))
        assertEquals(0xFFFFD0D0, UiStateCalculators.calcularCorBordaHex(destaque = false, sair = true))
        assertEquals(0xFFE2EAF6, UiStateCalculators.calcularCorBordaHex(destaque = false, sair = false))
    }

    @Test
    fun resolverIconeCategoriaEValidadorTipos() {
        assertEquals("academico", UiStateCalculators.resolverIconeCategoria("Acadêmico"))
        assertEquals("tecnologia", UiStateCalculators.resolverIconeCategoria("Tecnologia"))

        assertTrue(UiStateCalculators.ehTipoIconeEventoValido("busca"))
        assertFalse(UiStateCalculators.ehTipoIconeEventoValido("invalido"))

        assertTrue(UiStateCalculators.ehTipoIconeLinhaValido("calendario"))
        assertFalse(UiStateCalculators.ehTipoIconeLinhaValido("invalido"))

        assertTrue(UiStateCalculators.ehTipoIconeFavoritoValido("star"))
        assertFalse(UiStateCalculators.ehTipoIconeFavoritoValido("invalido"))

        assertTrue(UiStateCalculators.ehTipoIconeMeusEventosValido("calendario"))
        assertFalse(UiStateCalculators.ehTipoIconeMeusEventosValido("invalido"))
    }
}
