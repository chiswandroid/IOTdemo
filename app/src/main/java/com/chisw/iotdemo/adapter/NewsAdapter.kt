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
package com.chisw.iotdemo.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chisw.iotdemo.R
import com.chisw.iotdemo.models.news.Article
import kotlinx.android.synthetic.main.news_item.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    var news: MutableList<Article> = ArrayList()
    var listener: ViewHolder.NewsListener? = null

    fun setData(data: MutableList<Article>?) {
        data?.let {
            news = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position], listener)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    fun getUrlById(position: Int): String? {
        return news[position].url
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Set up item view
         */
        fun bind(item: Article, listener: NewsListener?) {
            itemView.tvTitle.text = item.title
            itemView.tvDescription.text = item.description
            itemView.tvAuthor.text = item.author
            itemView.tvLink.text = item.url
            itemView.tvPosition.text = (adapterPosition + 1).toString()
            Glide.with(itemView.ivNewsImage)
                    .load(item.urlToImage)
                    .apply(RequestOptions()
                            .placeholder(R.drawable.ic_placeholder_black)
                            .error(R.drawable.ic_placeholder_black))
                    .into(itemView.ivNewsImage)

            itemView.setOnClickListener { listener?.onItemClicked(item.url) }
        }

        interface NewsListener {

            /**
             * Item click callback
             */
            fun onItemClicked(url: String?)
        }
    }
}