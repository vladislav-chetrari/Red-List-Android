package chetrari.vlad.redlist.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

open class BaseInterceptor : Interceptor {

    open val queryParameters: Map<String, String> = hashMapOf()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlBuilder = request.url().newBuilder()
        queryParameters.entries.forEach { urlBuilder.addQueryParameter(it.key, it.value) }
        return chain.proceed(request.newBuilder().url(urlBuilder.build()).build())
    }
}