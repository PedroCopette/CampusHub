package br.com.loginfirebase

object MyEventsManager {

    fun mapearDocumentoParaInscricao(dados: Map<String, Any?>): Evento? {
        val nome = dados["nomeEvento"] as? String ?: return null
        val data = dados["dataEvento"] as? String ?: return null
        val horario = dados["horarioEvento"] as? String ?: return null
        val local = dados["localEvento"] as? String ?: return null

        return Evento(
            nome = nome,
            data = data,
            horario = horario,
            local = local
        )
    }

    fun mapearListaInscricoes(documentos: List<Map<String, Any?>>): List<Evento> {
        return documentos.mapNotNull { mapearDocumentoParaInscricao(it) }
    }

    fun criarDadosInscricao(
        idEvento: String,
        nomeEvento: String,
        dataEvento: String,
        horarioEvento: String,
        localEvento: String,
        usuarioId: String,
        usuarioEmail: String?
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to idEvento,
            "nomeEvento" to nomeEvento,
            "dataEvento" to dataEvento,
            "horarioEvento" to horarioEvento,
            "localEvento" to localEvento,
            "usuarioId" to usuarioId,
            "usuarioEmail" to (usuarioEmail ?: "")
        )
    }
}
