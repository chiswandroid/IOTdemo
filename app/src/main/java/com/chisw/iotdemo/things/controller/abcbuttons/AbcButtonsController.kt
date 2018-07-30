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
package com.chisw.iotdemo.things.controller.abcbuttons

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.annotation.VisibleForTesting
import com.chisw.iotdemo.things.controller.BaseController
import com.chisw.iotdemo.things.model.AbcButton
import com.chisw.iotdemo.things.supplier.abcbuttons.AbcButtonsSupplier

class AbcButtonsController(private val supplier: AbcButtonsSupplier, lifecycle: Lifecycle) : BaseController, AbcButtonsSupplier.Listener, LifecycleObserver {

    private lateinit var listener: AbcButton.Listener
    private var lastPressed: AbcButton? = null

    init {
        lifecycle.addObserver(this)
        supplier.setListener(this)
    }

    var isEnabled: Boolean = false
        private set

    fun setListener(listener: AbcButton.Listener) {
        this.listener = listener
    }

    fun enable() {
        isEnabled = true
    }

    fun disable() {
        isEnabled = false
    }

    fun setLastPressed(abcButton: AbcButton?) {
        lastPressed = abcButton
    }

    override fun onButtonEvent(abcButton: AbcButton, pressed: Boolean) {
        if (pressed) {
            listener.onAbcButton(abcButton)
        }
    }

    @Throws(Exception::class)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun close() {
        supplier.close()
    }
}
