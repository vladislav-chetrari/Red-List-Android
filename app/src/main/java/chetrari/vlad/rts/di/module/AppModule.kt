package chetrari.vlad.rts.di.module

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import chetrari.vlad.rts.App
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    internal fun context(app: App): Context = app.applicationContext

    @Provides
    internal fun resources(context: Context): Resources = context.resources

    @Provides
    internal fun assets(context: Context): AssetManager = context.assets
}