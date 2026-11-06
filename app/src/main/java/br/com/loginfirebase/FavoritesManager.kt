package br.com.loginfirebase

object FavoritesManager {

    fun mapearDocumentoParaFavorito(dados: Map<String, Any?>): FavoritoEvento? {
        val id = dados["idEvento"] as? String ?: return null
        val nome = dados["nomeEvento"] as? String ?: return null
        val data = dados["dataEvento"] as? String ?: return null
        val horario = dados["horarioEvento"] as? String ?: return null
        val local = dados["localEvento"] as? String ?: return null

        return FavoritoEvento(
            id = id,
            nome = nome,
            data = data,
            horario = horario,
            local = local
        )
    }

    fun mapearListaFavoritos(documentos: List<Map<String, Any?>>): List<FavoritoEvento> {
        return documentos.mapNotNull { mapearDocumentoParaFavorito(it) }
    }

    fun criarDadosFavorito(
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
