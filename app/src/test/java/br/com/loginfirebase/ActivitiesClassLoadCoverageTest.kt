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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ActivitiesClassLoadCoverageTest {

    @Test
    fun carregarETestarMainActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.MainActivity")
        assertNotNull(clazz)
        assertTrue(MainActivity.validarLogin("pedro@email.com", "123456"))
    }

    @Test
    fun carregarETestarRegistrationActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.RegistrationActivity")
        assertNotNull(clazz)
        assertTrue(RegistrationActivity.validarCadastro("Pedro", "pedro@email.com", "123456"))
    }

    @Test
    fun carregarETestarRecoveryActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.RecoveryActivity")
        assertNotNull(clazz)
        assertTrue(RecoveryActivity.validarEmailRecuperacao("pedro@email.com"))
    }

    @Test
    fun carregarETestarDashboardActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.DashboardActivity")
        assertNotNull(clazz)
        assertEquals("ÁREA DO ALUNO", DashboardActivity.obterTituloArea())
        assertEquals("EventsActivity", DashboardActivity.tratarCliqueOpcao("Eventos"))
    }

    @Test
    fun carregarETestarEventsActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.EventsActivity")
        assertNotNull(clazz)
        assertTrue(EventsActivity.obterEnderecoPadrao("semana_academica").contains("Santiago"))
    }

    @Test
    fun carregarETestarEventDetailsActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.EventDetailsActivity")
        assertNotNull(clazz)
        assertTrue(EventDetailsActivity.formatarAvisoAvaliacao(4.5, 10).contains("Média"))
    }

    @Test
    fun carregarETestarProfileActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.ProfileActivity")
        assertNotNull(clazz)
        assertEquals("Pedro", ProfileActivity.formatarNome("Pedro", "pedro@email.com"))
    }

    @Test
    fun carregarETestarEditProfileActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.EditProfileActivity")
        assertNotNull(clazz)
        assertTrue(EditProfileActivity.validarEdicao("Pedro Copette"))
    }

    @Test
    fun carregarETestarFavoritesActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.FavoritesActivity")
        assertNotNull(clazz)
        assertEquals("1 evento salvo", FavoritesActivity.formatarTextoLista(1))
    }

    @Test
    fun carregarETestarMyEventsActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.MyEventsActivity")
        assertNotNull(clazz)
        assertEquals("1 inscrição realizada", MyEventsActivity.formatarTextoInscricoes(1))
    }

    @Test
    fun carregarETestarEventPhotosActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.EventPhotosActivity")
        assertNotNull(clazz)
        assertEquals("evento_1", EventPhotosActivity.sanitizarId("evento/1"))
    }

    @Test
    fun carregarETestarEventAudioActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.EventAudioActivity")
        assertNotNull(clazz)
        assertEquals("evento_2", EventAudioActivity.sanitizarId("evento/2"))
    }

    @Test
    fun carregarETestarEventVideoActivity() {
        val clazz = Class.forName("br.com.loginfirebase.activities.EventVideoActivity")
        assertNotNull(clazz)
        assertEquals("evento_3", EventVideoActivity.sanitizarId("evento/3"))
    }
}
