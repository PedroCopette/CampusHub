package br.com.loginfirebase

import br.com.loginfirebase.activities.DashboardActivity
import br.com.loginfirebase.activities.EditProfileActivity
import br.com.loginfirebase.activities.EventAudioActivity
import br.com.loginfirebase.activities.EventDetailsActivity
import br.com.loginfirebase.activities.EventPhotosActivity
import br.com.loginfirebase.activities.EventVideoActivity
import br.com.loginfirebase.activities.EventsActivity
import br.com.loginfirebase.activities.FavoritesActivity
import br.com.loginfirebase.activities.MainActivity
import br.com.loginfirebase.activities.MyEventsActivity
import br.com.loginfirebase.activities.ProfileActivity
import br.com.loginfirebase.activities.RecoveryActivity
import br.com.loginfirebase.activities.RegistrationActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivitiesCoverageTest {

    @Test
    fun testarMainActivityCoverage() {
        assertTrue(MainActivity.validarLogin("pedro@email.com", "123456"))
        assertFalse(MainActivity.validarLogin("", "123456"))
        assertFalse(MainActivity.validarLogin("pedro@email.com", ""))
    }

    @Test
    fun testarRegistrationActivityCoverage() {
        assertTrue(RegistrationActivity.validarCadastro("Pedro", "pedro@email.com", "123456"))
        assertFalse(RegistrationActivity.validarCadastro("", "pedro@email.com", "123456"))
        assertFalse(RegistrationActivity.validarCadastro("Pedro", "", "123456"))
        assertFalse(RegistrationActivity.validarCadastro("Pedro", "pedro@email.com", "123"))
    }

    @Test
    fun testarRecoveryActivityCoverage() {
        assertTrue(RecoveryActivity.validarEmailRecuperacao("pedro@email.com"))
        assertFalse(RecoveryActivity.validarEmailRecuperacao(""))
        assertFalse(RecoveryActivity.validarEmailRecuperacao("emailsemarroba"))
    }

    @Test
    fun testarDashboardActivityCoverage() {
        assertEquals("ÁREA DO ALUNO", DashboardActivity.obterTituloArea())
        assertEquals("EventsActivity", DashboardActivity.tratarCliqueOpcao("Eventos"))
        assertEquals("MyEventsActivity", DashboardActivity.tratarCliqueOpcao("Meus Eventos"))
        assertEquals("FavoritesActivity", DashboardActivity.tratarCliqueOpcao("Meus Favoritos"))
        assertEquals("ProfileActivity", DashboardActivity.tratarCliqueOpcao("Meu Perfil"))
        assertEquals("MainActivity", DashboardActivity.tratarCliqueOpcao("Sair"))
        assertEquals("Desconhecido", DashboardActivity.tratarCliqueOpcao("Outra"))

        val opcoes = DashboardUtils.obterOpcoesMenu()
        assertEquals(5, opcoes.size)
        assertTrue(DashboardUtils.ehOpcaoValida("Eventos"))
    }

    @Test
    fun testarEventsActivityCoverage() {
        val end1 = EventsActivity.obterEnderecoPadrao("semana_academica")
        assertTrue(end1.contains("Batista Bonotto"))

        val end2 = EventsActivity.obterEnderecoPadrao("hackathon_campushub")
        assertTrue(end2.contains("Tio Virgílio"))

        val end3 = EventsActivity.obterEnderecoPadrao("palestra_tecnologia")
        assertTrue(end3.contains("Bento Gonçalves"))

        val endOutro = EventsActivity.obterEnderecoPadrao("desconhecido")
        assertTrue(endOutro.contains("Santiago"))

        val cat = EventsActivity.obterFiltrosCategorias()
        assertEquals(3, cat.size)

        val sit = EventsActivity.obterFiltrosSituacoes()
        assertEquals(3, sit.size)
    }

    @Test
    fun testarEventDetailsActivityCoverage() {
        val aviso1 = EventDetailsActivity.formatarAvisoAvaliacao(4.5, 10)
        assertTrue(aviso1.contains("Média"))

        val avisoZero = EventDetailsActivity.formatarAvisoAvaliacao(0.0, 0)
        assertTrue(avisoZero.contains("Ainda não existem"))

        assertTrue(EventDetailsActivity.validarTextoComentario("Excelente!"))
        assertFalse(EventDetailsActivity.validarTextoComentario("   "))

        assertTrue(EventDetailsActivity.ehAutorDoComentario("u1", "u1"))
        assertFalse(EventDetailsActivity.ehAutorDoComentario("u1", "u2"))
        assertFalse(EventDetailsActivity.ehAutorDoComentario(null, "u1"))
    }

    @Test
    fun testarProfileActivityCoverage() {
        assertEquals("Pedro", ProfileActivity.formatarNome("Pedro", "pedro@email.com"))
        assertEquals("pedro", ProfileActivity.formatarNome("", "pedro@email.com"))
        assertEquals("Estudante CampusHub", ProfileActivity.formatarNome(null, null))
    }

    @Test
    fun testarEditProfileActivityCoverage() {
        assertTrue(EditProfileActivity.validarEdicao("Pedro Copette"))
        assertFalse(EditProfileActivity.validarEdicao("   "))
    }

    @Test
    fun testarFavoritesActivityCoverage() {
        assertEquals("Nenhum evento salvo", FavoritesActivity.formatarTextoLista(0))
        assertEquals("1 evento salvo", FavoritesActivity.formatarTextoLista(1))
        assertEquals("3 eventos salvos", FavoritesActivity.formatarTextoLista(3))

        assertEquals("user1_event1", FavoritesActivity.gerarIdFavorito("user1", "event1"))
    }

    @Test
    fun testarMyEventsActivityCoverage() {
        assertEquals("Nenhuma inscrição realizada", MyEventsActivity.formatarTextoInscricoes(0))
        assertEquals("1 inscrição realizada", MyEventsActivity.formatarTextoInscricoes(1))
        assertEquals("4 inscrições realizadas", MyEventsActivity.formatarTextoInscricoes(4))

        assertTrue(MyEventsActivity.ehUsuarioLogado("user123"))
        assertFalse(MyEventsActivity.ehUsuarioLogado(null))
        assertFalse(MyEventsActivity.ehUsuarioLogado(""))
    }

    @Test
    fun testarEventPhotosActivityCoverage() {
        assertEquals("evento_1", EventPhotosActivity.sanitizarId("evento/1"))
        assertTrue(EventPhotosActivity.ehAutorDaMidia("u1", "u1"))
        assertFalse(EventPhotosActivity.ehAutorDaMidia("u1", "u2"))
    }

    @Test
    fun testarEventAudioActivityCoverage() {
        assertEquals("evento_2", EventAudioActivity.sanitizarId("evento/2"))
        assertEquals("Gravando", EventAudioActivity.validarStatusGravacao(true))
        assertEquals("Parado", EventAudioActivity.validarStatusGravacao(false))
    }

    @Test
    fun testarEventVideoActivityCoverage() {
        assertEquals("evento_3", EventVideoActivity.sanitizarId("evento/3"))
        assertEquals("Reproduzindo", EventVideoActivity.validarStatusVideo(true))
        assertEquals("Pausado", EventVideoActivity.validarStatusVideo(false))
    }
}
