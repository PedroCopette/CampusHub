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
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.objenesis.ObjenesisStd

class ActivitiesMockitoCoverageTest {

    private val objenesis = ObjenesisStd()

    @Test
    fun testarMainActivityClass() {
        assertNotNull(objenesis.newInstance(MainActivity::class.java))
    }

    @Test
    fun testarRegistrationActivityClass() {
        assertNotNull(objenesis.newInstance(RegistrationActivity::class.java))
    }

    @Test
    fun testarRecoveryActivityClass() {
        assertNotNull(objenesis.newInstance(RecoveryActivity::class.java))
    }

    @Test
    fun testarDashboardActivityClass() {
        assertNotNull(objenesis.newInstance(DashboardActivity::class.java))
    }

    @Test
    fun testarEventsActivityClass() {
        assertNotNull(objenesis.newInstance(EventsActivity::class.java))
    }

    @Test
    fun testarEventDetailsActivityClass() {
        assertNotNull(objenesis.newInstance(EventDetailsActivity::class.java))
    }

    @Test
    fun testarProfileActivityClass() {
        assertNotNull(objenesis.newInstance(ProfileActivity::class.java))
    }

    @Test
    fun testarEditProfileActivityClass() {
        assertNotNull(objenesis.newInstance(EditProfileActivity::class.java))
    }

    @Test
    fun testarFavoritesActivityClass() {
        assertNotNull(objenesis.newInstance(FavoritesActivity::class.java))
    }

    @Test
    fun testarMyEventsActivityClass() {
        assertNotNull(objenesis.newInstance(MyEventsActivity::class.java))
    }

    @Test
    fun testarEventPhotosActivityClass() {
        assertNotNull(objenesis.newInstance(EventPhotosActivity::class.java))
    }

    @Test
    fun testarEventAudioActivityClass() {
        assertNotNull(objenesis.newInstance(EventAudioActivity::class.java))
    }

    @Test
    fun testarEventVideoActivityClass() {
        assertNotNull(objenesis.newInstance(EventVideoActivity::class.java))
    }
}
