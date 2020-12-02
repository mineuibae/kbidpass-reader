/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.kbds.kbidpassreader.presentation.users

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kbds.kbidpassreader.domain.model.User
import com.kbds.kbidpassreader.extension.toFormatted

@BindingAdapter("app:userItems")
fun setUserItems(listView: RecyclerView, items: List<User>?) {
    items?.let {
        (listView.adapter as UsersAdapter).submitList(items)
    }
}

@BindingAdapter("createdDate")
fun setCreatedDate(textView: TextView, user: User) {
    with(textView) {
        user.created_at.let { date ->
            if(date != null) {
                text = "생성일 : ${date.toFormatted()}"
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }
}

@BindingAdapter("registeredDate")
fun setRegisteredDate(textView: TextView, user: User) {
    with(textView) {
        user.registered_at.let { date ->
            if(date != null) {
                text = "등록일 : ${date.toFormatted()}"
                visibility = View.VISIBLE
            } else {
                visibility = View.GONE
            }
        }
    }
}