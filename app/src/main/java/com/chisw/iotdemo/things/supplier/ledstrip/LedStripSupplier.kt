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

import com.chisw.iotdemo.things.supplier.BaseSupplier

const val TURN_OFF_COLOR = -1

interface LedStripSupplier : BaseSupplier {

    /**
     * Light leds in LedStrip
     *
     * @param colors [IntArray]
     */
    fun light(colors: IntArray)

    /**
     * Get LedStrip length
     *
     * @return [Int] length
     */
    fun getLength(): Int
}
