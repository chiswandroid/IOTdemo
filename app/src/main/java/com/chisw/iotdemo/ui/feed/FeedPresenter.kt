/*
 * Copyright (C) 2018 CHI Software Inc.
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
package com.chisw.iotdemo.ui.feed

import android.content.SharedPreferences
import com.chisw.iotdemo.models.news.NewsResponse
import com.chisw.iotdemo.models.news.NewsSource
import com.chisw.iotdemo.models.weather.WeatherResponse
import com.chisw.iotdemo.mvp.model.network.INetworkModel
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel.Companion.KEY_GESTURES_LOCKED
import com.chisw.iotdemo.mvp.presenter.BasePresenter
import com.chisw.iotdemo.network.NetworkCallback
import com.google.gson.Gson

class FeedPresenter(private val network: INetworkModel, private val prefs: IPreferencesModel) : BasePresenter<FeedView> {

    override var view: FeedView? = null

    private var sources: MutableList<NewsSource> = ArrayList()
    private var sourcesToShow: MutableList<String> = ArrayList()

    private val sharedListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _: SharedPreferences, key: String ->
        if (key == KEY_GESTURES_LOCKED) view?.setBlockText()
    }

    /**
     * * [INetworkModel] weather request
     */
    fun getWeather(ip: String) {
        network.getWeather(ip, NetworkCallback<WeatherResponse>().apply {
            success = { view?.setUpWeather(it) }
            error = { view?.handleError(it) }
        })
    }

    /**
     * [INetworkModel] news request
     */
    fun getNews() {
        network.getNews(NetworkCallback<NewsResponse>().apply {
            success = { view?.setUpNews(it) }
            error = { view?.handleError(it) }
        })
    }

    fun lockGestures() {
        prefs.setGesturesLocked(!prefs.isGesturesLocked())
    }

    fun isGesturesLocked(): Boolean = prefs.isGesturesLocked()

    fun subscribeOnGestureStatus() {
        view?.setBlockText()
        prefs.getSharedPrefs().registerOnSharedPreferenceChangeListener(sharedListener)
    }

    fun unsubscribeOnGestureStatus() {
        prefs.getSharedPrefs().unregisterOnSharedPreferenceChangeListener(sharedListener)
    }

    fun getNewsSource(): String = prefs.getNewsName()

    fun getNewsSourceList(array: Array<String>): MutableList<String> {
        val gson = Gson()
        sources.clear()
        sourcesToShow.clear()
        for (source in array) {
            sources.add(gson.fromJson(source, NewsSource::class.java))
            sourcesToShow.add(gson.fromJson(source, NewsSource::class.java).name)
        }
        return sourcesToShow
    }

    fun saveNewsSource(source: String) {
        for (i in 0 until sources.size) {
            if (sources[i].name == source) {
                prefs.setNewsSource(sources[i])
                getNews()
                break
            }
        }
    }

}
