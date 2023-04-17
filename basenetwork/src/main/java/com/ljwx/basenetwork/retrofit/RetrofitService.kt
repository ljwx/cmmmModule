package com.ljwx.basenetwork.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

inline fun <reified T : Any> http() = RetrofitService.getService(T::class.java)

internal var globalErrorHandler: ((Throwable) -> Unit)? = null

object RetrofitService {

    private val retrofit by lazy { retrofitProvider() }

    /**
     * service容器
     */
    private var services = mutableMapOf<Class<*>, Any>()

    /**
     * retrofit实例对象
     */
    private lateinit var retrofitProvider: () -> Retrofit

    /**
     * 设置retrofit对象
     */
    fun setRetrofitProvider(factory: () -> Retrofit) {
        retrofitProvider = factory
    }

    fun setErrorHandler(handler: (Throwable) -> Unit) {
        globalErrorHandler = handler
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getService(clazz: Class<T>): T =
        services.getOrPut(clazz) { retrofit.create(clazz) } as T

    fun setNormalRetrofit(okhttp: OkHttpClient, baseUrl: String) {
        setRetrofitProvider {
            Retrofit.Builder().client(okhttp)
                .baseUrl(baseUrl)
                .addConverterFactory(
                    RetrofitResultConverterFactory.create(
                        Moshi.Builder().add(
                            KotlinJsonAdapterFactory()
                        ).build()
                    ).asLenient()
                )
                .addCallAdapterFactory(RetrofitResultAdapterFactory())
                .build()
        }
    }
}
