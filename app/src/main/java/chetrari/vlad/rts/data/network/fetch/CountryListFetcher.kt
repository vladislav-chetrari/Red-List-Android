package chetrari.vlad.rts.data.network.fetch

import chetrari.vlad.rts.data.model.response.CountriesResponse
import chetrari.vlad.rts.data.model.ui.Country
import chetrari.vlad.rts.data.model.ui.Country_
import chetrari.vlad.rts.data.network.api.RedListApi
import io.objectbox.Box
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryListFetcher @Inject constructor(
    private val api: RedListApi,
    private val box: Box<Country>
) : Fetcher<Unit, CountriesResponse, List<Country>>() {

    override suspend fun fetch(input: Unit) = api.countries().body

    override suspend fun map(response: CountriesResponse) = response.results
        .map { Country(isoCode = it.isoCode, name = it.name) }

    override suspend fun persist(input: Unit, output: List<Country>) = box.put(output
        .map { (box.query().equal(Country_.isoCode, it.isoCode).build().findFirst() ?: Country()) to it }
        .map {
            it.first.name = it.second.name
            it.first.isoCode = it.second.isoCode
            it.first
        })
}