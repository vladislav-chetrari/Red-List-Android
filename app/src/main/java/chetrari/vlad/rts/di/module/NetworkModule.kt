package chetrari.vlad.rts.di.module

import android.content.res.Resources
import chetrari.vlad.rts.R
import chetrari.vlad.rts.data.network.RedListApi
import chetrari.vlad.rts.data.network.RedListInterceptor
import chetrari.vlad.rts.di.RedListBaseUrl
import chetrari.vlad.rts.di.RedListToken
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
    @RedListBaseUrl
    fun baseUrl(res: Resources): String = res.getString(R.string.red_list_api_base_url)

    @Provides
    @Singleton
    @RedListToken
    fun redListToken(res: Resources): String = res.getString(R.string.red_list_api_token)

    @Provides
    @Singleton
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    @Singleton
    fun redListInterceptor(@RedListToken token: String) = RedListInterceptor(token)

    @Provides
    @Singleton
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor, redListInterceptor: RedListInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(redListInterceptor)
            .build()

    @Provides
    @Singleton
    fun retrofit(@RedListBaseUrl baseUrl: String, factory: GsonConverterFactory, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(factory)
            .build()

    @Provides
    @Singleton
    fun redListApi(retrofit: Retrofit): RedListApi = retrofit.create(RedListApi::class.java)

    private companion object {
        const val REQUEST_TIMEOUT = 90L
    }
}