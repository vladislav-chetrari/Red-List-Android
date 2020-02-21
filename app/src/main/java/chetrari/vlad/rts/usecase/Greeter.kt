package chetrari.vlad.rts.usecase

import android.content.res.Resources
import chetrari.vlad.rts.R
import chetrari.vlad.rts.base.UseCase
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Greeter @Inject constructor(
    private val resources: Resources
) : UseCase<Nothing, String>() {

    private var currentIndex = -1
    private val persons = listOf("Alice", "Vlad")

    override suspend fun run(input: Nothing): String {
        delay(1000L)
        return resources.getString(R.string.greeting_message, persons[currentIndex % persons.size]).also {
            currentIndex += 1
        }
    }
}