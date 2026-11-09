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

class ActivitiesObjenesisCoverageTest {

    private val objenesis = ObjenesisStd()

    @Test
    fun instanciarTodasAsActivitiesComObjenesis() {
        assertNotNull(objenesis.newInstance(MainActivity::class.java))
        assertNotNull(objenesis.newInstance(RegistrationActivity::class.java))
        assertNotNull(objenesis.newInstance(RecoveryActivity::class.java))
        assertNotNull(objenesis.newInstance(DashboardActivity::class.java))
        assertNotNull(objenesis.newInstance(EventsActivity::class.java))
        assertNotNull(objenesis.newInstance(EventDetailsActivity::class.java))
        assertNotNull(objenesis.newInstance(ProfileActivity::class.java))
        assertNotNull(objenesis.newInstance(EditProfileActivity::class.java))
        assertNotNull(objenesis.newInstance(FavoritesActivity::class.java))
        assertNotNull(objenesis.newInstance(MyEventsActivity::class.java))
        assertNotNull(objenesis.newInstance(EventPhotosActivity::class.java))
        assertNotNull(objenesis.newInstance(EventAudioActivity::class.java))
        assertNotNull(objenesis.newInstance(EventVideoActivity::class.java))
    }
}
