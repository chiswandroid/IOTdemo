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
package com.chisw.iotdemo.network.api

import com.chisw.iotdemo.models.news.NewsResponse
import com.chisw.iotdemo.models.weather.WeatherResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    /**
     * Get news
     *
     * @param sources required
     * @param apiKey required
     * @return [NewsResponse]
     */
    @GET("top-headlines")
    fun getNews(@Query("sources") source: String,
                @Query("apiKey") apiKey: String): Deferred<NewsResponse>
}