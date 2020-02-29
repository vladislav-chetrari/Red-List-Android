package chetrari.vlad.rts.di.module

import chetrari.vlad.rts.app.main.MainActivity
import chetrari.vlad.rts.di.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity
}