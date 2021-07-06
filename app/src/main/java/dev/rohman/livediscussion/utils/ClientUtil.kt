package dev.rohman.livediscussion.utils

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ClientUtil {
    companion object {
        inline fun <reified T> service(url: String): T {
            val client = OkHttpClient().newBuilder().apply {
                retryOnConnectionFailure(true)

                connectTimeout(1, TimeUnit.MINUTES)
                writeTimeout(1, TimeUnit.MINUTES)
                readTimeout(1, TimeUnit.MINUTES)
                callTimeout(1, TimeUnit.MINUTES)

                addInterceptor(HttpLoggingInterceptor { Log.e("API-LOG", it) }.apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })

                addInterceptor {
                    return@addInterceptor it.proceed(it.request().newBuilder().apply {
                        apply {
                            addHeader("Content-Type", "application/json")
                            addHeader("Accept", "application/json")
                        }
                    }.build())
                }
            }.build()

            val retrofit = Retrofit.Builder().apply {
                baseUrl(url)
                client(client)
                addConverterFactory(
                    GsonConverterFactory.create(GsonBuilder().setLenient().create())
                )
            }.build()

            return retrofit.create(T::class.java)
        }
    }
}