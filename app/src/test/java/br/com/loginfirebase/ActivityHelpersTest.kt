package br.com.loginfirebase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivityHelpersTest {

    @Test
    fun authActivityHelperValidaEntradasEMensagens() {
        val (loginVal, _) = AuthActivityHelper.validarLoginInput("pedro@email.com", "123456")
        assertTrue(loginVal)

        val (cadVal, _) = AuthActivityHelper.validarCadastroInput("Pedro", "pedro@email.com", "123456")
        assertTrue(cadVal)

        val (recVal, _) = AuthActivityHelper.validarRecuperacaoInput("pedro@email.com")
        assertTrue(recVal)

        assertEquals("Login realizado com sucesso!", AuthActivityHelper.mensagemSucessoLogin())
        assertEquals("Conta criada com sucesso!", AuthActivityHelper.mensagemSucessoCadastro())
        assertEquals("E-mail de recuperação enviado!", AuthActivityHelper.mensagemSucessoRecuperacao())
    }

    @Test
    fun eventsActivityHelperProcessaEFiltrar() {
        assertEquals("Av. Batista Bonotto Sobrinho, 733, Santiago - RS, Brasil", EventsActivityHelper.obterEnderecoPadrao("semana_academica"))
        assertEquals(3, EventsActivityHelper.obterEventosIniciais().size)

        val doc1 = Pair("e1", mapOf("nomeEvento" to "A", "dataEvento" to "20/09/2026", "horarioEvento" to "19:00", "localEvento" to "L", "descricaoEvento" to "D", "categoria" to "C"))
        val lista = EventsActivityHelper.processarDocumentosEventos(listOf(doc1))
        assertEquals(1, lista.size)

        val filtrada = EventsActivityHelper.filtrarListaEventos(lista, "A", "Todas", "Todos")
        assertEquals(1, filtrada.size)
    }

    @Test
    fun favoritesActivityHelperProcessaECria() {
        val favDoc = mapOf("idEvento" to "e1", "nomeEvento" to "E1", "dataEvento" to "20/09/2026", "horarioEvento" to "19:00", "localEvento" to "L")
        val lista = FavoritesActivityHelper.processarDocumentosFavoritos(listOf(favDoc))
        assertEquals(1, lista.size)

        val dadosFav = FavoritesActivityHelper.criarDocumentoFavorito("e1", "E1", "20/09", "19:00", "L", "u1", "u1@email.com")
        assertEquals("e1", dadosFav["idEvento"])

        assertEquals("u1_e1", FavoritesActivityHelper.idDocumentoFavorito("u1", "e1"))
        assertEquals("1 evento salvo", FavoritesActivityHelper.formatarContador(1))
    }

    @Test
    fun myEventsActivityHelperProcessaECria() {
        val insDoc = mapOf("nomeEvento" to "E1", "dataEvento" to "20/09/2026", "horarioEvento" to "19:00", "localEvento" to "L")
        val lista = MyEventsActivityHelper.processarDocumentosInscricoes(listOf(insDoc))
        assertEquals(1, lista.size)

        val dadosIns = MyEventsActivityHelper.criarDocumentoInscricao("e1", "E1", "20/09", "19:00", "L", "u1", "u1@email.com")
        assertEquals("e1", dadosIns["idEvento"])

        assertEquals("u1_e1", MyEventsActivityHelper.idDocumentoInscricao("u1", "e1"))
        assertEquals("1 inscrição realizada", MyEventsActivityHelper.formatarContador(1))
    }

    @Test
    fun eventDetailsActivityHelperProcessaEValida() {
        val comDoc = Pair("c1", mapOf("texto" to "Comentário", "usuarioId" to "u1", "autorNome" to "Pedro", "dataPublicacao" to "20/09"))
        val lista = EventDetailsActivityHelper.processarComentarios(listOf(comDoc))
        assertEquals(1, lista.size)

        val (media, count) = EventDetailsActivityHelper.calcularMediaAvaliacoes(listOf(4L, 5L))
        assertEquals(4.5, media, 0.01)
        assertEquals(2, count)

        assertTrue(EventDetailsActivityHelper.formatarMediaAviso(media, count).contains("Média"))

        val (comVal, _) = EventDetailsActivityHelper.validarComentario("Excelente")
        assertTrue(comVal)

        val (avalVal, _) = EventDetailsActivityHelper.validarAvaliacao(inscrito = true, encerrado = true, nota = 5)
        assertTrue(avalVal)

        val uriMap = EventDetailsActivityHelper.gerarUriMapa("URI", "Rua A")
        assertTrue(uriMap.contains("google.com/maps"))
    }

    @Test
    fun mediaActivityHelperProcessaESerializa() {
        val fotos = listOf(FotoPublicada("f1", "f1.jpg", "Pedro", "u1", "20/09"))
        val jsonFotos = MediaActivityHelper.serializarFotos(fotos)
        val lidasFotos = MediaActivityHelper.processarFotosJson(jsonFotos)
        assertEquals(1, lidasFotos.size)

        val audios = listOf(AudioPublicado("a1", "a1.m4a", "Pedro", "u1", "20/09"))
        val jsonAudios = MediaActivityHelper.serializarAudios(audios)
        val lidosAudios = MediaActivityHelper.processarAudiosJson(jsonAudios)
        assertEquals(1, lidosAudios.size)

        val videos = listOf(VideoPublicado("v1", "v1.mp4", "Pedro", "u1", "20/09"))
        val jsonVideos = MediaActivityHelper.serializarVideos(videos)
        val lidosVideos = MediaActivityHelper.processarVideosJson(jsonVideos)
        assertEquals(1, lidosVideos.size)
    }

    @Test
    fun profileActivityHelperProcessaEValida() {
        val (nome, email) = ProfileActivityHelper.obterPerfilTratado("Pedro", "pedro@email.com")
        assertEquals("Pedro", nome)
        assertEquals("pedro@email.com", email)

        val (valido, _) = ProfileActivityHelper.validarEdicaoNome("Pedro Copette")
        assertTrue(valido)
    }
}
