package chetrari.vlad.rts.data.domain

import chetrari.vlad.rts.data.network.api.RedListApi
import chetrari.vlad.rts.data.network.model.Country
import javax.inject.Inject

class CountryListFetcher @Inject constructor(
    private val api: RedListApi
) : IOUseCase<Unit, List<Country>>() {

    override suspend fun execute(input: Unit) = api.countries().body.results.sortedBy(Country::name)
}