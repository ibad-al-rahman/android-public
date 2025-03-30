package org.ibadalrahman.publicsector.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import org.ibadalrahman.publicsector.main.model.IOUtils

@HiltAndroidApp
class IbadApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        System.setProperty(DEBUG_PROPERTY_NAME, DEBUG_PROPERTY_VALUE_ON)
        IOUtils.filesDir = this.applicationContext.filesDir
    }
}
