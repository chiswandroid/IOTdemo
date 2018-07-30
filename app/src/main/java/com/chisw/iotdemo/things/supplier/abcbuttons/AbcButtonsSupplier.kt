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
import com.chisw.iotdemo.things.supplier.BaseSupplier
import com.google.android.things.contrib.driver.button.Button

interface AbcButtonsSupplier : BaseSupplier, Button.OnButtonEventListener {

    /**
     * Set listener for clicks for ABC buttons
     *
     * @param listener [Listener]
     */
    fun setListener(listener: Listener)

    interface Listener {
        fun onButtonEvent(abcButton: AbcButton, pressed: Boolean)
    }
}
