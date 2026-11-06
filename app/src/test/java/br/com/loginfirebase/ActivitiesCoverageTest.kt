package br.com.loginfirebase

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
    }

    @Test
    fun testarEventDetailsActivityCoverage() {
        val aviso1 = EventDetailsActivity.formatarAvisoAvaliacao(4.5, 10)
        assertTrue(aviso1.contains("Média"))

        val avisoZero = EventDetailsActivity.formatarAvisoAvaliacao(0.0, 0)
        assertTrue(avisoZero.contains("Ainda não existem"))
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
    }

    @Test
    fun testarMyEventsActivityCoverage() {
        assertEquals("Nenhuma inscrição realizada", MyEventsActivity.formatarTextoInscricoes(0))
        assertEquals("1 inscrição realizada", MyEventsActivity.formatarTextoInscricoes(1))
        assertEquals("4 inscrições realizadas", MyEventsActivity.formatarTextoInscricoes(4))
    }

    @Test
    fun testarEventPhotosActivityCoverage() {
        assertEquals("evento_1", EventPhotosActivity.sanitizarId("evento/1"))
    }

    @Test
    fun testarEventAudioActivityCoverage() {
        assertEquals("evento_2", EventAudioActivity.sanitizarId("evento/2"))
    }

    @Test
    fun testarEventVideoActivityCoverage() {
        assertEquals("evento_3", EventVideoActivity.sanitizarId("evento/3"))
    }
}
