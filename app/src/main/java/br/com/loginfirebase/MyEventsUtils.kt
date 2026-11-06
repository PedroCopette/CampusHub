package br.com.loginfirebase

object MyEventsUtils {

    fun formatarContadorInscricoes(quantidade: Int): String {
        return when (quantidade) {
            0 -> "Nenhuma inscrição realizada"
            1 -> "1 inscrição realizada"
            else -> "$quantidade inscrições realizadas"
        }
    }

    fun ordenarInscricoesPorData(eventos: List<Evento>): List<Evento> {
        return eventos.sortedBy { EventosUtils.ordenarData(it.data) }
    }

    fun estaInscrito(eventos: List<Evento>, idEvento: String): Boolean {
        return eventos.any { it.id == idEvento }
    }
}
