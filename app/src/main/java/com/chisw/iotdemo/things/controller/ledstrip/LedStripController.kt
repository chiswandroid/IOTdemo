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
package com.chisw.iotdemo.things.controller.ledstrip

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.chisw.iotdemo.things.controller.BaseController
import com.chisw.iotdemo.things.model.Led
import com.chisw.iotdemo.things.supplier.ledstrip.TURN_OFF_COLOR
import com.chisw.iotdemo.things.supplier.ledstrip.LedStripSupplier
import java.util.*


class LedStripController(private val supplier: LedStripSupplier, lifecycle: Lifecycle) : BaseController, LifecycleObserver{

    private val leds = Led.values().filterNot { it == Led.NONE || it == Led.ALL }
    private val ledStates = HashMap<Led, Boolean>(supplier.getLength())

    init {
        lifecycle.addObserver(this)
    }

    fun light(led: Led) {
        when (led) {
            Led.ALL -> putAll(true)
            else -> ledStates[led] = true
        }
        light()
    }

    fun reset() {
        putAll(false)
        light()
    }

    private fun light() {
        val colors = IntArray(ledStates.size)
        for (led in ledStates.keys) {
            colors[led.index] = if (ledStates[led] as Boolean) led.color else TURN_OFF_COLOR
        }
        supplier.light(colors)
    }

    private fun putAll(all: Boolean) {
        leds.forEach { ledStates[it] = all }
    }

    @Throws(Exception::class)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun close() {
        supplier.apply {
            reset()
            close()
        }
    }
}
