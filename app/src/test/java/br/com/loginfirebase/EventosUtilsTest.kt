package br.com.loginfirebase

import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventosUtilsTest {

    private val formato = SimpleDateFormat(
        "dd/MM/yyyy HH:mm",
        Locale.getDefault()
    )

    private fun criarData(data: String): Date {
        return formato.parse(data)!!
    }

    private val agora = criarData("04/11/2026 19:00")

    private fun criarEvento(
        id: String,
        nome: String,
        data: String,
        horario: String,
        categoria: String
    ): EventoBusca {
        return EventoBusca(
            id = id,
            nome = nome,
            data = data,
            horario = horario,
            local = "URI Santiago",
            descricao = "Evento de teste",
            categoria = categoria,
            endereco = "Santiago - RS"
        )
    }

    private val eventos = listOf(
        criarEvento(
            "1",
            "Semana Acadêmica",
            "03/11/2026",
            "19:00",
            "Acadêmico"
        ),
        criarEvento(
            "2",
            "Hackathon CampusHub",
            "10/11/2026",
            "08:00",
            "Tecnologia"
        ),
        criarEvento(
            "3",
            "Palestra de Tecnologia",
            "15/11/2026",
            "20:00",
            "Tecnologia"
        )
    )

    // Testes para eventoEncerrado

    @Test
    fun eventoEncerrado_deveRetornarTrueParaEventoPassado() {
        val resultado = EventosUtils.eventoEncerrado(
            "03/11/2026",
            "18:00",
            agora
        )

        assertTrue(resultado)
    }

    @Test
    fun eventoEncerrado_deveRetornarFalseParaEventoFuturo() {
        val resultado = EventosUtils.eventoEncerrado(
            "05/11/2026",
            "18:00",
            agora
        )

        assertFalse(resultado)
    }

    @Test
    fun eventoEncerrado_deveRetornarFalseParaDataInvalida() {
        val resultado = EventosUtils.eventoEncerrado(
            "data inválida",
            "horário inválido",
            agora
        )

        assertFalse(resultado)
    }

    @Test
    fun eventoEncerrado_deveRetornarFalseParaEventoNoHorarioAtual() {
        val resultado = EventosUtils.eventoEncerrado(
            "04/11/2026",
            "19:00",
            agora
        )

        assertFalse(resultado)
    }

    @Test
    fun eventoEncerrado_deveRetornarFalseParaHorarioInvalido() {
        val resultado = EventosUtils.eventoEncerrado(
            "04/11/2026",
            "horário inválido",
            agora
        )

        assertFalse(resultado)
    }

    // Testes para ordenarData

    @Test
    fun ordenarData_deveConverterDataCorretamente() {
        val resultado = EventosUtils.ordenarData("04/11/2026")

        val esperado = SimpleDateFormat(
            "dd/MM/yyyy",
            Locale.getDefault()
        ).parse("04/11/2026")!!.time

        assertEquals(esperado, resultado)
    }

    @Test
    fun ordenarData_deveRetornarLongMaxValueParaDataInvalida() {
        val resultado = EventosUtils.ordenarData("data inválida")

        assertEquals(Long.MAX_VALUE, resultado)
    }

    @Test
    fun ordenarData_devePermitirOrdenacaoCronologica() {
        val dataAnterior = EventosUtils.ordenarData("03/11/2026")
        val dataPosterior = EventosUtils.ordenarData("10/11/2026")

        assertTrue(dataAnterior < dataPosterior)
    }

    // Testes para filtrarEventos

    @Test
    fun filtrarEventos_deveFiltrarPorNome() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "Hackathon",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertEquals(1, resultado.size)
        assertEquals("Hackathon CampusHub", resultado[0].nome)
    }

    @Test
    fun filtrarEventos_deveIgnorarMaiusculasEMinusculas() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "hackathon",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertEquals(1, resultado.size)
    }

    @Test
    fun filtrarEventos_deveFiltrarPorCategoria() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "",
            categoriaSelecionada = "Tecnologia",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertEquals(2, resultado.size)
    }

    @Test
    fun filtrarEventos_deveFiltrarEventosProximos() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Próximos",
            agora = agora
        )

        assertEquals(2, resultado.size)
    }

    @Test
    fun filtrarEventos_deveFiltrarEventosEncerrados() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Encerrados",
            agora = agora
        )

        assertEquals(1, resultado.size)
        assertEquals("Semana Acadêmica", resultado[0].nome)
    }

    @Test
    fun filtrarEventos_deveCombinarNomeCategoriaESituacao() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "Tecnologia",
            categoriaSelecionada = "Tecnologia",
            situacaoSelecionada = "Próximos",
            agora = agora
        )

        assertEquals(1, resultado.size)
        assertEquals("Palestra de Tecnologia", resultado[0].nome)
    }

    // Testes complementares

    @Test
    fun filtrarEventos_deveRetornarListaVaziaParaBuscaInexistente() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "Evento inexistente",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun filtrarEventos_deveRetornarListaVaziaParaCategoriaInexistente() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "",
            categoriaSelecionada = "Categoria inexistente",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun filtrarEventos_deveRetornarTodosParaSituacaoTodos() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertEquals(3, resultado.size)
    }

    @Test
    fun filtrarEventos_deveRetornarTodosParaBuscaVazia() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertEquals(eventos.size, resultado.size)
    }

    @Test
    fun filtrarEventos_deveRetornarListaVaziaQuandoNaoExistemEventos() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = emptyList(),
            busca = "",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )

        assertTrue(resultado.isEmpty())
    }

    @Test
    fun filtrarEventos_deveRetornarListaVaziaParaFiltrosCombinadosSemCorrespondencia() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "Hackathon",
            categoriaSelecionada = "Acadêmico",
            situacaoSelecionada = "Próximos",
            agora = agora
        )

        assertTrue(resultado.isEmpty())
    }

    // Testes de borda adicionais para EventosUtils

    @Test
    fun ordenarData_deveRetornarLongMaxValueParaStringVazia() {
        val resultado = EventosUtils.ordenarData("")
        assertEquals(Long.MAX_VALUE, resultado)
    }

    @Test
    fun eventoEncerrado_deveRetornarTrueParaHorarioAnteriorNoMesmoDia() {
        val resultado = EventosUtils.eventoEncerrado(
            "04/11/2026",
            "18:59",
            agora
        )
        assertTrue(resultado)
    }

    @Test
    fun filtrarEventos_deveFiltrarPorBuscaParcialNoNome() {
        val resultado = EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = "Palestra",
            categoriaSelecionada = "Todas",
            situacaoSelecionada = "Todos",
            agora = agora
        )
        assertEquals(1, resultado.size)
        assertEquals("Palestra de Tecnologia", resultado[0].nome)
    }
}