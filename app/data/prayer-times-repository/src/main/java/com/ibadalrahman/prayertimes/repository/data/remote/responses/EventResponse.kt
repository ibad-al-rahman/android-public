package com.ibadalrahman.prayertimes.repository.data.remote.responses

import com.google.gson.annotations.SerializedName

data class EventResponse(
    @SerializedName("ar") val ar: String,
    @SerializedName("en") val en: String?
)
