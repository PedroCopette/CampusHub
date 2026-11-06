package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MediaUtilsTest {

    @Test
    fun sanitizarIdEventoRemoveCaracteresEspeciais() {
        val sanitizado = MediaUtils.sanitizarIdEvento("evento/123#abc")
        assertEquals("evento_123_abc", sanitizado)
    }

    @Test
    fun gerarNomeArquivoPublicacaoMontaNomeValido() {
        val nome = MediaUtils.gerarNomeArquivoPublicacao("evento_1", "fotos", "json")
        assertEquals("publicacoes_fotos_evento_1.json", nome)
    }

    @Test
    fun ehArquivoMediaValidoValidaExtensoesSuportadas() {
        assertTrue(MediaUtils.ehArquivoMediaValido("foto.jpg"))
        assertTrue(MediaUtils.ehArquivoMediaValido("AUDIO.M4A"))
        assertTrue(MediaUtils.ehArquivoMediaValido("video.mp4"))
        assertFalse(MediaUtils.ehArquivoMediaValido("documento.pdf"))
        assertFalse(MediaUtils.ehArquivoMediaValido("script.exe"))
    }
}
