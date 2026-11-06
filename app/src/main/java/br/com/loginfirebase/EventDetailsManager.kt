package br.com.loginfirebase

object EventDetailsManager {

    fun mapearDocumentoParaComentario(id: String, dados: Map<String, Any?>): Comentario? {
        val texto = dados["texto"] as? String ?: return null
        val usuarioId = dados["usuarioId"] as? String ?: return null
        val autorNome = dados["autorNome"] as? String ?: return null
        val dataPublicacao = dados["dataPublicacao"] as? String ?: return null

        return Comentario(
            id = id,
            texto = texto,
            usuarioId = usuarioId,
            autorNome = autorNome,
            dataPublicacao = dataPublicacao
        )
    }

    fun mapearListaComentarios(documentos: List<Pair<String, Map<String, Any?>>>): List<Comentario> {
        return documentos.mapNotNull { (id, dados) ->
            mapearDocumentoParaComentario(id, dados)
        }
    }

    fun calcularMediaEQuantidade(notas: List<Long>): Pair<Double, Int> {
        if (notas.isEmpty()) return Pair(0.0, 0)
        val media = notas.map { it.toDouble() }.average()
        return Pair(media, notas.size)
    }

    fun criarDadosComentario(
        idEvento: String,
        nomeEvento: String,
        texto: String,
        usuarioId: String,
        autorNome: String,
        dataAtual: String
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to idEvento,
            "nomeEvento" to nomeEvento,
            "texto" to texto,
            "usuarioId" to usuarioId,
            "autorNome" to autorNome,
            "dataPublicacao" to dataAtual
        )
    }

    fun criarDadosAvaliacao(
        idEvento: String,
        usuarioId: String,
        usuarioEmail: String?,
        nota: Int
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to idEvento,
            "usuarioId" to usuarioId,
            "usuarioEmail" to (usuarioEmail ?: ""),
            "nota" to nota
        )
    }
}
