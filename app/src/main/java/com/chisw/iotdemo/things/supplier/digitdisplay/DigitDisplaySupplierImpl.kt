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
package com.chisw.iotdemo.things.supplier.digitdisplay

import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

class DigitDisplaySupplierImpl : DigitDisplaySupplier {

    private val alphanumericDisplay: AlphanumericDisplay by lazy {
        RainbowHat.openDisplay().apply {
            setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
            setEnabled(true)
        }
    }

    var deferred: Deferred<*>? = null

    override fun display(number: Double) {
        display(number.toString())
    }

    override fun display(text: String) {
        deferred?.cancel()
        //Do we need to show moving text?
        if(text.length<5){
            alphanumericDisplay.display(text)
        } else {
            showRunningLine(text)
        }
    }

    /**
     * Moving text with delay
     *
     * @param text
     */
    private fun showRunningLine(text: String){
        val whitespaceString = addSpace(text)
        deferred = async {
            var workString = whitespaceString
            while (workString.isNotEmpty()) {
                alphanumericDisplay.display(workString)
                Thread.sleep(1000)
                workString = if (workString.length == 4) whitespaceString else workString.substring(1, workString.length)
            }
        }
    }

    /**
     * Adding space to text to move text in a nice way
     *
     * @param text
     */
    private fun addSpace(text: String): String {
        val str1 = StringBuilder()
        for (j in 0 until 1) {
            str1.append(" -- ")
        }
        str1.append(text)
        return str1.toString()

    }

    @Throws(Exception::class)
    override fun close() {
        deferred?.cancel()
        alphanumericDisplay.apply {
            clear()
            close()
        }
    }
}
