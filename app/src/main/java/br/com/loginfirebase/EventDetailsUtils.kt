package br.com.loginfirebase

object EventDetailsUtils {

    fun calcularMediaAvaliacoes(notas: List<Int>): Double {
        if (notas.isEmpty()) return 0.0
        return notas.average()
    }

    fun formatarMediaTexto(media: Double, total: Int): String {
        if (total == 0 || media <= 0.0) {
            return "Ainda não existem avaliações."
        }
        return String.format(java.util.Locale.US, "Média %.1f · %d avaliações", media, total)
    }

    fun podeAvaliarEvento(eventoEncerrado: Boolean, inscrito: Boolean): Boolean {
        return eventoEncerrado && inscrito
    }

    fun notaValida(nota: Int): Boolean {
        return nota in 1..5
    }

    fun formatarDestinoMapa(local: String, endereco: String): String {
        val localTratado = local.trim()
        val enderecoTratado = endereco.trim()
        return if (enderecoTratado.isNotEmpty()) {
            "$localTratado, $enderecoTratado"
        } else {
            "$localTratado, Santiago, Rio Grande do Sul, Brasil"
        }
    }
}
