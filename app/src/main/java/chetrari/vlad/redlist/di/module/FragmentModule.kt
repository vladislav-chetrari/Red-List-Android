package chetrari.vlad.redlist.di.module

import chetrari.vlad.redlist.app.main.countries.CountriesFragment
import chetrari.vlad.redlist.app.main.search.SpeciesSearchFragment
import chetrari.vlad.redlist.app.main.species.SpeciesListFragment
import chetrari.vlad.redlist.app.main.species.details.SpeciesFragment
import chetrari.vlad.redlist.app.main.watching.WatchingSpeciesFragment
import chetrari.vlad.redlist.di.FragmentScoped
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