package chetrari.vlad.rts.di.module

import chetrari.vlad.rts.app.main.countries.CountriesFragment
import chetrari.vlad.rts.app.main.species.SpeciesListFragment
import chetrari.vlad.rts.app.main.species.details.SpeciesFragment
import chetrari.vlad.rts.app.welcome.greeting.GreetingFragment
import chetrari.vlad.rts.di.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun greetingFragment(): GreetingFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun countriesFragment(): CountriesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun speciesListFragment(): SpeciesListFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun speciesFragment(): SpeciesFragment
}