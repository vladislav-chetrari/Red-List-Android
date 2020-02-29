package chetrari.vlad.rts.data.network.model

data class CountriesResponse(
    val count: Int = 0,
    val results: List<Country> = emptyList()
)