package com.example.karsanusa.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModelResponse(
    @field:SerializedName("listPredictions")
    val listPredictions: List<ListPredictionsItem>
) : Parcelable

@Parcelize
data class ListPredictionsItem(
    @field:SerializedName("identifier")
    val identifier: String,

    @field:SerializedName("confidence")
    val confidence: Double,

    @field:SerializedName("name")
    val name: String
) : Parcelable
