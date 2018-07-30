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
package com.chisw.iotdemo.mvp.model.prefs

import android.content.SharedPreferences
import com.chisw.iotdemo.models.news.NewsSource
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel.Companion.KEY_CITY
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel.Companion.KEY_GESTURES_LOCKED
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel.Companion.KEY_NEWS_SOURCE
import com.google.gson.Gson

class PreferencesModel(private val preferences: SharedPreferences) : IPreferencesModel {

    override fun getSharedPrefs() = preferences

    override fun setCity(city: String) {
        setString(KEY_CITY, city)
    }

    override fun getCity(): String = preferences.getString(KEY_CITY, "Kharkiv")

    override fun setNewsSource(source: NewsSource) =
            setString(KEY_NEWS_SOURCE, Gson().toJson(source))

    override fun isGesturesLocked(): Boolean = preferences.getBoolean(KEY_GESTURES_LOCKED, false)

    override fun setGesturesLocked(isLocked: Boolean) =
            setBoolean(KEY_GESTURES_LOCKED, isLocked)

    override fun getNewsSource(): String =
            Gson().fromJson(preferences.getString(KEY_NEWS_SOURCE,
                    "{\"name\":\"TechCrunch\",\"query\":\"techcrunch\"}"), NewsSource::class.java).query

    override fun getNewsName(): String =
            Gson().fromJson(preferences.getString(KEY_NEWS_SOURCE,
                    "{\"name\":\"TechCrunch\",\"query\":\"techcrunch\"}"), NewsSource::class.java).name

    private fun setString(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    private fun setInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    private fun setFloat(key: String, value: Float) {
        preferences.edit().putFloat(key, value).apply()
    }

    private fun setLong(key: String, value: Long) {
        preferences.edit().putLong(key, value).apply()
    }

    private fun setBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    private fun setStringSet(key: String, value: Set<String>) {
        preferences.edit().putStringSet(key, value).apply()
    }
}
