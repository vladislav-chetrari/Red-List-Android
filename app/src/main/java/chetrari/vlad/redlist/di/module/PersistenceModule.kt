package chetrari.vlad.redlist.di.module

import android.content.Context
import chetrari.vlad.redlist.data.persistence.model.Country
import chetrari.vlad.redlist.data.persistence.model.MyObjectBox
import chetrari.vlad.redlist.data.persistence.model.Species
import dagger.Module
import dagger.Provides
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun boxStore(context: Context): BoxStore = MyObjectBox.builder().androidContext(context).build()

    @Provides
    @Singleton
    fun countryBox(store: BoxStore) = store.boxFor<Country>()

    @Provides
    @Singleton
    fun speciesBox(store: BoxStore) = store.boxFor<Species>()
}
