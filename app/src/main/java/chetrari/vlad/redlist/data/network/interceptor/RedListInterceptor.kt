package chetrari.vlad.redlist.data.network.interceptor

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RedListInterceptor @Inject constructor() : BaseInterceptor() {

    override val queryParameters = hashMapOf("token" to TOKEN)

    private companion object {
        const val TOKEN = "9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"
    }
}