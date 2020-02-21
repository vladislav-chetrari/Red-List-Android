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
) : UseCase<Unit, String>() {

    private var currentIndex = 0
    private val persons = listOf("Alice", "Vlad")

    override suspend fun execute(input: Unit) = repeat(100) {
        progress()
        delay(1000L)
        val output = resources.getString(R.string.greeting_message, persons[currentIndex % persons.size])
        currentIndex += 1
        update(output)
        delay(1000L)
    }
}