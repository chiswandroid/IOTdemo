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
package com.chisw.iotdemo.things.supplier.abcleds

import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.google.android.things.pio.Gpio

import java.io.IOException

class AbcLedsSupplierImpl : AbcLedsSupplier {

    private val ledA: Gpio by lazy { RainbowHat.openLedRed() }
    private val ledB: Gpio by lazy { RainbowHat.openLedGreen() }
    private val ledC: Gpio by lazy { RainbowHat.openLedBlue() }

    override fun lightFor(onPadA: Boolean, onPadB: Boolean, onPadC: Boolean) {
        try {
            ledA.value = onPadA
            ledB.value = onPadB
            ledC.value = onPadC
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    override fun close() {
        ledA.close()
        ledB.close()
        ledC.close()
    }
}
