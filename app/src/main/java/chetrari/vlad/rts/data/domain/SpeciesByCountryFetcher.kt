package chetrari.vlad.rts.data.domain

import chetrari.vlad.rts.data.network.RedListApi
import chetrari.vlad.rts.data.network.model.Country
import chetrari.vlad.rts.data.network.model.Species
import javax.inject.Inject

class SpeciesByCountryFetcher @Inject constructor(
    private val api: RedListApi
) : FetcherUseCase<Country, List<Species>>() {

    override suspend fun execute(input: Country) = api.speciesByCountry(input.isoCode).body.species
}