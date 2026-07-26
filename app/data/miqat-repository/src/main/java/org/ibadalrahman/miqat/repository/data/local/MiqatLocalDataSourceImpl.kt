package org.ibadalrahman.miqat.repository.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalConfig
import org.ibadalrahman.miqat.repository.data.domain.MiqatCalculationMethod
import org.ibadalrahman.miqat.repository.di.MIQAT_PREFS
import javax.inject.Inject
import javax.inject.Named

class MiqatLocalDataSourceImpl @Inject constructor(
    @Named(MIQAT_PREFS) private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
) : MiqatLocalDataSource {

    override fun getCalculationMethod(): MiqatCalculationMethod {
        val json = sharedPreferences.getString(KEY_CALCULATION_METHOD, null)
            ?: return MiqatCalculationMethod.default
        return gson.fromJson(json, MiqatCalculationMethodDto::class.java).toDomain()
    }

    override fun saveCalculationMethod(method: MiqatCalculationMethod) {
        val json = gson.toJson(method.toDto())
        sharedPreferences.edit { putString(KEY_CALCULATION_METHOD, json) }
    }

    override fun getRetainedAstronomicalConfig(): AstronomicalConfig? {
        val json = sharedPreferences.getString(KEY_RETAINED_ASTRONOMICAL, null) ?: return null
        return gson.fromJson(json, AstronomicalConfigDto::class.java).toDomain()
    }

    override fun saveRetainedAstronomicalConfig(config: AstronomicalConfig) {
        val json = gson.toJson(config.toDto())
        sharedPreferences.edit { putString(KEY_RETAINED_ASTRONOMICAL, json) }
    }

    private companion object {
        const val KEY_CALCULATION_METHOD = "calculationMethod"
        const val KEY_RETAINED_ASTRONOMICAL = "retainedAstronomicalConfig"
    }
}
