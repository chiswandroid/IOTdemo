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
package com.chisw.iotdemo.things.model

enum class Note(val led: Led, val order: Int, val pitch: Double) {
    DO_LO(Led.ZERO, 0, 261.626), RE(Led.ONE, 1, 293.665),
    MI(Led.TWO, 2, 329.628), FA(Led.THREE, 3, 349.228),
    SO(Led.FOUR, 4, 391.995), LA(Led.FIVE, 5, 440.0),
    SI(Led.SIX, 6, 493.883), DO_HI(Led.ALL, 7, 523.251),
    MISS(Led.NONE, -1, 110.0)
}
