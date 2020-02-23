package chetrari.vlad.rts.di.module

import chetrari.vlad.rts.app.main.countries.CountriesFragment
import chetrari.vlad.rts.di.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun countriesFragment(): CountriesFragment
}