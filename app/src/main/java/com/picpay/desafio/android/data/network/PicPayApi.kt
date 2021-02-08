package com.picpay.desafio.android.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.infrastructure.getConnectionType
import com.picpay.desafio.android.ui.MyApplication
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PicPayApi {

    private const val url = "http://careers.picpay.com/tests/mobdev/"

    private const val cacheSize = (5 * 1024 * 1024).toLong()
    private const val fiveSeconds = 5
    private const val sevenDayOfCache = 60 * 60 * 24 * 7

    private val context = MyApplication.getContext()
    private val myCache = Cache(context.cacheDir, cacheSize)
    private val gson: Gson by lazy { GsonBuilder().create() }

    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (getConnectionType(context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=$fiveSeconds")
                        .build()
                else
                    request.newBuilder().header(
                        "Cache-control",
                        "public, only-i-cached, max-stale=$sevenDayOfCache")
                        .build()
                chain.proceed(request)
            }
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val service: PicPayService by lazy {
        retrofit.create(PicPayService::class.java)
    }
}
