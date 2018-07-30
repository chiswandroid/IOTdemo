/*
 * Copyright (C) 2018 CHI Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chisw.iotdemo.mvp.model.network

import com.chisw.iotdemo.BuildConfig
import com.chisw.iotdemo.models.news.NewsResponse
import com.chisw.iotdemo.models.weather.WeatherResponse
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel
import com.chisw.iotdemo.network.Network
import com.chisw.iotdemo.network.NetworkCallback
import com.chisw.iotdemo.network.api.CityApi
import com.chisw.iotdemo.network.api.NewsApi
import com.chisw.iotdemo.network.api.WeatherApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier

class NetworkModel(private val prefs: IPreferencesModel) : INetworkModel {

    private lateinit var retrofitWeather: WeatherApi
    private lateinit var retrofitNews: NewsApi
    private lateinit var retrofitCity: CityApi

    private lateinit var gson: Gson

    init {
        setUpGson()
        setUpRetrofit()
    }

    private fun setUpRetrofit() {
        val retrofitWeather = Retrofit.Builder()
                .baseUrl(BuildConfig.WEATHER_URL)
                .client(provideOkHttpClient(provideLoggingInterceptor()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        this.retrofitWeather = retrofitWeather.create(WeatherApi::class.java)

        val retrofitNews = Retrofit.Builder()
                .baseUrl(BuildConfig.NEWS_URL)
                .client(provideOkHttpClient(provideLoggingInterceptor()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        this.retrofitNews = retrofitNews.create(NewsApi::class.java)

        val retrofitCity = Retrofit.Builder()
                .baseUrl(BuildConfig.CITY_URL)
                .client(provideOkHttpClient(provideLoggingInterceptor()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        this.retrofitCity = retrofitCity.create(CityApi::class.java)
    }

    private fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        val b = OkHttpClient.Builder()
        b.addInterceptor(interceptor)
        return b.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    private fun setUpGson() {
        gson = GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .setLenient()
                .create()
    }

    override fun getWeather(ip: String, callback: NetworkCallback<WeatherResponse>) {
        //try to get city name by IP, in case it returns "Undefined" use default value
        Network.request(retrofitCity.getCity(ip), NetworkCallback<String>().apply {
            success = {
                if (it != UNDEFINED) prefs.setCity(it)
                Network.request(retrofitWeather.getWeather(city = prefs.getCity(), appid = BuildConfig.WEATHER_API_KEY), callback)
            }
            error = {
                Network.request(retrofitWeather.getWeather(city = prefs.getCity(), appid = BuildConfig.WEATHER_API_KEY), callback)
            }
        })
    }

    override fun getNews(callback: NetworkCallback<NewsResponse>) {
        Network.request(retrofitNews.getNews(prefs.getNewsSource(), BuildConfig.NEWS_API_KEY), callback)
    }

    companion object {

        const val UNDEFINED = "Undefined"

    }

}