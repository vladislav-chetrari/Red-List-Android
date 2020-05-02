package chetrari.vlad.rts.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chetrari.vlad.rts.app.main.countries.CountriesViewModel
import chetrari.vlad.rts.app.main.species.SpeciesListViewModel
import chetrari.vlad.rts.app.main.species.details.SpeciesViewModel
import chetrari.vlad.rts.app.welcome.WelcomeViewModel
import chetrari.vlad.rts.di.ViewModelFactory
import chetrari.vlad.rts.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    internal abstract fun greetingViewModel(viewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountriesViewModel::class)
    internal abstract fun countriesViewModel(viewModel: CountriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpeciesListViewModel::class)
    internal abstract fun speciesListViewModel(viewModel: SpeciesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SpeciesViewModel::class)
    internal abstract fun speciesViewModel(viewModel: SpeciesViewModel): ViewModel
}