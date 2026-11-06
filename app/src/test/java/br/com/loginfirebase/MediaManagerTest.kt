package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaManagerTest {

    @Test
    fun parseESerializarFotosJsonManterDados() {
        val fotos = listOf(FotoPublicada("f1", "foto1.jpg", "Pedro", "u1", "20/09/2026 19:00"))
        val json = MediaManager.serializarFotosJson(fotos)
        val lidas = MediaManager.parseFotosJson(json)

        assertEquals(1, lidas.size)
        assertEquals("f1", lidas[0].id)
        assertEquals("foto1.jpg", lidas[0].arquivo)
    }

    @Test
    fun parseFotosJsonTextoInvalidoRetornaListaVazia() {
        assertTrue(MediaManager.parseFotosJson("").isEmpty())
        assertTrue(MediaManager.parseFotosJson("invalid json").isEmpty())
    }

    @Test
    fun parseESerializarAudiosJsonManterDados() {
        val audios = listOf(AudioPublicado("a1", "audio1.m4a", "Pedro", "u1", "20/09/2026 19:00"))
        val json = MediaManager.serializarAudiosJson(audios)
        val lidos = MediaManager.parseAudiosJson(json)

        assertEquals(1, lidos.size)
        assertEquals("a1", lidos[0].id)
    }

    @Test
    fun parseAudiosJsonTextoInvalidoRetornaListaVazia() {
        assertTrue(MediaManager.parseAudiosJson("").isEmpty())
        assertTrue(MediaManager.parseAudiosJson("invalid json").isEmpty())
    }

    @Test
    fun parseESerializarVideosJsonManterDados() {
        val videos = listOf(VideoPublicado("v1", "video1.mp4", "Pedro", "u1", "20/09/2026 19:00"))
        val json = MediaManager.serializarVideosJson(videos)
        val lidos = MediaManager.parseVideosJson(json)

        assertEquals(1, lidos.size)
        assertEquals("v1", lidos[0].id)
    }

    @Test
    fun parseVideosJsonTextoInvalidoRetornaListaVazia() {
        assertTrue(MediaManager.parseVideosJson("").isEmpty())
        assertTrue(MediaManager.parseVideosJson("invalid json").isEmpty())
    }
}
