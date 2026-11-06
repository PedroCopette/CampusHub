package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EventDetailsUtilsTest {

    @Test
    fun calcularMediaAvaliacoesCalculaCorretamente() {
        val media = EventDetailsUtils.calcularMediaAvaliacoes(listOf(4, 5, 3))
        assertEquals(4.0, media, 0.01)
    }

    @Test
    fun calcularMediaAvaliacoesComListaVaziaRetornaZero() {
        assertEquals(0.0, EventDetailsUtils.calcularMediaAvaliacoes(emptyList()), 0.01)
    }

    @Test
    fun formatarMediaTextoFormatacaoEsperada() {
        assertEquals("Ainda não existem avaliações.", EventDetailsUtils.formatarMediaTexto(0.0, 0))
        assertEquals("Média 4.5 · 10 avaliações", EventDetailsUtils.formatarMediaTexto(4.5, 10))
    }

    @Test
    fun podeAvaliarEventoRetornaTrueApenasSeEncerradoEInscrito() {
        assertTrue(EventDetailsUtils.podeAvaliarEvento(eventoEncerrado = true, inscrito = true))
        assertFalse(EventDetailsUtils.podeAvaliarEvento(eventoEncerrado = false, inscrito = true))
        assertFalse(EventDetailsUtils.podeAvaliarEvento(eventoEncerrado = true, inscrito = false))
    }

    @Test
    fun notaValidaAceitaValoresEntreUmEEscopos() {
        assertTrue(EventDetailsUtils.notaValida(1))
        assertTrue(EventDetailsUtils.notaValida(5))
        assertFalse(EventDetailsUtils.notaValida(0))
        assertFalse(EventDetailsUtils.notaValida(6))
    }

    @Test
    fun formatarDestinoMapaMontaDestinoCorreto() {
        val destino = EventDetailsUtils.formatarDestinoMapa("Auditório", "Rua A, 100")
        assertEquals("Auditório, Rua A, 100", destino)

        val destinoSemEndereco = EventDetailsUtils.formatarDestinoMapa("Auditório", "")
        assertEquals("Auditório, Santiago, Rio Grande do Sul, Brasil", destinoSemEndereco)
    }
}
