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
package com.chisw.iotdemo.things.supplier.ledstrip

import android.graphics.Color
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import java.io.IOException

class LedStripSupplierImpl : LedStripSupplier {

    private val apa102: Apa102 by lazy {
        RainbowHat.openLedStrip().apply {
            direction = Apa102.Direction.REVERSED
            brightness = 7
        }
    }

    override fun getLength(): Int = RainbowHat.LEDSTRIP_LENGTH

    override fun light(colors: IntArray) {
        val writeColors = IntArray(colors.size)
        colors.forEachIndexed { index, color ->
            writeColors[index] = if (color == TURN_OFF_COLOR) Color.BLACK else color
        }

        try {
            apa102.write(writeColors)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    override fun close() {
        apa102.close()
    }
}
