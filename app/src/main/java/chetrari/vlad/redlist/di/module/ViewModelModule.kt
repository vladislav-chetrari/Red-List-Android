package chetrari.vlad.redlist.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chetrari.vlad.redlist.app.main.countries.CountriesViewModel
import chetrari.vlad.redlist.app.main.search.SpeciesSearchViewModel
import chetrari.vlad.redlist.app.main.species.SpeciesListViewModel
import chetrari.vlad.redlist.app.main.species.details.SpeciesViewModel
import chetrari.vlad.redlist.app.main.watching.WatchingViewModel
import chetrari.vlad.redlist.app.welcome.WelcomeViewModel
import chetrari.vlad.redlist.di.ViewModelFactory
import chetrari.vlad.redlist.di.ViewModelKey
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

    @Binds
    @IntoMap
    @ViewModelKey(SpeciesSearchViewModel::class)
    internal abstract fun speciesSearchViewModel(viewModel: SpeciesSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WatchingViewModel::class)
    internal abstract fun watchingViewModel(viewModel: WatchingViewModel): ViewModel
}