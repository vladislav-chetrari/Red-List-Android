package chetrari.vlad.rts.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chetrari.vlad.rts.app.main.countries.CountriesViewModel
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
    @ViewModelKey(CountriesViewModel::class)
    internal abstract fun countriesViewModel(viewModel: CountriesViewModel): ViewModel
}