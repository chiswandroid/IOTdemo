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

import com.chisw.iotdemo.things.supplier.BaseSupplier

interface DigitDisplaySupplier : BaseSupplier {

    /**
     * Display info
     *
     * @param number [Double]
     */
    fun display(number: Double)

    /**
     * Display info
     *
     * @param text [String]
     */
    fun display(text: String)
}
