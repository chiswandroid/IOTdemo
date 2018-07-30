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
package com.chisw.iotdemo.things.controller.buzzer

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.chisw.iotdemo.things.controller.BaseController
import com.chisw.iotdemo.things.model.Note
import com.chisw.iotdemo.things.supplier.buzzer.BuzzerSupplier

class BuzzerController(private val supplier: BuzzerSupplier, lifecycle: Lifecycle) : BaseController, LifecycleObserver {

    private var lastBuzzed: Note? = null

    fun lastBuzzedAt(note: Note): Boolean = lastBuzzed === note

    init {
        lifecycle.addObserver(this)
    }

    fun buzz(note: Note) {
        supplier.play(note.pitch)
        lastBuzzed = note
    }

    fun playStarWars(){
        supplier.playStarWars()
    }

    @Throws(Exception::class)
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun close() {
        supplier.close()
    }
}
