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

import android.graphics.Color
import android.support.annotation.ColorInt

enum class Led(val index: Int, @param:ColorInt val color: Int) {
    ZERO(0, Color.YELLOW), ONE(1, Color.GREEN), TWO(2, Color.CYAN),
    THREE(3, Color.BLUE), FOUR(4, Color.LTGRAY), FIVE(5, Color.MAGENTA),
    SIX(6, Color.RED), ALL(7, Color.WHITE), NONE(-1, Color.BLACK)
}
