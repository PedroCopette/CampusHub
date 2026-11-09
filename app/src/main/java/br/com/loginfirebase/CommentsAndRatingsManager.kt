package br.com.loginfirebase

object CommentsAndRatingsManager {

    fun validarComentarioTexto(texto: String): Pair<Boolean, String> {
        val textoTratado = texto.trim()
        if (textoTratado.isEmpty()) {
            return Pair(false, "Digite um comentário.")
        }
        return Pair(true, "OK")
    }

    fun prepararDadosComentario(
        eventId: String,
        eventName: String,
        texto: String,
        userId: String,
        autorNome: String,
        dataAtual: String
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to eventId,
            "nomeEvento" to eventName,
            "texto" to texto.trim(),
            "usuarioId" to userId,
            "autorNome" to autorNome,
            "dataPublicacao" to dataAtual
        )
    }

    fun validarAvaliacao(
        inscrito: Boolean,
        eventoEncerrado: Boolean,
        nota: Int
    ): Pair<Boolean, String> {
        if (!inscrito) {
            return Pair(false, "Você precisa estar inscrito no evento.")
        }
        if (!eventoEncerrado) {
            return Pair(false, "O evento ainda não foi encerrado.")
        }
        if (nota !in 1..5) {
            return Pair(false, "Selecione uma nota de 1 a 5.")
        }
        return Pair(true, "OK")
    }

    fun prepararDadosAvaliacao(
        eventId: String,
        userId: String,
        userEmail: String?,
        nota: Int
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to eventId,
            "usuarioId" to userId,
            "usuarioEmail" to (userEmail ?: ""),
            "nota" to nota
        )
    }

    fun calcularMedia(notas: List<Long>): Pair<Double, Int> {
        if (notas.isEmpty()) return Pair(0.0, 0)
        val media = notas.map { it.toDouble() }.average()
        return Pair(media, notas.size)
    }
}
