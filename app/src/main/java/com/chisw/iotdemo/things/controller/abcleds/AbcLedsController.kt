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
package com.chisw.iotdemo.things.controller.abcleds

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.chisw.iotdemo.things.controller.BaseController
import com.chisw.iotdemo.things.model.AbcButton
import com.chisw.iotdemo.things.model.AbcLed
import com.chisw.iotdemo.things.supplier.abcleds.AbcLedsSupplier

class AbcLedsController(private val supplier: AbcLedsSupplier, lifecycle: Lifecycle) : BaseController, AbcLed.Listener, LifecycleObserver {

    private val lit = booleanArrayOf(false, false, false)

    init {
        lifecycle.addObserver(this)
    }

    fun isLightAt(abcLed: AbcLed): Boolean = when (abcLed) {
        AbcLed.A -> lit[0]
        AbcLed.B -> lit[1]
        AbcLed.C -> lit[2]
    }

    override fun lightFor(abcButton: AbcButton) {
        when (abcButton) {
            AbcButton.A -> setLitLeds(true, false, false)
            AbcButton.B -> setLitLeds(false, true, false)
            AbcButton.C -> setLitLeds(false, false, true)
        }
        lightFor(lit[0], lit[1], lit[2])
    }

    override fun reset() {
        setLitLeds(false, false, false)
        lightFor(lit[0], lit[1], lit[2])
    }

    @Throws(Exception::class)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun close() {
        supplier.close()
    }

    private fun lightFor(onPadA: Boolean, onPadB: Boolean, onPadC: Boolean) {
        supplier.lightFor(onPadA, onPadB, onPadC)
    }

    private fun setLitLeds(zero: Boolean, one: Boolean, two: Boolean) {
        lit[0] = zero
        lit[1] = one
        lit[2] = two
    }
}
