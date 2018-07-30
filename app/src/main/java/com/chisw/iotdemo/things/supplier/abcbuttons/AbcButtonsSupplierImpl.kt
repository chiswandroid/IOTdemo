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
package com.chisw.iotdemo.things.supplier.abcbuttons

import com.chisw.iotdemo.things.model.AbcButton
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

class AbcButtonsSupplierImpl : AbcButtonsSupplier {

    private val buttons: Map<Button, AbcButton> = mapOf(
            RainbowHat.openButtonA() to AbcButton.A,
            RainbowHat.openButtonB() to AbcButton.B,
            RainbowHat.openButtonC() to AbcButton.C
    )

    private lateinit var listener: AbcButtonsSupplier.Listener

    init {
        buttons.keys.forEach { it.setOnButtonEventListener(this) }
    }

    override fun setListener(listener: AbcButtonsSupplier.Listener) {
        this.listener = listener
    }

    override fun onButtonEvent(button: Button, pressed: Boolean) {
        buttons[button]?.let { listener.onButtonEvent(it, pressed) }
    }

    @Throws(Exception::class)
    override fun close() {
        buttons.keys.forEach { it.close() }
    }
}
