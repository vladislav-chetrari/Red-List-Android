package chetrari.vlad.redlist.data.persistence.repository

import android.content.res.Resources
import chetrari.vlad.redlist.R
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryErrorMapper @Inject constructor(
    private val res: Resources
) {

    fun map(throwable: Throwable) = when (throwable) {
        is UnknownHostException -> Throwable(res.getString(R.string.error_no_internet), throwable)
        else -> throwable
    }
}