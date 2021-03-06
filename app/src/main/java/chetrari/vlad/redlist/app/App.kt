package chetrari.vlad.redlist.app

import android.app.Application
import chetrari.vlad.redlist.di.DaggerAppComponent
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    internal lateinit var injector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = injector

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder().application(this).build().inject(this)
        Timber.plant(Timber.DebugTree())
    }
}