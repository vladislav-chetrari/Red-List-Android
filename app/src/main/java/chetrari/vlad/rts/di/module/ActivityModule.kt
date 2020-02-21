package chetrari.vlad.rts.di.module

import chetrari.vlad.rts.di.ActivityScoped
import chetrari.vlad.rts.hello.HelloActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScoped
    @ContributesAndroidInjector
    internal abstract fun helloActivity(): HelloActivity
}