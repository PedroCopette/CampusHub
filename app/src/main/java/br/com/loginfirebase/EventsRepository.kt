package br.com.loginfirebase

object EventsRepository {

    fun gerarIdInscricao(userId: String, eventId: String): String {
        return "${userId}_$eventId"
    }

    fun gerarIdFavorito(userId: String, eventId: String): String {
        return "${userId}_$eventId"
    }

    fun verificarEventoInscrito(listaInscricoes: List<Evento>, eventId: String): Boolean {
        return listaInscricoes.any { it.id == eventId }
    }

    fun verificarEventoFavoritado(listaFavoritos: List<FavoritoEvento>, eventId: String): Boolean {
        return listaFavoritos.any { it.id == eventId }
    }

    fun prepararDadosInscricao(
        userId: String,
        userEmail: String?,
        eventId: String,
        nome: String,
        data: String,
        horario: String,
        local: String
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to eventId,
            "nomeEvento" to nome,
            "dataEvento" to data,
            "horarioEvento" to horario,
            "localEvento" to local,
            "usuarioId" to userId,
            "usuarioEmail" to (userEmail ?: "")
        )
    }

    fun prepararDadosFavorito(
        userId: String,
        userEmail: String?,
        eventId: String,
        nome: String,
        data: String,
        horario: String,
        local: String
    ): Map<String, Any> {
        return hashMapOf(
            "idEvento" to eventId,
            "nomeEvento" to nome,
            "dataEvento" to data,
            "horarioEvento" to horario,
            "localEvento" to local,
            "usuarioId" to userId,
            "usuarioEmail" to (userEmail ?: "")
        )
    }
}
