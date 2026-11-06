package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FavoritesManagerTest {

    @Test
    fun mapearDocumentoParaFavoritoMapeiaCampos() {
        val dados = mapOf(
            "idEvento" to "ev1",
            "nomeEvento" to "Semana Acadêmica",
            "dataEvento" to "20/09/2026",
            "horarioEvento" to "19:00",
            "localEvento" to "URI"
        )

        val fav = FavoritesManager.mapearDocumentoParaFavorito(dados)
        assertNotNull(fav)
        assertEquals("ev1", fav?.id)
        assertEquals("Semana Acadêmica", fav?.nome)
    }

    @Test
    fun mapearDocumentoParaFavoritoRetornaNullSeDadosInvalidos() {
        assertNull(FavoritesManager.mapearDocumentoParaFavorito(emptyMap()))
    }

    @Test
    fun mapearListaFavoritosFiltraValidos() {
        val doc1 = mapOf("idEvento" to "ev1", "nomeEvento" to "E1", "dataEvento" to "10/09/2026", "horarioEvento" to "10:00", "localEvento" to "L1")
        val doc2 = mapOf<String, Any?>("idEvento" to null)

        val lista = FavoritesManager.mapearListaFavoritos(listOf(doc1, doc2))
        assertEquals(1, lista.size)
        assertEquals("E1", lista[0].nome)
    }

    @Test
    fun criarDadosFavoritoGeraMapCorreto() {
        val map = FavoritesManager.criarDadosFavorito("e1", "Evento 1", "20/09/2026", "19:00", "URI", "u1", "u1@email.com")
        assertEquals("e1", map["idEvento"])
        assertEquals("u1@email.com", map["usuarioEmail"])
    }
}
