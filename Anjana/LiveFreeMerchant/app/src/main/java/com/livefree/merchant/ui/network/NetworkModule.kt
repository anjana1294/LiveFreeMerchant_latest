package com.livefree.merchant.ui.network

import android.content.Context
import android.util.Log
import com.readystatesoftware.chuck.ChuckInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


class NetworkModule {
    private val apiEndpoint = "https://livefreee.herokuapp.com/"
    /* @Provides
    @Named("baseUrl")
    @Singleton
    StringPreference provideEndpointPreference(SharedPreferences prefs) {
        return new StringPreference(prefs, "baseUrl", ApiEndpoints.RELEASE.url);
    }

    @Provides
    @Singleton
    HttpUrl provideHttpUrl(@Named("baseUrl") StringPreference apiEndpoint) {
        return HttpUrl.parse(apiEndpoint.get());
    }
*/

    internal fun provideOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                        message -> Log.v("OkHttp",message) })
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(ChuckInterceptor(context))
            .cache(null)
            .build()
    }

    internal fun provideRetrofitClient(context: Context): RestService {


        val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

        return Retrofit.Builder()
            .baseUrl(apiEndpoint)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .client(provideOkHttpClient(context))
            .build().create(RestService::class.java)
    }

    companion object {

        private val READ_TIME_OUT = 30
        private val CONNECT_TIMEOUT = 10
    }


}