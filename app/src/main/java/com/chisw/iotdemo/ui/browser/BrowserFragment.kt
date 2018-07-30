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
package com.chisw.iotdemo.ui.browser

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chisw.iotdemo.R
import com.chisw.iotdemo.mvp.BaseFragment
import com.chisw.iotdemo.ui.feed.FeedFragment
import kotlinx.android.synthetic.main.fragment_browser.*
import org.koin.android.ext.android.inject

/**
 * [BaseFragment] subclass. Displaying article by URL.
 *
 * The arguments can be modified to show different news articles.
 */

class BrowserFragment : BaseFragment(), BrowserView {

    private val presenter: BrowserPresenter by inject()

    private lateinit var mCallback: BrowserFragment.OnFragmentInteraction

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Bind fragment listener
        try {
            mCallback = context as BrowserFragment.OnFragmentInteraction
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnFragmentInteraction")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        ivBack.setOnClickListener { mCallback.onCancelPressed() }
        arguments?.let { webView.loadUrl(it.getString(EXTRA_KEY_URL)) }
    }

    override fun bindPresenter() {
        presenter.view = this
    }

    override fun unbindPresenter() {
        presenter.view = null
    }

    companion object {

        const val EXTRA_KEY_URL = "extra_key_url"

        /**
         * Pass new article URL
         *
         * @param url news article URL
         */
        fun newInstance(url: String): BrowserFragment {
            val args = Bundle().apply { putString(EXTRA_KEY_URL, url)}
            return BrowserFragment().apply { arguments = args }
        }
    }

    interface OnFragmentInteraction {

        /**
         * Cancel click callback
         */
        fun onCancelPressed()
    }
}
