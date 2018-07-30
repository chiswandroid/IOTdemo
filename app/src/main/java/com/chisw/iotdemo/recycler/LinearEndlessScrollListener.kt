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

abstract class LinearEndlessScrollListener : RecyclerView.OnScrollListener {

    protected val linearLayoutManager: LinearLayoutManager
    private var previousTotal = 0
    private var loading = true
    private var visibleThreshold: Int = 0

    constructor(linearLayoutManager: LinearLayoutManager, visibleThreshold: Int) {
        this.linearLayoutManager = linearLayoutManager
        this.visibleThreshold = visibleThreshold
    }

    constructor(linearLayoutManager: LinearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager
        this.visibleThreshold = 10
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = recyclerView!!.childCount
        val totalItemCount = linearLayoutManager.itemCount
        val firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (previousTotal >= totalItemCount) {
            //reset counter if previous data size is larger then total items size (REFRESH)
            previousTotal = 0
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {

            loading = true
        }
    }
}
