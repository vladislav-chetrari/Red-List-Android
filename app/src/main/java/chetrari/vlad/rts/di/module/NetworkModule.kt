package chetrari.vlad.rts.di.module

import android.content.res.Resources
import chetrari.vlad.rts.R
import chetrari.vlad.rts.data.network.api.RedListApi
import chetrari.vlad.rts.data.network.api.WikiMediaApi
import chetrari.vlad.rts.data.network.interceptor.RedListInterceptor
import chetrari.vlad.rts.di.RedList
import chetrari.vlad.rts.di.WikiMedia
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun redListInterceptor(res: Resources) = RedListInterceptor(res.getString(R.string.red_list_api_token))

    @Provides
    @Singleton
    @RedList
    fun redListHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        redListInterceptor: RedListInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor(redListInterceptor)
        .build()

    @Provides
    @Singleton
    @WikiMedia
    fun wikiMediaHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    @RedList
    fun retrofit(
        res: Resources,
        @RedList client: OkHttpClient,
        factory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(res.getString(R.string.red_list_api_base_url))
        .client(client)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Singleton
    @WikiMedia
    fun wikiMediaRetrofit(
        res: Resources,
        @WikiMedia client: OkHttpClient,
        factory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(res.getString(R.string.wiki_media_api_base_url))
        .client(client)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Singleton
    fun redListApi(@RedList retrofit: Retrofit): RedListApi = retrofit.create(RedListApi::class.java)

    @Provides
    @Singleton
    fun wikiMediaApi(@WikiMedia retrofit: Retrofit): WikiMediaApi = retrofit.create(WikiMediaApi::class.java)

    private companion object {
        const val REQUEST_TIMEOUT = 30L
    }
}