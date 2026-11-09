package br.com.loginfirebase

import java.net.URLEncoder

object MapNavigationManager {

    private fun codificarUrl(texto: String): String {
        return try {
            URLEncoder.encode(texto, "UTF-8")
        } catch (e: Exception) {
            texto
        }
    }

    fun gerarUriGoogleMaps(local: String, endereco: String): String {
        val origem = codificarUrl("Santiago, Rio Grande do Sul, Brasil")
        val destinoText = if (endereco.isNotBlank()) {
            "$local, $endereco"
        } else {
            "$local, Santiago, Rio Grande do Sul, Brasil"
        }
        val destinoCodificado = codificarUrl(destinoText)

        return "https://www.google.com/maps/dir/?api=1" +
                "&origin=$origem" +
                "&destination=$destinoCodificado" +
                "&travelmode=driving"
    }
}
