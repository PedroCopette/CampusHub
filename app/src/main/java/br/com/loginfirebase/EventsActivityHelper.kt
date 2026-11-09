package br.com.loginfirebase

object EventsActivityHelper {

    fun obterEnderecoPadrao(id: String): String {
        return EventsManager.obterEnderecoPadrao(id)
    }

    fun obterEventosIniciais(): List<EventoBusca> {
        return EventsManager.obterEventosIniciais()
    }

    fun processarDocumentosEventos(documentos: List<Pair<String, Map<String, Any?>>>): List<EventoBusca> {
        return EventsManager.MapearListaDocumentos(documentos)
    }

    fun filtrarListaEventos(
        eventos: List<EventoBusca>,
        busca: String,
        categoria: String,
        situacao: String
    ): List<EventoBusca> {
        return EventosUtils.filtrarEventos(
            eventos = eventos,
            busca = busca,
            categoriaSelecionada = categoria,
            situacaoSelecionada = situacao
        )
    }
}
