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
package com.chisw.iotdemo.mvp.model.prefs

import android.content.SharedPreferences
import com.chisw.iotdemo.models.news.NewsSource

interface IPreferencesModel {

    /**
     * Get city name
     *
     * @return [String] city
     */
    fun getCity(): String

    /**
     * Set [String] city name
     *
     * @param city required
     */
    fun setCity(city: String)

    /**
     * Get news source query
     *
     * @return [String] query
     */
    fun getNewsSource(): String

    /**
     * Get news source name
     *
     * @return [String] name
     */
    fun getNewsName(): String

    /**
     * Set [NewsSource]
     *
     * @param source required
     */
    fun setNewsSource(source: NewsSource)

    /**
     * Get gesture detection status
     *
     * @return [Boolean] status
     */
    fun isGesturesLocked(): Boolean

    /**
     * Set gesture detection status
     *
     * @param isLocked required
     */
    fun setGesturesLocked(isLocked: Boolean)

    fun getSharedPrefs(): SharedPreferences

    companion object {

        const val KEY_CITY = "key_city"
        const val KEY_NEWS_SOURCE = "key_news_source"
        const val KEY_GESTURES_LOCKED = "key_gestures_locked"

    }
}
