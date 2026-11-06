package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoritesUtilsTest {

    private val favoritos = listOf(
        FavoritoEvento("e1", "Semana Acadêmica", "20/09/2026", "19:00", "URI"),
        FavoritoEvento("e2", "Hackathon", "25/09/2026", "08:00", "Lab TI")
    )

    @Test
    fun estaNosFavoritosRetornaTrueSeExistir() {
        assertTrue(FavoritesUtils.estaNosFavoritos(favoritos, "e1"))
        assertFalse(FavoritesUtils.estaNosFavoritos(favoritos, "e3"))
    }

    @Test
    fun formatarContadorFavoritosFormataCorretamente() {
        assertEquals("Nenhum evento salvo", FavoritesUtils.formatarContadorFavoritos(0))
        assertEquals("1 evento salvo", FavoritesUtils.formatarContadorFavoritos(1))
        assertEquals("5 eventos salvos", FavoritesUtils.formatarContadorFavoritos(5))
    }

    @Test
    fun filtrarFavoritosPorNomeFiltraSemDiferenciarMaiusculas() {
        val resultado = FavoritesUtils.filtrarFavoritosPorNome(favoritos, "hackathon")
        assertEquals(1, resultado.size)
        assertEquals("Hackathon", resultado[0].nome)
    }

    @Test
    fun filtrarFavoritosPorNomeRetornaTodosSeBuscaVazia() {
        val resultado = FavoritesUtils.filtrarFavoritosPorNome(favoritos, "   ")
        assertEquals(2, resultado.size)
    }
}
