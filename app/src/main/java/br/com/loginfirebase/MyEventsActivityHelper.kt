package br.com.loginfirebase

object MyEventsActivityHelper {

    fun processarDocumentosInscricoes(documentos: List<Map<String, Any?>>): List<Evento> {
        return MyEventsManager.mapearListaInscricoes(documentos)
    }

    fun criarDocumentoInscricao(
        idEvento: String,
        nomeEvento: String,
        dataEvento: String,
        horarioEvento: String,
        localEvento: String,
        usuarioId: String,
        usuarioEmail: String?
    ): Map<String, Any> {
        return MyEventsManager.criarDadosInscricao(
            idEvento, nomeEvento, dataEvento, horarioEvento, localEvento, usuarioId, usuarioEmail
        )
    }

    fun idDocumentoInscricao(usuarioId: String, idEvento: String): String {
        return "${usuarioId}_$idEvento"
    }

    fun formatarContador(count: Int): String {
        return MyEventsUtils.formatarContadorInscricoes(count)
    }
}
