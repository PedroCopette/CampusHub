package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MyEventsUtilsTest {

    private val eventos = listOf(
        Evento("e1", "Semana Acadêmica", "20/09/2026", "19:00", "URI"),
        Evento("e2", "Hackathon", "10/09/2026", "08:00", "Lab TI")
    )

    @Test
    fun formatarContadorInscricoesFormataTextoPluralESingular() {
        assertEquals("Nenhuma inscrição realizada", MyEventsUtils.formatarContadorInscricoes(0))
        assertEquals("1 inscrição realizada", MyEventsUtils.formatarContadorInscricoes(1))
        assertEquals("3 inscrições realizadas", MyEventsUtils.formatarContadorInscricoes(3))
    }

    @Test
    fun ordenarInscricoesPorDataOrdenaCronologicamente() {
        val ordenados = MyEventsUtils.ordenarInscricoesPorData(eventos)
        assertEquals("Hackathon", ordenados[0].nome)
        assertEquals("Semana Acadêmica", ordenados[1].nome)
    }

    @Test
    fun estaInscritoVerificaPresencaNaLista() {
        assertTrue(MyEventsUtils.estaInscrito(eventos, "e1"))
        assertFalse(MyEventsUtils.estaInscrito(eventos, "e99"))
    }
}
