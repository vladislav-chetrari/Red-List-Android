package chetrari.vlad.rts.di

import chetrari.vlad.rts.App
import chetrari.vlad.rts.di.module.ActivityModule
import chetrari.vlad.rts.di.module.AppModule
import chetrari.vlad.rts.di.module.ViewModelModule
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
        ActivityModule::class/*,
        FragmentModule::class,
        PersistenceModule::class,
        UiModule::class,
        NetworkModule::class,
        NetworkClientModule::class*/]
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