package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class EventDetailsManagerTest {

    @Test
    fun mapearDocumentoParaComentarioMapeiaComSucesso() {
        val dados = mapOf(
            "texto" to "Excelente evento!",
            "usuarioId" to "u1",
            "autorNome" to "Pedro",
            "dataPublicacao" to "20/09/2026 19:30"
        )

        val com = EventDetailsManager.mapearDocumentoParaComentario("c1", dados)
        assertNotNull(com)
        assertEquals("c1", com?.id)
        assertEquals("Excelente evento!", com?.texto)
    }

    @Test
    fun mapearDocumentoParaComentarioRetornaNullSeFaltarTexto() {
        assertNull(EventDetailsManager.mapearDocumentoParaComentario("c1", emptyMap()))
    }

    @Test
    fun mapearListaComentariosFiltraValidos() {
        val doc1 = Pair("c1", mapOf("texto" to "T1", "usuarioId" to "u1", "autorNome" to "A1", "dataPublicacao" to "D1"))
        val doc2 = Pair("c2", mapOf<String, Any?>())

        val lista = EventDetailsManager.mapearListaComentarios(listOf(doc1, doc2))
        assertEquals(1, lista.size)
        assertEquals("T1", lista[0].texto)
    }

    @Test
    fun calcularMediaEQuantidadeCalculaCorretamente() {
        val (media, count) = EventDetailsManager.calcularMediaEQuantidade(listOf(4L, 5L))
        assertEquals(4.5, media, 0.01)
        assertEquals(2, count)

        val (mediaVazia, countVazio) = EventDetailsManager.calcularMediaEQuantidade(emptyList())
        assertEquals(0.0, mediaVazia, 0.01)
        assertEquals(0, countVazio)
    }

    @Test
    fun criarDadosComentarioGeraHashMapCorreto() {
        val map = EventDetailsManager.criarDadosComentario("e1", "E1", "Texto", "u1", "Pedro", "20/09/2026 19:30")
        assertEquals("e1", map["idEvento"])
        assertEquals("Texto", map["texto"])
    }

    @Test
    fun criarDadosAvaliacaoGeraHashMapCorreto() {
        val map = EventDetailsManager.criarDadosAvaliacao("e1", "u1", "u1@email.com", 5)
        assertEquals("e1", map["idEvento"])
        assertEquals(5, map["nota"])
    }
}
