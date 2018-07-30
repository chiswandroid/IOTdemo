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
package com.chisw.iotdemo.ui.feed

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.chisw.iotdemo.BuildConfig
import com.chisw.iotdemo.R
import com.chisw.iotdemo.adapter.NewsAdapter
import com.chisw.iotdemo.models.news.NewsResponse
import com.chisw.iotdemo.models.weather.WeatherResponse
import com.chisw.iotdemo.mvp.BaseFragment
import com.chisw.iotdemo.recycler.SnapEndlessScrollListener
import com.chisw.iotdemo.ui.main.MainActivity
import com.chisw.iotdemo.utils.SystemUtils.getIp
import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor
import kotlinx.android.synthetic.main.fragment_feed.*
import org.koin.android.ext.android.inject

/**
 * [BaseFragment] subclass. Displaying weather and news from different sources
 */

class FeedFragment : BaseFragment(), FeedView {

    private val presenter: FeedPresenter by inject()

    private var adapter: NewsAdapter? = null
    private var adapterPosition: Int = 0

    lateinit var mCallback: FeedFragment.OnFragmentInteraction

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Bind fragment listener
        try {
            mCallback = context as FeedFragment.OnFragmentInteraction
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement OnFragmentInteraction")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        tvNewsSource.setOnClickListener { showNewsDialog() }
        tvBlockGestures.setOnClickListener { presenter.lockGestures() }
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeOnGestureStatus()
        setNewsSource()
        context?.let { presenter.getWeather(getIp(it)) }
        presenter.getNews()
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribeOnGestureStatus()
    }

    override fun setUpWeather(data: WeatherResponse) {
        tvCity.text = data.name
        tvDescription.text = data.weather?.get(0)?.description ?: ""
        tvTemperature.text = getString(R.string.temperature, data.main?.temp.toString())
        tvPressure.text = getString(R.string.pressure, data.main?.pressure.toString())
        tvHumidity.text = getString(R.string.humidity, data.main?.humidity.toString())
        tvWind.text = getString(R.string.wind, data.wind?.speed.toString())
        Glide.with(this)
                .load(BuildConfig.WEATHER_IMAGE_URL + data.weather?.get(0)?.icon + MainActivity.PNG)
                .into(ivWeather)
        llWeather.visibility = View.VISIBLE
    }

    override fun setUpNews(data: NewsResponse) {
        adapter = if (adapter == null) NewsAdapter() else adapter
        adapter?.listener = object : NewsAdapter.ViewHolder.NewsListener {
            override fun onItemClicked(url: String?) {
                mCallback.onItemClicked(url)
            }
        }
        adapter!!.setData(data.articles)
        recyclerView.adapter = adapter
    }

    override fun setBlockText() {
        tvBlockGestures.text = if (!presenter.isGesturesLocked())
            context?.getString(R.string.block_gestures) else
            context?.getString(R.string.unblock_gestures)
    }

    override fun bindPresenter() {
        presenter.view = this
    }

    override fun unbindPresenter() {
        presenter.view = null
    }

    fun openCurrentItem() {
        mCallback.onItemClicked(adapter?.getUrlById(adapterPosition))
    }

    private fun setNewsSource() {
        tvNewsSource.text = presenter.getNewsSource()
        tvNewsSource.visibility = View.VISIBLE
    }

    fun moveToNextItem() {
        adapter?.let {
            if (adapterPosition < it.itemCount)
                recyclerView.smoothScrollToPosition(adapterPosition + 1)
        }
    }

    fun moveToPreviousItem() {
        if (adapterPosition > 0) recyclerView.smoothScrollToPosition(adapterPosition - 1)
    }

    private fun setUpRecycler() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val snapHelper = PagerSnapHelper()
        PagerSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(object : SnapEndlessScrollListener(layoutManager, snapHelper) {
            override fun currentPosition(position: Int) {
                adapterPosition = position
            }
        })
    }


    /**
     * Opens dialog for choosing news source
     */
    private fun showNewsDialog() {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.select_news_source))
            val arrayAdapter = ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice)
            arrayAdapter.addAll(presenter.getNewsSourceList(resources.getStringArray(R.array.news_resources)))
            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            builder.setAdapter(arrayAdapter) { _, which ->
                presenter.saveNewsSource(arrayAdapter.getItem(which))
            }
            builder.show()
        }
    }

    companion object {

        fun newInstance(): FeedFragment {
            return FeedFragment()
        }
    }

    interface OnFragmentInteraction {

        /**
         * Item click callback
         */
        fun onItemClicked(url: String?)
    }
}
