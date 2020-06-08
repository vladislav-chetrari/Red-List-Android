package chetrari.vlad.redlist.data.persistence.repository

import android.content.res.Resources
import chetrari.vlad.redlist.R.string.error_no_internet
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class RepositoryErrorMapperTest {

    @MockK
    private lateinit var res: Resources

    private lateinit var mapper: RepositoryErrorMapper

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mapper = RepositoryErrorMapper(res)
    }

    @Test
    fun map() {
        val throwable = Throwable()
        val unknownHostException = UnknownHostException()
        val errorMessage = "error"
        every { res.getString(error_no_internet) } returns errorMessage

        val result1 = mapper.map(throwable)
        val result2 = mapper.map(unknownHostException)

        assertEquals(throwable, result1)
        assertEquals(errorMessage, result2.message)
        assertEquals(unknownHostException, result2.cause)
    }
}