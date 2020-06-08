package chetrari.vlad.redlist.data.persistence

import android.content.SharedPreferences
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class UserPreferencesTest {

    @MockK
    private lateinit var prefs: SharedPreferences

    private lateinit var userPreferences: UserPreferences

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userPreferences = UserPreferences(prefs)
    }

    @Test
    fun isWelcomePassed_get() {
        every { prefs.getBoolean(IS_WELCOME_PASSED, false) } returns true
        assertTrue(userPreferences.isWelcomePassed)
    }

    @Test
    fun isWelcomePassed_set() {
        val editor = mockk<SharedPreferences.Editor>(relaxed = true)
        every { prefs.edit() } returns editor
        every { editor.putBoolean(IS_WELCOME_PASSED, true) } returns editor

        userPreferences.isWelcomePassed = true

        verify { editor.apply() }
    }

    private companion object {
        const val IS_WELCOME_PASSED = "isWelcomePassed"
    }
}