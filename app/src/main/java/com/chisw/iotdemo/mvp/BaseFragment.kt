/*
 * Copyright (C) 2018 CHI Software, Inc.
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
package com.chisw.iotdemo.mvp

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.chisw.iotdemo.R
import com.chisw.iotdemo.mvp.view.BaseView

/**
 * [Fragment] subclass.
 */

abstract class BaseFragment : Fragment(), BaseView {

    /**
     * Binds presenter
     */
    abstract fun bindPresenter()

    /**
     * Unbinds presenter
     */
    abstract fun unbindPresenter()

    override fun handleError(e: Throwable) {
        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        bindPresenter()
    }

    override fun onStop() {
        super.onStop()
        unbindPresenter()
    }
}