package org.ibadalrahman.prayertimes.repository.data.remote.responses

import com.google.gson.annotations.SerializedName

data class WeekHadithResponse (
    @SerializedName("hadith") val hadith: String,
    @SerializedName("note") val note: String?
)
