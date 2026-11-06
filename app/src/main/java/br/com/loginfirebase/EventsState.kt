package br.com.loginfirebase

data class EventsState(
    val busca: String = "",
    val categoriaSelecionada: String = "Todas",
    val situacaoSelecionada: String = "Todos",
    val carregando: Boolean = false,
    val erro: String? = null,
    val todosEventos: List<EventoBusca> = emptyList()
) {
    fun eventosFiltrados(): List<EventoBusca> {
        return EventosUtils.filtrarEventos(
            eventos = todosEventos,
            busca = busca,
            categoriaSelecionada = categoriaSelecionada,
            situacaoSelecionada = situacaoSelecionada
        )
    }

    fun quantidadeEncontrados(): Int {
        return eventosFiltrados().size
    }
}
