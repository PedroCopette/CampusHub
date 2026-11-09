package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class AppRepositoriesTest {

    // AuthRepository

    @Test
    fun authRepositoryTrataResultados() {
        assertEquals("Login realizado com sucesso!", AuthRepository.tratarResultadoLogin(true))
        assertEquals("E-mail ou senha incorretos.", AuthRepository.tratarResultadoLogin(false))

        assertEquals("Conta criada com sucesso!", AuthRepository.tratarResultadoCadastro(true, null))
        assertTrue(AuthRepository.tratarResultadoCadastro(false, "Erro de rede").contains("Erro de rede"))

        assertEquals("E-mail de recuperação enviado!", AuthRepository.tratarResultadoRecuperacao(true))
        assertEquals("Não foi possível enviar o e-mail de recuperação.", AuthRepository.tratarResultadoRecuperacao(false))
    }

    // EventsRepository

    @Test
    fun eventsRepositoryGeraIdsEValidaInscricaoEFavorito() {
        assertEquals("u1_e1", EventsRepository.gerarIdInscricao("u1", "e1"))
        assertEquals("u1_e1", EventsRepository.gerarIdFavorito("u1", "e1"))

        val inscricoes = listOf(Evento("e1", "Evento 1", "20/09/2026", "19:00", "Local 1"))
        assertTrue(EventsRepository.verificarEventoInscrito(inscricoes, "e1"))
        assertFalse(EventsRepository.verificarEventoInscrito(inscricoes, "e2"))

        val favoritos = listOf(FavoritoEvento("e1", "Evento 1", "20/09/2026", "19:00", "Local 1"))
        assertTrue(EventsRepository.verificarEventoFavoritado(favoritos, "e1"))
        assertFalse(EventsRepository.verificarEventoFavoritado(favoritos, "e2"))

        val dadosInscricao = EventsRepository.prepararDadosInscricao("u1", "u1@email.com", "e1", "E1", "20/09", "19:00", "L1")
        assertEquals("u1", dadosInscricao["usuarioId"])

        val dadosFavorito = EventsRepository.prepararDadosFavorito("u1", "u1@email.com", "e1", "E1", "20/09", "19:00", "L1")
        assertEquals("e1", dadosFavorito["idEvento"])
    }

    // CommentsAndRatingsManager

    @Test
    fun commentsAndRatingsManagerValidaEPreparaDados() {
        val (valido, msg) = CommentsAndRatingsManager.validarComentarioTexto("  Ótimo evento!  ")
        assertTrue(valido)
        assertEquals("OK", msg)

        val (invalido, msgInvalido) = CommentsAndRatingsManager.validarComentarioTexto("   ")
        assertFalse(invalido)
        assertEquals("Digite um comentário.", msgInvalido)

        val dadosComentario = CommentsAndRatingsManager.prepararDadosComentario("e1", "E1", "Ótimo", "u1", "Pedro", "20/09")
        assertEquals("Ótimo", dadosComentario["texto"])

        val (validaAvaliacao, msgAvaliacao) = CommentsAndRatingsManager.validarAvaliacao(inscrito = true, eventoEncerrado = true, nota = 5)
        assertTrue(validaAvaliacao)
        assertEquals("OK", msgAvaliacao)

        val (naoInscritoVal, naoInscritoMsg) = CommentsAndRatingsManager.validarAvaliacao(inscrito = false, eventoEncerrado = true, nota = 5)
        assertFalse(naoInscritoVal)
        assertEquals("Você precisa estar inscrito no evento.", naoInscritoMsg)

        val (naoEncerradoVal, naoEncerradoMsg) = CommentsAndRatingsManager.validarAvaliacao(inscrito = true, eventoEncerrado = false, nota = 5)
        assertFalse(naoEncerradoVal)
        assertEquals("O evento ainda não foi encerrado.", naoEncerradoMsg)

        val (notaInvalidaVal, notaInvalidaMsg) = CommentsAndRatingsManager.validarAvaliacao(inscrito = true, eventoEncerrado = true, nota = 0)
        assertFalse(notaInvalidaVal)
        assertEquals("Selecione uma nota de 1 a 5.", notaInvalidaMsg)

        val (media, qtd) = CommentsAndRatingsManager.calcularMedia(listOf(4L, 5L))
        assertEquals(4.5, media, 0.01)
        assertEquals(2, qtd)

        val dadosAvaliacao = CommentsAndRatingsManager.prepararDadosAvaliacao("e1", "u1", "u1@email.com", 5)
        assertEquals(5, dadosAvaliacao["nota"])
    }

    // MediaFileManager

    @Test
    fun mediaFileManagerCriaEPastaEValidaPermissoes() {
        val tempFolder = File(System.getProperty("java.io.tmpdir"), "test_media_dir_${System.currentTimeMillis()}")
        tempFolder.deleteOnExit()

        val pastaFotos = MediaFileManager.obterPastaMidia(tempFolder, "fotos")
        assertNotNull(pastaFotos)

        val arquivoJson = MediaFileManager.obterArquivoJsonPublicacoes(tempFolder, "fotos", "evento/1")
        assertTrue(arquivoJson.name.contains("evento_1.json"))

        val (incluirValido, _) = MediaFileManager.validarInclusaoMidia(true)
        assertTrue(incluirValido)

        val (incluirInvalido, msgIncluir) = MediaFileManager.validarInclusaoMidia(false)
        assertFalse(incluirInvalido)
        assertEquals("Faça login para publicar mídia.", msgIncluir)

        val (excluirValido, _) = MediaFileManager.validarExclusaoMidia("u1", "u1")
        assertTrue(excluirValido)

        val (excluirOutro, msgExcluirOutro) = MediaFileManager.validarExclusaoMidia("u1", "u2")
        assertFalse(excluirOutro)
        assertEquals("Você só pode excluir seus próprios arquivos.", msgExcluirOutro)

        val (excluirSemLogin, msgExcluirSemLogin) = MediaFileManager.validarExclusaoMidia(null, "u1")
        assertFalse(excluirSemLogin)
        assertEquals("Faça login para excluir mídia.", msgExcluirSemLogin)
    }

    // PreferencesManager

    @Test
    fun preferencesManagerPreparaDados() {
        val map = PreferencesManager.prepararDadosPreferencias("  Pedro  ", "  pedro@email.com  ")
        assertEquals("Pedro", map["nome"])
        assertEquals("pedro@email.com", map["email"])
    }

    // MapNavigationManager

    @Test
    fun mapNavigationManagerGeraUriValida() {
        val uri = MapNavigationManager.gerarUriGoogleMaps("URI Santiago", "Av. Batista Bonotto, 733")
        assertTrue(uri.contains("google.com/maps/dir"))
        assertTrue(uri.contains("travelmode=driving"))
    }
}
