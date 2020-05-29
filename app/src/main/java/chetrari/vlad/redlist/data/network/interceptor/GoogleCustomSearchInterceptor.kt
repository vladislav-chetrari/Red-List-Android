package chetrari.vlad.redlist.data.network.interceptor

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleCustomSearchInterceptor @Inject constructor() : BaseInterceptor() {

    override val queryParameters = hashMapOf(
        "key" to API_KEY,
        "cx" to CX,
        "fields" to FIELDS
    )

    private companion object {
        const val API_KEY = "AIzaSyDERikuej05rQCqqo-4JQXCx_31ZuNsqp4"
        const val CX = "006892408646295645421:5lcbe5sc2zk"
        const val FIELDS = "items(title,pagemap(cse_thumbnail,cse_image))"
    }
}