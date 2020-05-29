package chetrari.vlad.redlist.data.network.model

data class CountriesResponse(
    val count: Int = 0,
    val results: List<CountryResponse> = emptyList()
)