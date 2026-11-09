package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DataModelsTest {

    @Test
    fun testarFavoritoEventoDataClass() {
        val f1 = FavoritoEvento("1", "Semana Acadêmica", "20/09/2026", "19:00", "URI")
        val f2 = FavoritoEvento("1", "Semana Acadêmica", "20/09/2026", "19:00", "URI")
        val f3 = f1.copy(nome = "Outro Evento")

        assertEquals(f1, f2)
        assertEquals(f1.hashCode(), f2.hashCode())
        assertNotEquals(f1, f3)
        assertEquals("1", f1.id)
        assertEquals("Semana Acadêmica", f1.nome)
        assertEquals("20/09/2026", f1.data)
        assertEquals("19:00", f1.horario)
        assertEquals("URI", f1.local)
        assertNotNull(f1.toString())
    }

    @Test
    fun testarEventoDataClass() {
        val e1 = Evento("1", "Hackathon", "25/09/2026", "08:00", "Lab TI")
        val e2 = Evento("1", "Hackathon", "25/09/2026", "08:00", "Lab TI")
        val e3 = e1.copy(nome = "Palestra")

        assertEquals(e1, e2)
        assertEquals(e1.hashCode(), e2.hashCode())
        assertNotEquals(e1, e3)
        assertEquals("1", e1.id)
        assertEquals("Hackathon", e1.nome)
        assertEquals("25/09/2026", e1.data)
        assertEquals("08:00", e1.horario)
        assertEquals("Lab TI", e1.local)
        assertNotNull(e1.toString())
    }

    @Test
    fun testarComentarioDataClass() {
        val c1 = Comentario("c1", "Ótimo evento!", "u1", "Pedro", "20/09/2026 19:30")
        val c2 = Comentario("c1", "Ótimo evento!", "u1", "Pedro", "20/09/2026 19:30")
        val c3 = c1.copy(texto = "Comentário editado")

        assertEquals(c1, c2)
        assertEquals(c1.hashCode(), c2.hashCode())
        assertNotEquals(c1, c3)
        assertEquals("c1", c1.id)
        assertEquals("Ótimo evento!", c1.texto)
        assertEquals("u1", c1.usuarioId)
        assertEquals("Pedro", c1.autorNome)
        assertEquals("20/09/2026 19:30", c1.dataPublicacao)
        assertNotNull(c1.toString())
    }

    @Test
    fun testarFotoPublicadaDataClass() {
        val fp1 = FotoPublicada("f1", "foto1.jpg", "Pedro", "u1", "20/09/2026 19:00")
        val fp2 = FotoPublicada("f1", "foto1.jpg", "Pedro", "u1", "20/09/2026 19:00")
        val fp3 = fp1.copy(arquivo = "foto2.jpg")

        assertEquals(fp1, fp2)
        assertEquals(fp1.hashCode(), fp2.hashCode())
        assertNotEquals(fp1, fp3)
        assertEquals("f1", fp1.id)
        assertEquals("foto1.jpg", fp1.arquivo)
        assertEquals("Pedro", fp1.autor)
        assertEquals("u1", fp1.usuarioId)
        assertEquals("20/09/2026 19:00", fp1.dataPublicacao)
        assertNotNull(fp1.toString())
    }

    @Test
    fun testarAudioPublicadoDataClass() {
        val ap1 = AudioPublicado("a1", "audio1.m4a", "Pedro", "u1", "20/09/2026 19:00")
        val ap2 = AudioPublicado("a1", "audio1.m4a", "Pedro", "u1", "20/09/2026 19:00")
        val ap3 = ap1.copy(arquivo = "audio2.m4a")

        assertEquals(ap1, ap2)
        assertEquals(ap1.hashCode(), ap2.hashCode())
        assertNotEquals(ap1, ap3)
        assertEquals("a1", ap1.id)
        assertEquals("audio1.m4a", ap1.arquivo)
        assertEquals("Pedro", ap1.autor)
        assertEquals("u1", ap1.usuarioId)
        assertEquals("20/09/2026 19:00", ap1.dataPublicacao)
        assertNotNull(ap1.toString())
    }

    @Test
    fun testarVideoPublicadoDataClass() {
        val vp1 = VideoPublicado("v1", "video1.mp4", "Pedro", "u1", "20/09/2026 19:00")
        val vp2 = VideoPublicado("v1", "video1.mp4", "Pedro", "u1", "20/09/2026 19:00")
        val vp3 = vp1.copy(arquivo = "video2.mp4")

        assertEquals(vp1, vp2)
        assertEquals(vp1.hashCode(), vp2.hashCode())
        assertNotEquals(vp1, vp3)
        assertEquals("v1", vp1.id)
        assertEquals("video1.mp4", vp1.arquivo)
        assertEquals("Pedro", vp1.autor)
        assertEquals("u1", vp1.usuarioId)
        assertEquals("20/09/2026 19:00", vp1.dataPublicacao)
        assertNotNull(vp1.toString())
    }
}
