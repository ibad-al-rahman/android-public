package org.ibadalrahman.network.interceptor

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ChuckerInterceptor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val chuckerCollector = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )

    fun interceptor(): ChuckerInterceptor =
        ChuckerInterceptor
            .Builder(context = context)
            .collector(collector = chuckerCollector)
            .maxContentLength(length = 250_000L)
            .alwaysReadResponseBody(enable = true)
            .build()
}
