package br.com.loginfirebase

object EventDetailsActivityHelper {

    fun processarComentarios(documentos: List<Pair<String, Map<String, Any?>>>): List<Comentario> {
        return EventDetailsManager.mapearListaComentarios(documentos)
    }

    fun calcularMediaAvaliacoes(notas: List<Long>): Pair<Double, Int> {
        return EventDetailsManager.calcularMediaEQuantidade(notas)
    }

    fun formatarMediaAviso(media: Double, total: Int): String {
        return EventDetailsUtils.formatarMediaTexto(media, total)
    }

    fun validarComentario(texto: String): Pair<Boolean, String> {
        return CommentsAndRatingsManager.validarComentarioTexto(texto)
    }

    fun validarAvaliacao(inscrito: Boolean, encerrado: Boolean, nota: Int): Pair<Boolean, String> {
        return CommentsAndRatingsManager.validarAvaliacao(inscrito, encerrado, nota)
    }

    fun gerarUriMapa(local: String, endereco: String): String {
        return MapNavigationManager.gerarUriGoogleMaps(local, endereco)
    }
}
