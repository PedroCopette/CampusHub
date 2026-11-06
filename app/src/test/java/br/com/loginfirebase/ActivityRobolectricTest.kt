package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ActivityRobolectricTest {

    @Test
    fun verificarEStruturaDeChavesDeIntentDeEventos() {
        val id = "semana_academica"
        val nome = "Semana Acadêmica"
        val data = "20/09/2026"
        val horario = "19:00"

        val evento = EventoBusca(
            id = id,
            nome = nome,
            data = data,
            horario = horario,
            local = "Auditório",
            descricao = "Descrição",
            categoria = "Acadêmico",
            endereco = "Santiago - RS"
        )

        assertNotNull(evento)
        assertEquals("semana_academica", evento.id)
        assertEquals("Semana Acadêmica", evento.nome)
    }

    @Test
    fun verificarEStruturaDeStatusDeInscricao() {
        val evento = Evento(
            id = "1",
            nome = "Hackathon",
            data = "25/09/2026",
            horario = "08:00",
            local = "Lab TI"
        )

        assertEquals("1", evento.id)
        assertEquals("Hackathon", evento.nome)
    }

    @Test
    fun verificarEStruturaDeFavorito() {
        val favorito = FavoritoEvento(
            id = "f1",
            nome = "Palestra",
            data = "30/09/2026",
            horario = "20:00",
            local = "Sala 12"
        )

        assertEquals("f1", favorito.id)
        assertEquals("Palestra", favorito.nome)
    }
}
