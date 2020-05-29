package chetrari.vlad.rts.di.module

import chetrari.vlad.rts.app.main.countries.CountriesFragment
import chetrari.vlad.rts.app.main.search.SpeciesSearchFragment
import chetrari.vlad.rts.app.main.species.SpeciesListFragment
import chetrari.vlad.rts.app.main.species.details.SpeciesFragment
import chetrari.vlad.rts.app.main.watching.WatchingSpeciesFragment
import chetrari.vlad.rts.di.FragmentScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun countriesFragment(): CountriesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun speciesListFragment(): SpeciesListFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun speciesFragment(): SpeciesFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun speciesSearchFragment(): SpeciesSearchFragment

    @FragmentScoped
    @ContributesAndroidInjector
    abstract fun watchingFragment(): WatchingSpeciesFragment
}