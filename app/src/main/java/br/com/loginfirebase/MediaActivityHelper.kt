package br.com.loginfirebase

object MediaActivityHelper {

    fun processarFotosJson(jsonStr: String): List<FotoPublicada> {
        return MediaManager.parseFotosJson(jsonStr)
    }

    fun serializarFotos(fotos: List<FotoPublicada>): String {
        return MediaManager.serializarFotosJson(fotos)
    }

    fun processarAudiosJson(jsonStr: String): List<AudioPublicado> {
        return MediaManager.parseAudiosJson(jsonStr)
    }

    fun serializarAudios(audios: List<AudioPublicado>): String {
        return MediaManager.serializarAudiosJson(audios)
    }

    fun processarVideosJson(jsonStr: String): List<VideoPublicado> {
        return MediaManager.parseVideosJson(jsonStr)
    }

    fun serializarVideos(videos: List<VideoPublicado>): String {
        return MediaManager.serializarVideosJson(videos)
    }
}
