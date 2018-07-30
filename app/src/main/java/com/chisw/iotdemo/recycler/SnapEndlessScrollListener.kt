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
package com.chisw.iotdemo.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper

import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE
import android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING

abstract class SnapEndlessScrollListener : LinearEndlessScrollListener {

    private var snapHelper: SnapHelper? = null

    constructor(linearLayoutManager: LinearLayoutManager, visibleThreshold: Int, snapHelper: SnapHelper) : super(linearLayoutManager, visibleThreshold) {
        this.snapHelper = snapHelper
    }

    constructor(linearLayoutManager: LinearLayoutManager, snapHelper: SnapHelper) : super(linearLayoutManager) {
        this.snapHelper = snapHelper
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == SCROLL_STATE_IDLE || newState == SCROLL_STATE_SETTLING || newState == SCROLL_STATE_DRAGGING) {
            val centerView = snapHelper!!.findSnapView(linearLayoutManager)
            if (centerView != null)
                currentPosition(linearLayoutManager.getPosition(centerView))

        }
    }

    abstract fun currentPosition(position: Int)

}
