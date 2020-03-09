package chetrari.vlad.rts.data.model.response

data class CountriesResponse(
    val count: Int = 0,
    val results: List<CountryResponse> = emptyList()
)