package chetrari.vlad.rts.data.network

import okhttp3.Interceptor
import okhttp3.Response

class RedListInterceptor(
    private val token: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url().newBuilder()
            .addQueryParameter("token", token)
            .build()
        return chain.proceed(request.newBuilder().url(url).build())
    }
}