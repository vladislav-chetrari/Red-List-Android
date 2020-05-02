package chetrari.vlad.rts.data.persistence

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val prefs: SharedPreferences) {

    var isWelcomePassed: Boolean
        get() = prefs.getBoolean(IS_WELCOME_PASSED, false)
        set(value) = prefs.edit { putBoolean(IS_WELCOME_PASSED, value) }

    private companion object {
        const val IS_WELCOME_PASSED = "isWelcomePassed"
    }
}