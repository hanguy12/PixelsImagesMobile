package hn.single.imageapp.common.networking

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import hn.single.imageapp.BuildConfig
import hn.single.imageapp.common.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(AppConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(AppConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(AppConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true).apply {
                if (BuildConfig.DEBUG) {
                    val bodyLog = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(bodyLog)
                }
                addInterceptor {
                    val request = it.request()
                    val builder = request.newBuilder().header("Content-Type", "application/json")
                        .method(request.method, request.body).build()
                    it.proceed(builder)
                }
            }.build()
    }

    private val mRetrofit by lazy {
        val gson: Gson = GsonBuilder().setLenient().create()
        Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    val getRetrofit: IRetrofit by lazy {
        mRetrofit.create(IRetrofit::class.java)
    }

}