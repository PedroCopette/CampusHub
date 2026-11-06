package br.com.loginfirebase

object MediaManager {

    private fun extrairCampoJson(jsonObjeto: String, chave: String): String {
        val padrao = "\"$chave\"\\s*:\\s*\"([^\"]*)\"".toRegex()
        return padrao.find(jsonObjeto)?.groupValues?.get(1) ?: ""
    }

    private fun extrairObjetosJson(jsonTexto: String): List<String> {
        if (jsonTexto.isBlank() || !jsonTexto.trim().startsWith("[")) return emptyList()
        val padrao = "\\{[^{}]*\\}".toRegex()
        return padrao.findAll(jsonTexto).map { it.value }.toList()
    }

    fun parseFotosJson(jsonTexto: String): List<FotoPublicada> {
        val objetos = extrairObjetosJson(jsonTexto)
        return objetos.mapNotNull { obj ->
            val id = extrairCampoJson(obj, "id")
            val arquivo = extrairCampoJson(obj, "arquivo")
            val autor = extrairCampoJson(obj, "autor")
            val usuarioId = extrairCampoJson(obj, "usuarioId")
            val data = extrairCampoJson(obj, "dataPublicacao")

            if (id.isNotBlank() && arquivo.isNotBlank() && autor.isNotBlank() && usuarioId.isNotBlank() && data.isNotBlank()) {
                FotoPublicada(id, arquivo, autor, usuarioId, data)
            } else null
        }
    }

    fun serializarFotosJson(fotos: List<FotoPublicada>): String {
        val itens = fotos.joinToString(",") { foto ->
            """{"id":"${foto.id}","arquivo":"${foto.arquivo}","autor":"${foto.autor}","usuarioId":"${foto.usuarioId}","dataPublicacao":"${foto.dataPublicacao}"}"""
        }
        return "[$itens]"
    }

    fun parseAudiosJson(jsonTexto: String): List<AudioPublicado> {
        val objetos = extrairObjetosJson(jsonTexto)
        return objetos.mapNotNull { obj ->
            val id = extrairCampoJson(obj, "id")
            val arquivo = extrairCampoJson(obj, "arquivo")
            val autor = extrairCampoJson(obj, "autor")
            val usuarioId = extrairCampoJson(obj, "usuarioId")
            val data = extrairCampoJson(obj, "dataPublicacao")

            if (id.isNotBlank() && arquivo.isNotBlank() && autor.isNotBlank() && usuarioId.isNotBlank() && data.isNotBlank()) {
                AudioPublicado(id, arquivo, autor, usuarioId, data)
            } else null
        }
    }

    fun serializarAudiosJson(audios: List<AudioPublicado>): String {
        val itens = audios.joinToString(",") { audio ->
            """{"id":"${audio.id}","arquivo":"${audio.arquivo}","autor":"${audio.autor}","usuarioId":"${audio.usuarioId}","dataPublicacao":"${audio.dataPublicacao}"}"""
        }
        return "[$itens]"
    }

    fun parseVideosJson(jsonTexto: String): List<VideoPublicado> {
        val objetos = extrairObjetosJson(jsonTexto)
        return objetos.mapNotNull { obj ->
            val id = extrairCampoJson(obj, "id")
            val arquivo = extrairCampoJson(obj, "arquivo")
            val autor = extrairCampoJson(obj, "autor")
            val usuarioId = extrairCampoJson(obj, "usuarioId")
            val data = extrairCampoJson(obj, "dataPublicacao")

            if (id.isNotBlank() && arquivo.isNotBlank() && autor.isNotBlank() && usuarioId.isNotBlank() && data.isNotBlank()) {
                VideoPublicado(id, arquivo, autor, usuarioId, data)
            } else null
        }
    }

    fun serializarVideosJson(videos: List<VideoPublicado>): String {
        val itens = videos.joinToString(",") { video ->
            """{"id":"${video.id}","arquivo":"${video.arquivo}","autor":"${video.autor}","usuarioId":"${video.usuarioId}","dataPublicacao":"${video.dataPublicacao}"}"""
        }
        return "[$itens]"
    }
}
