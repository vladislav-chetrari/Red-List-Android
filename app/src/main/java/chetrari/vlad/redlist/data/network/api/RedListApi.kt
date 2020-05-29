package chetrari.vlad.redlist.data.network.api

import chetrari.vlad.redlist.data.network.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RedListApi {

    @GET("country/list")
    fun countries(): Call<CountriesResponse>

    @GET("country/getspecies/{countryIsoCode}")
    fun speciesByCountry(
        @Path("countryIsoCode") countryIsoCode: String
    ): Call<ArrayResponse<SpeciesResponse>>

    @GET("species/category/{category}")
    fun speciesByCategory(
        @Path("category") category: String
    ): Call<ArrayResponse<SpeciesResponse>>

    @GET("species/id/{id}")
    fun detailsBySpeciesId(
        @Path("id") speciesId: Long
    ): Call<ArrayResponse<SpeciesResponse>>

    @GET("species/{name}")
    fun detailsBySpeciesScientificName(
        @Path("name") scientificName: String
    ): Call<ArrayResponse<SpeciesResponse>>

    @GET("species/common_names/{name}")
    fun commonNamesByScientificName(
        @Path("name") scientificName: String
    ): Call<ArrayResponse<CommonNameResponse>>

    @GET("species/narrative/id/{id}")
    fun narrativeBySpeciesId(
        @Path("id") speciesId: Long
    ): Call<ArrayResponse<SpeciesNarrativeResponse>>

    @GET("weblink/{name}")
    fun webLinkBySpeciesScientificName(
        @Path("name") scientificName: String
    ): Call<SpeciesWebLinkResponse>
}