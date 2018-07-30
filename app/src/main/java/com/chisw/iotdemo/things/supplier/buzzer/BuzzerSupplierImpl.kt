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
package com.chisw.iotdemo.things.supplier.buzzer

import android.os.Handler
import android.os.HandlerThread

import com.google.android.things.contrib.driver.pwmspeaker.Speaker
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat

import java.io.IOException

class BuzzerSupplierImpl : BuzzerSupplier {

    companion object {
        const val e = 329.0
        const val f = 349.0
        const val gS = 415.0
        const val a = 440.0
        const val aS = 455.0
        const val b = 466.0
        const val cH = 523.0
        const val cSH = 554.0
        const val dH = 587.0
        const val dSH = 622.0
        const val eH = 659.0
        const val fH = 698.0
        const val fSH = 740.0
        const val gH = 784.0
        const val gSH = 830.0
        const val aH = 880.0
    }

    private val speaker: Speaker by lazy { RainbowHat.openPiezo() }

    private val handler: Handler by lazy {
        handlerThread.start()
        Handler(handlerThread.looper)
    }

    private val handlerThread: HandlerThread = HandlerThread("BuzzerSupplierImpl")

    override fun play(note: Double) {
        handler.post {
            try {
                speaker.play(note)
                Thread.sleep(300)
                speaker.stop()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun playStarWars(){
        handler.post {
            try {
                firstPart()

                secondPart()

                speaker.play(f)
                Thread.sleep(250)
                speaker.play(gS)
                Thread.sleep(500)
                speaker.play(f)
                Thread.sleep(350)
                speaker.play(a)
                Thread.sleep(125)
                speaker.play(cH)
                Thread.sleep(500)
                speaker.play(a)
                Thread.sleep(375)
                speaker.play(cH)
                Thread.sleep(125)
                speaker.play(eH)
                Thread.sleep(650)

                speaker.stop()
                Thread.sleep(500)

                secondPart()

                speaker.play(f)
                Thread.sleep(250)
                speaker.play(gS)
                Thread.sleep(500)
                speaker.play(f)
                Thread.sleep(375)
                speaker.play(cH)
                Thread.sleep(125)
                speaker.play(a)
                Thread.sleep(500)
                speaker.play(f)
                Thread.sleep(375)
                speaker.play(cH)
                Thread.sleep(125)
                speaker.play(a)
                Thread.sleep(650)

                speaker.stop()

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun firstPart(){
        speaker.play(a)
        Thread.sleep(500)
        speaker.stop()
        Thread.sleep(50)
        speaker.play(a)
        Thread.sleep(500)
        speaker.stop()
        Thread.sleep(50)
        speaker.play(a)
        Thread.sleep(500)
        speaker.play(f)
        Thread.sleep(350)
        speaker.play(cH)
        Thread.sleep(150)
        speaker.play(a)
        Thread.sleep(500)
        speaker.play(f)
        Thread.sleep(350)
        speaker.play(cH)
        Thread.sleep(150)
        speaker.play(a)
        Thread.sleep(650)

        speaker.stop()
        Thread.sleep(500)

        speaker.play(eH)
        Thread.sleep(500)
        speaker.stop()
        Thread.sleep(50)
        speaker.play(eH)
        Thread.sleep(500)
        speaker.stop()
        Thread.sleep(50)
        speaker.play(eH)
        Thread.sleep(500)
        speaker.play(fH)
        Thread.sleep(350)
        speaker.play(cH)
        Thread.sleep(150)
        speaker.play(gS)
        Thread.sleep(500)
        speaker.play(f)
        Thread.sleep(350)
        speaker.play(cH)
        Thread.sleep(150)
        speaker.play(a)
        Thread.sleep(650)

        speaker.stop()
        Thread.sleep(500)
    }

    private fun secondPart(){
        //second section
        speaker.play(aH)
        Thread.sleep(500)
        speaker.play(a)
        Thread.sleep(300)
        speaker.stop()
        speaker.play(a)
        Thread.sleep(150)
        speaker.play(cH)
        Thread.sleep(500)
        speaker.play(gSH)
        Thread.sleep(325)
        speaker.play(gH)
        Thread.sleep(175)
        speaker.play(fSH)
        Thread.sleep(125)
        speaker.play(fH)
        Thread.sleep(125)
        speaker.play(fSH)
        Thread.sleep(250)

        speaker.stop()
        Thread.sleep(325)

        speaker.play(aS)
        Thread.sleep(250)
        speaker.play(dSH)
        Thread.sleep(500)
        speaker.play(dH)
        Thread.sleep(325)
        speaker.play(cSH)
        Thread.sleep(175)
        speaker.play(cH)
        Thread.sleep(125)
        speaker.play(b)
        Thread.sleep(125)
        speaker.play(cH)
        Thread.sleep(250)

        speaker.stop()
        Thread.sleep(325)

    }

    @Throws(Exception::class)
    override fun close() {
        handlerThread.quit()
        speaker.close()
    }
}
