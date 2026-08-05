package org.ibadalrahman.settings.calculationmethod.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.ibadalrahman.miqat.Method
import org.ibadalrahman.miqat.parametersForMethod
import org.ibadalrahman.resources.R
import kotlin.math.roundToInt

/**
 * The twilight-angle caption shown under a preset row, e.g. "Fajr 18° · Ishaa 17°".
 *
 * The angles are read from the library's [parametersForMethod]. Interval-based Ishaa (Umm al-Qura)
 * surfaces as a `0°` angle, so it is rendered as the localized "Interval" label instead. Port of
 * iOS `CalculationMethodSelectionView.caption`.
 */
@Composable
internal fun Method.captionLabel(): String {
    val params = parametersForMethod(this)
    val fajr = "${stringResource(R.string.caption_fajr)} ${formatAngle(params.fajrAngle)}"
    val ishaa = if (params.ishaaAngle > 0) {
        "${stringResource(R.string.caption_ishaa)} ${formatAngle(params.ishaaAngle)}"
    } else {
        "${stringResource(R.string.caption_ishaa)} ${stringResource(R.string.caption_interval)}"
    }
    return "$fajr · $ishaa"
}

/** "18°" for whole degrees, "18.5°" otherwise. */
internal fun formatAngle(angle: Double): String {
    val whole = angle.roundToInt().toDouble() == angle
    return if (whole) "${angle.toInt()}°" else "$angle°"
}
