package com.udacity.asteroidradar.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "picture_of_day")
data class PictureOfDay(
    @Json(name = "media_type")
    val mediaType: String,

    val title: String,

    @PrimaryKey
    val date: String,

    val url: String?,
) {
    val isImage: Boolean
        get() = mediaType == "image"
}
