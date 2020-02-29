package chetrari.vlad.rts.data.network.api

import chetrari.vlad.rts.data.network.model.WikiMediaResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WikiMediaApi {

    @GET("page/media/{title}")
    fun media(@Path("title") title: String): Call<WikiMediaResponse>
}