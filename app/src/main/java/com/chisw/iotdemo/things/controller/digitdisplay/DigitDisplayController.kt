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
package com.chisw.iotdemo.things.controller.digitdisplay

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.chisw.iotdemo.things.controller.BaseController
import com.chisw.iotdemo.things.supplier.digitdisplay.DigitDisplaySupplier

class DigitDisplayController(private val supplier: DigitDisplaySupplier, lifecycle: Lifecycle) : BaseController, LifecycleObserver {

    var isRunning: Boolean = false
        private set

    var counter: Double = 0.0
        private set

    init {
        lifecycle.addObserver(this)
    }

    @Throws(Exception::class)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun close() {
        supplier.close()
    }

    fun display(n: Double) {
        supplier.display(n)
    }

    fun display(n: String) {
        supplier.display(n)
    }

}
