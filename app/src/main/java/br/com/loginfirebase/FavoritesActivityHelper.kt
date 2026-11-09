package br.com.loginfirebase

object FavoritesActivityHelper {

    fun processarDocumentosFavoritos(documentos: List<Map<String, Any?>>): List<FavoritoEvento> {
        return FavoritesManager.mapearListaFavoritos(documentos)
    }

    fun criarDocumentoFavorito(
        idEvento: String,
        nomeEvento: String,
        dataEvento: String,
        horarioEvento: String,
        localEvento: String,
        usuarioId: String,
        usuarioEmail: String?
    ): Map<String, Any> {
        return FavoritesManager.criarDadosFavorito(
            idEvento, nomeEvento, dataEvento, horarioEvento, localEvento, usuarioId, usuarioEmail
        )
    }

    fun idDocumentoFavorito(usuarioId: String, idEvento: String): String {
        return "${usuarioId}_$idEvento"
    }

    fun formatarContador(count: Int): String {
        return FavoritesUtils.formatarContadorFavoritos(count)
    }
}
