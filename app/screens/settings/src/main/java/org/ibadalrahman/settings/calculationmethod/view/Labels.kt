package org.ibadalrahman.settings.calculationmethod.view

import org.ibadalrahman.miqat.Mazhab
import org.ibadalrahman.miqat.Method
import org.ibadalrahman.miqat.repository.data.domain.AstronomicalMethod
import org.ibadalrahman.resources.R

internal val Method.labelRes: Int
    get() = when (this) {
        Method.MUSLIM_WORLD_LEAGUE -> R.string.method_muslim_world_league
        Method.EGYPTIAN -> R.string.method_egyptian
        Method.UMM_AL_QURA -> R.string.method_umm_al_qura
        Method.MOONSIGHTING_COMMITTEE -> R.string.method_moonsighting_committee
        Method.NORTH_AMERICA -> R.string.method_north_america
        Method.SINGAPORE -> R.string.method_singapore
    }

internal val Mazhab.labelRes: Int
    get() = when (this) {
        Mazhab.SHAFI -> R.string.madhab_shafi
        Mazhab.HANAFI -> R.string.madhab_hanafi
    }

internal val AstronomicalMethod.labelRes: Int
    get() = when (this) {
        is AstronomicalMethod.Preset -> method.labelRes
        is AstronomicalMethod.Custom -> R.string.method_custom
    }

/** The six presets offered in the picker, in display order. */
internal val presetMethods: List<Method> = listOf(
    Method.MUSLIM_WORLD_LEAGUE,
    Method.EGYPTIAN,
    Method.UMM_AL_QURA,
    Method.MOONSIGHTING_COMMITTEE,
    Method.NORTH_AMERICA,
    Method.SINGAPORE,
)
