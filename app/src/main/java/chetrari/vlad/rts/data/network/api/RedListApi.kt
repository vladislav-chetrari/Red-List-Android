package chetrari.vlad.rts.data.network.api

import chetrari.vlad.rts.data.network.model.CountriesResponse
import chetrari.vlad.rts.data.network.model.SpeciesByCountryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RedListApi {

    @GET("country/list")
    fun countries(): Call<CountriesResponse>

    @GET("country/getspecies/{countryIsoCode}")
    fun speciesByCountry(
        @Path("countryIsoCode") countryIsoCode: String
    ): Call<SpeciesByCountryResponse>

}