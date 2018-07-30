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
package com.chisw.iotdemo.di

import android.content.Context.MODE_PRIVATE
import com.chisw.iotdemo.mvp.model.network.INetworkModel
import com.chisw.iotdemo.mvp.model.network.NetworkModel
import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel
import com.chisw.iotdemo.mvp.model.prefs.PreferencesModel
import com.chisw.iotdemo.ui.browser.BrowserPresenter
import com.chisw.iotdemo.ui.feed.FeedPresenter
import com.chisw.iotdemo.ui.main.MainPresenter
import com.chisw.iotdemo.ui.splash.SplashPresenter
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

/**
 * Inject presenters
 */
val AppModule = applicationContext {
    factory { SplashPresenter() }
    factory { MainPresenter(get()) }
    factory { FeedPresenter(get(), get()) }
    factory { BrowserPresenter() }
}

/**
 * Inject [IPreferencesModel]
 */
val PrefsModule = applicationContext {
    bean {
        PreferencesModel(androidApplication().getSharedPreferences("IOTGestures",
                MODE_PRIVATE)) as IPreferencesModel
    }
}

/**
 * Inject [INetworkModel]
 */
val NetworkModule = applicationContext {
    bean { NetworkModel(get()) as INetworkModel }
}

/**
 * Modules list
 */
val appModules = listOf(AppModule, NetworkModule, PrefsModule)

