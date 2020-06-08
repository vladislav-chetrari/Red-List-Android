package chetrari.vlad.redlist.di.module

import android.content.res.Resources
import chetrari.vlad.redlist.R
import chetrari.vlad.redlist.data.network.api.RedListApi
import chetrari.vlad.redlist.data.network.interceptor.RedListInterceptor
import chetrari.vlad.redlist.di.RedList
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    fun gson(): Gson = GsonBuilder().serializeNulls().create()

    @Provides
    @Singleton
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun loggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

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
    @RedList
    fun redListRetrofit(
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
    fun redListApi(@RedList retrofit: Retrofit): RedListApi = retrofit.create(RedListApi::class.java)

    private companion object {
        const val REQUEST_TIMEOUT = 30L
    }
}