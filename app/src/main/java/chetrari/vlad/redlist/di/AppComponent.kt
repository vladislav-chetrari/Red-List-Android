package chetrari.vlad.redlist.di

import chetrari.vlad.redlist.app.App
import chetrari.vlad.redlist.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            ViewModelModule::class,
            ActivityModule::class,
            FragmentModule::class,
            NetworkModule::class,
            PersistenceModule::class]
)
interface AppComponent : AndroidInjector<App> {

    override fun inject(instance: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}