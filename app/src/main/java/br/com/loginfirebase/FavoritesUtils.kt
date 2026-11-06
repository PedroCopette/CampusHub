package br.com.loginfirebase

object FavoritesUtils {

    fun estaNosFavoritos(favoritos: List<FavoritoEvento>, idEvento: String): Boolean {
        return favoritos.any { it.id == idEvento }
    }

    fun formatarContadorFavoritos(quantidade: Int): String {
        return when (quantidade) {
            0 -> "Nenhum evento salvo"
            1 -> "1 evento salvo"
            else -> "$quantidade eventos salvos"
        }
    }

    fun filtrarFavoritosPorNome(favoritos: List<FavoritoEvento>, busca: String): List<FavoritoEvento> {
        val termo = busca.trim()
        if (termo.isEmpty()) return favoritos
        return favoritos.filter { it.nome.contains(termo, ignoreCase = true) }
    }
}
