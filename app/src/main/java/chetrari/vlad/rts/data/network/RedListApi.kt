package chetrari.vlad.rts.data.network

import chetrari.vlad.rts.data.network.model.CountriesResponse
import retrofit2.Call
import retrofit2.http.GET

interface RedListApi {

    @GET("country/list")
    fun countries(): Call<CountriesResponse>

}