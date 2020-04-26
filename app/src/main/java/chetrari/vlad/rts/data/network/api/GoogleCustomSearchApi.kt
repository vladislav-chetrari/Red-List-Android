package chetrari.vlad.rts.data.network.api

import chetrari.vlad.rts.data.network.model.ImageListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleCustomSearchApi {

    @GET("v1")
    fun searchImages(@Query("q") query: String): Call<ImageListResponse>
}