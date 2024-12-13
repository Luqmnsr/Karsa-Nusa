package com.example.karsanusa.view.listener

import android.view.View
import com.example.karsanusa.data.remote.response.ListPredictionsItem

interface ResultListener {
    fun onItemClick(view: View, item: ListPredictionsItem)
}
