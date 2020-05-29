package chetrari.vlad.redlist.di.module

import chetrari.vlad.redlist.app.main.MainActivity
import chetrari.vlad.redlist.app.welcome.WelcomeActivity
import chetrari.vlad.redlist.di.ActivityScoped
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun welcomeActivity(): WelcomeActivity

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity
}