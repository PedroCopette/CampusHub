package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class EventoBuscaTest {

    @Test
    fun criarEventoBuscaComValoresCorretos() {
        val evento = EventoBusca(
            id = "ev1",
            nome = "Semana de Tecnologia",
            data = "12/11/2026",
            horario = "14:00",
            local = "Auditório Central",
            descricao = "Palestra sobre inteligência artificial",
            categoria = "Tecnologia",
            endereco = "Av. Principal, 100"
        )

        assertEquals("ev1", evento.id)
        assertEquals("Semana de Tecnologia", evento.nome)
        assertEquals("12/11/2026", evento.data)
        assertEquals("14:00", evento.horario)
        assertEquals("Auditório Central", evento.local)
        assertEquals("Palestra sobre inteligência artificial", evento.descricao)
        assertEquals("Tecnologia", evento.categoria)
        assertEquals("Av. Principal, 100", evento.endereco)
    }

    @Test
    fun compararEventosIguaisDeveRetornarTrue() {
        val evento1 = EventoBusca(
            id = "ev1",
            nome = "Semana de Tecnologia",
            data = "12/11/2026",
            horario = "14:00",
            local = "Auditório Central",
            descricao = "Palestra sobre IA",
            categoria = "Tecnologia",
            endereco = "Av. Principal, 100"
        )

        val evento2 = EventoBusca(
            id = "ev1",
            nome = "Semana de Tecnologia",
            data = "12/11/2026",
            horario = "14:00",
            local = "Auditório Central",
            descricao = "Palestra sobre IA",
            categoria = "Tecnologia",
            endereco = "Av. Principal, 100"
        )

        assertEquals(evento1, evento2)
        assertEquals(evento1.hashCode(), evento2.hashCode())
    }

    @Test
    fun compararEventosDiferentesDeveRetornarFalse() {
        val evento1 = EventoBusca(
            id = "ev1",
            nome = "Semana de Tecnologia",
            data = "12/11/2026",
            horario = "14:00",
            local = "Auditório Central",
            descricao = "Palestra sobre IA",
            categoria = "Tecnologia",
            endereco = "Av. Principal, 100"
        )

        val evento2 = EventoBusca(
            id = "ev2",
            nome = "Hackathon",
            data = "15/11/2026",
            horario = "09:00",
            local = "Lab 3",
            descricao = "Competição de Dev",
            categoria = "Tecnologia",
            endereco = "Av. Principal, 100"
        )

        assertNotEquals(evento1, evento2)
    }

    @Test
    fun metodoCopyDevePermitirAlterarAtributos() {
        val eventoOriginal = EventoBusca(
            id = "ev1",
            nome = "Semana de Tecnologia",
            data = "12/11/2026",
            horario = "14:00",
            local = "Auditório Central",
            descricao = "Palestra sobre IA",
            categoria = "Tecnologia",
            endereco = "Av. Principal, 100"
        )

        val eventoModificado = eventoOriginal.copy(nome = "Semana de IA e Dev")

        assertEquals("ev1", eventoModificado.id)
        assertEquals("Semana de IA e Dev", eventoModificado.nome)
        assertEquals(eventoOriginal.data, eventoModificado.data)
    }
}
