package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MyEventsManagerTest {

    @Test
    fun mapearDocumentoParaInscricaoMapeiaCampos() {
        val dados = mapOf(
            "nomeEvento" to "Semana Acadêmica",
            "dataEvento" to "20/09/2026",
            "horarioEvento" to "19:00",
            "localEvento" to "URI"
        )

        val inscricao = MyEventsManager.mapearDocumentoParaInscricao(dados)
        assertNotNull(inscricao)
        assertEquals("Semana Acadêmica", inscricao?.nome)
    }

    @Test
    fun mapearDocumentoParaInscricaoRetornaNullSeFaltarNome() {
        assertNull(MyEventsManager.mapearDocumentoParaInscricao(emptyMap()))
    }

    @Test
    fun mapearListaInscricoesFiltraNulos() {
        val doc1 = mapOf("nomeEvento" to "E1", "dataEvento" to "10/09/2026", "horarioEvento" to "10:00", "localEvento" to "L1")
        val doc2 = mapOf<String, Any?>()

        val lista = MyEventsManager.mapearListaInscricoes(listOf(doc1, doc2))
        assertEquals(1, lista.size)
        assertEquals("E1", lista[0].nome)
    }

    @Test
    fun criarDadosInscricaoGeraMapCorreto() {
        val map = MyEventsManager.criarDadosInscricao("e1", "E1", "10/09/2026", "10:00", "L1", "u1", "u1@email.com")
        assertEquals("e1", map["idEvento"])
        assertEquals("u1", map["usuarioId"])
    }
}
