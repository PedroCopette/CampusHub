package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EventsManagerTest {

    @Test
    fun obterEnderecoPadraoRetornaEnderecosCorretos() {
        assertEquals("Av. Batista Bonotto Sobrinho, 733, Santiago - RS, Brasil", EventsManager.obterEnderecoPadrao("semana_academica"))
        assertEquals("Rua Tio Virgílio, 423, Santiago - RS, Brasil", EventsManager.obterEnderecoPadrao("hackathon_campushub"))
        assertEquals("Rua Bento Gonçalves, 1642, Santiago - RS, Brasil", EventsManager.obterEnderecoPadrao("palestra_tecnologia"))
        assertEquals("Santiago, Rio Grande do Sul, Brasil", EventsManager.obterEnderecoPadrao("outro"))
    }

    @Test
    fun mapearDocumentoParaEventoMapeiaCorretamente() {
        val dados = mapOf(
            "nomeEvento" to "Hackathon",
            "dataEvento" to "25/09/2026",
            "horarioEvento" to "08:00",
            "localEvento" to "Lab TI",
            "descricaoEvento" to "Competição",
            "categoria" to "Tecnologia",
            "enderecoEvento" to "Rua A, 10"
        )

        val evento = EventsManager.MapearDocumentoParaEvento("h1", dados)
        assertNotNull(evento)
        assertEquals("h1", evento?.id)
        assertEquals("Hackathon", evento?.nome)
        assertEquals("Rua A, 10", evento?.endereco)
    }

    @Test
    fun mapearDocumentoParaEventoRetornaNullSeCamposFaltando() {
        val dadosIncompletos = mapOf<String, Any?>("nomeEvento" to "Incompleto")
        assertNull(EventsManager.MapearDocumentoParaEvento("h1", dadosIncompletos))
    }

    @Test
    fun mapearListaDocumentosOrdenaCronologicamente() {
        val doc1 = Pair("h1", mapOf("nomeEvento" to "Evento 2", "dataEvento" to "25/09/2026", "horarioEvento" to "08:00", "localEvento" to "Lab", "descricaoEvento" to "D", "categoria" to "Tec"))
        val doc2 = Pair("h2", mapOf("nomeEvento" to "Evento 1", "dataEvento" to "10/09/2026", "horarioEvento" to "08:00", "localEvento" to "Lab", "descricaoEvento" to "D", "categoria" to "Tec"))

        val lista = EventsManager.MapearListaDocumentos(listOf(doc1, doc2))
        assertEquals(2, lista.size)
        assertEquals("Evento 1", lista[0].nome)
        assertEquals("Evento 2", lista[1].nome)
    }

    @Test
    fun obterEventosIniciaisRetornaListaIniciais() {
        val iniciais = EventsManager.obterEventosIniciais()
        assertEquals(3, iniciais.size)
        assertTrue(iniciais.any { it.id == "semana_academica" })
    }
}
