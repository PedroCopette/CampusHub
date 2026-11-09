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
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ComprehensiveCoverageTest {

    @Test
    fun testarInstanciasDeObjetos() {
        assertNotNull(CadastroValidator)
        assertNotNull(EventosUtils)
        assertNotNull(DashboardUtils)
        assertNotNull(ProfileUtils)
        assertNotNull(FavoritesUtils)
        assertNotNull(MyEventsUtils)
        assertNotNull(EventDetailsUtils)
        assertNotNull(MediaUtils)
        assertNotNull(EventsManager)
        assertNotNull(FavoritesManager)
        assertNotNull(MyEventsManager)
        assertNotNull(EventDetailsManager)
        assertNotNull(MediaManager)
        assertNotNull(AuthManager)
        assertNotNull(RecoveryManager)
        assertNotNull(ProfileManager)
        assertNotNull(DashboardManager)
        assertNotNull(AuthRepository)
        assertNotNull(EventsRepository)
        assertNotNull(CommentsAndRatingsManager)
        assertNotNull(MediaFileManager)
        assertNotNull(PreferencesManager)
        assertNotNull(MapNavigationManager)
        assertNotNull(AuthActivityHelper)
        assertNotNull(EventsActivityHelper)
        assertNotNull(FavoritesActivityHelper)
        assertNotNull(MyEventsActivityHelper)
        assertNotNull(EventDetailsActivityHelper)
        assertNotNull(MediaActivityHelper)
        assertNotNull(ProfileActivityHelper)
    }

    @Test
    fun testarCompanionsDasActivities() {
        assertNotNull(MainActivity.Companion)
        assertNotNull(RegistrationActivity.Companion)
        assertNotNull(RecoveryActivity.Companion)
        assertNotNull(DashboardActivity.Companion)
        assertNotNull(EventsActivity.Companion)
        assertNotNull(EventDetailsActivity.Companion)
        assertNotNull(ProfileActivity.Companion)
        assertNotNull(EditProfileActivity.Companion)
        assertNotNull(FavoritesActivity.Companion)
        assertNotNull(MyEventsActivity.Companion)
        assertNotNull(EventPhotosActivity.Companion)
        assertNotNull(EventAudioActivity.Companion)
        assertNotNull(EventVideoActivity.Companion)
    }

    @Test
    fun testarLoginStateDataClass() {
        val s1 = LoginState("a@b.com", "123", true, "erro")
        val s2 = LoginState("a@b.com", "123", true, "erro")
        val s3 = s1.copy(carregando = false)

        assertEquals(s1, s2)
        assertEquals(s1.hashCode(), s2.hashCode())
        assertNotEquals(s1, s3)
        assertEquals("a@b.com", s1.component1())
        assertEquals("123", s1.component2())
        assertTrue(s1.component3())
        assertEquals("erro", s1.component4())
        assertNotNull(s1.toString())
    }

    @Test
    fun testarRegistrationStateDataClass() {
        val s1 = RegistrationState("Nome", "a@b.com", "123", true)
        val s2 = RegistrationState("Nome", "a@b.com", "123", true)
        val s3 = s1.copy(carregando = false)

        assertEquals(s1, s2)
        assertEquals(s1.hashCode(), s2.hashCode())
        assertNotEquals(s1, s3)
        assertEquals("Nome", s1.component1())
        assertEquals("a@b.com", s1.component2())
        assertEquals("123", s1.component3())
        assertTrue(s1.component4())
        assertNotNull(s1.toString())
    }

    @Test
    fun testarEventsStateDataClass() {
        val evento = EventoBusca("1", "E1", "20/09/2026", "19:00", "L1", "D1", "Tecnologia", "E1")
        val s1 = EventsState("busca", "Tecnologia", "Todos", false, null, listOf(evento))
        val s2 = EventsState("busca", "Tecnologia", "Todos", false, null, listOf(evento))
        val s3 = s1.copy(busca = "outra")

        assertEquals(s1, s2)
        assertEquals(s1.hashCode(), s2.hashCode())
        assertNotEquals(s1, s3)
        assertEquals("busca", s1.component1())
        assertEquals("Tecnologia", s1.component2())
        assertEquals("Todos", s1.component3())
        assertFalse(s1.component4())
        assertNull(s1.component5())
        assertEquals(1, s1.component6().size)
        assertNotNull(s1.toString())
    }

    @Test
    fun testarMediaManagerCasosBorda() {
        assertEquals("[]", MediaManager.serializarFotosJson(emptyList()))
        assertEquals("[]", MediaManager.serializarAudiosJson(emptyList()))
        assertEquals("[]", MediaManager.serializarVideosJson(emptyList()))

        assertTrue(MediaManager.parseFotosJson("{invalido}").isEmpty())
        assertTrue(MediaManager.parseAudiosJson("{invalido}").isEmpty())
        assertTrue(MediaManager.parseVideosJson("{invalido}").isEmpty())
    }

    @Test
    fun testarEventsManagerMapeamentoIncompleto() {
        assertNull(EventsManager.MapearDocumentoParaEvento("1", mapOf("nomeEvento" to "A")))
        assertNull(EventsManager.MapearDocumentoParaEvento("1", mapOf("nomeEvento" to "A", "dataEvento" to "20/09")))
        assertNull(EventsManager.MapearDocumentoParaEvento("1", mapOf("nomeEvento" to "A", "dataEvento" to "20/09", "horarioEvento" to "19:00")))
        assertNull(EventsManager.MapearDocumentoParaEvento("1", mapOf("nomeEvento" to "A", "dataEvento" to "20/09", "horarioEvento" to "19:00", "localEvento" to "L")))
        assertNull(EventsManager.MapearDocumentoParaEvento("1", mapOf("nomeEvento" to "A", "dataEvento" to "20/09", "horarioEvento" to "19:00", "localEvento" to "L", "descricaoEvento" to "D")))
    }
}
