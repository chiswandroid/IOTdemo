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
package com.chisw.iotdemo.ui.main

import com.chisw.iotdemo.mvp.model.prefs.IPreferencesModel
import com.chisw.iotdemo.mvp.presenter.BasePresenter

class MainPresenter(private val prefs: IPreferencesModel) : BasePresenter<MainView> {

    override var view: MainView? = null

    fun isGesturesLocked(): Boolean = prefs.isGesturesLocked()

}