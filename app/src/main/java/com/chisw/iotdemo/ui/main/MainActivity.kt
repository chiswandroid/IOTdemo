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
package com.chisw.iotdemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.chisw.iotdemo.R
import com.chisw.iotdemo.mvp.BaseActivity
import com.chisw.iotdemo.things.controller.abcbuttons.AbcButtonsController
import com.chisw.iotdemo.things.controller.buzzer.BuzzerController
import com.chisw.iotdemo.things.controller.digitdisplay.DigitDisplayController
import com.chisw.iotdemo.things.model.AbcButton
import com.chisw.iotdemo.things.supplier.abcbuttons.AbcButtonsSupplierImpl
import com.chisw.iotdemo.things.supplier.buzzer.BuzzerSupplierImpl
import com.chisw.iotdemo.things.supplier.digitdisplay.DigitDisplaySupplierImpl
import com.chisw.iotdemo.ui.browser.BrowserFragment
import com.chisw.iotdemo.ui.feed.FeedFragment
import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.koin.android.ext.android.inject

/**
 * [BaseActivity] subclass. Activity which used as fragments container and listens [CameraGestureSensor] callbacks.
 */

class MainActivity : BaseActivity(), MainView, AbcButton.Listener, CameraGestureSensor.Listener,
        FeedFragment.OnFragmentInteraction, BrowserFragment.OnFragmentInteraction {

    private val presenter: MainPresenter by inject()

    private var abcButtonsController: AbcButtonsController? = null
    private var buzzerController: BuzzerController? = null
    private var digitDisplayController: DigitDisplayController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpControls()
        replaceFeed()
    }

    public override fun onResume() {
        super.onResume()
        if (PermissionUtility.checkCameraPermission(this)) {
            LocalOpenCV(this, this)
        }
    }

    override fun bindPresenter() {
        presenter.view = this
    }

    override fun unbindPresenter() {
        presenter.view = null
    }

    private fun replaceFeed() {
        replaceFragment(FeedFragment.newInstance(), R.id.flContainer, true)
    }

    private fun replaceBrowser(url: String) {
        replaceFragment(BrowserFragment.newInstance(url), R.id.flContainer, false)
    }

    override fun onAbcButton(abcButton: AbcButton) {
        when (abcButton) {
            AbcButton.A -> onBackPressed()
            AbcButton.B -> buzzerController?.playStarWars()
            AbcButton.C -> {
            }
        }
    }

    override fun onGestureUp(caller: CameraGestureSensor?, gestureLength: Long) {
        if (!presenter.isGesturesLocked()) {
            launch(UI) {
                //Toast.makeText(this@MainActivity, "UP", Toast.LENGTH_SHORT).show()
                val fragment = supportFragmentManager.findFragmentById(R.id.flContainer)
                when (fragment) {
                    is FeedFragment -> fragment.moveToNextItem()
                }
            }
        }
    }

    override fun onGestureDown(caller: CameraGestureSensor?, gestureLength: Long) {
        if (!presenter.isGesturesLocked()) {
            launch(UI) {
                //Toast.makeText(this@MainActivity, "DOWN", Toast.LENGTH_SHORT).show()
                val fragment = supportFragmentManager.findFragmentById(R.id.flContainer)
                when (fragment) {
                    is FeedFragment -> fragment.moveToPreviousItem()
                }
            }
        }
    }

    override fun onGestureLeft(caller: CameraGestureSensor?, gestureLength: Long) {
        if (!presenter.isGesturesLocked()) {
            launch(UI) {
                //Toast.makeText(this@MainActivity, "LEFT", Toast.LENGTH_SHORT).show()
                if (supportFragmentManager.findFragmentById(R.id.flContainer) is BrowserFragment) {
                    replaceFeed()
                }
            }
        }
    }

    override fun onGestureRight(caller: CameraGestureSensor?, gestureLength: Long) {
        if (!presenter.isGesturesLocked()) {
            launch(UI) {
                //Toast.makeText(this@MainActivity, "RIGHT", Toast.LENGTH_SHORT).show()
                val fragment = supportFragmentManager.findFragmentById(R.id.flContainer)
                when (fragment) {
                    is FeedFragment -> fragment.openCurrentItem()
                }
            }
        }
    }

    override fun onItemClicked(url: String?) {
        url?.let { replaceBrowser(it) }
    }

    override fun onCancelPressed() {
        replaceFeed()
    }

    private fun setUpControls() {
        abcButtonsController = AbcButtonsController(AbcButtonsSupplierImpl(), lifecycle)
        abcButtonsController?.setListener(this)
        buzzerController = BuzzerController(BuzzerSupplierImpl(), lifecycle)
        digitDisplayController = DigitDisplayController(DigitDisplaySupplierImpl(), lifecycle)
        digitDisplayController?.display(DISPLAY_TEXT)
    }

    companion object {

        const val DISPLAY_TEXT = "IOT DEMO"
        const val PNG = ".png"

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
