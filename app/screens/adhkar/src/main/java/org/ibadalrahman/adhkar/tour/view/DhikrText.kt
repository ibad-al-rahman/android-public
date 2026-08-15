package org.ibadalrahman.adhkar.tour.view

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import org.ibadalrahman.adhkar.domain.entity.Dhikr

/**
 * The dhikr's display text. For Quranic passages, each numbered ayah is followed by its Mushaf-style
 * ornate verse marker (e.g. ﴾٢٥٥﴿) and flows inline; un-numbered opening lines (the Basmalah and
 * Ayat al-Kursi's isti'adhah) sit on their own line above the verse body. Plain adhkar render as-is.
 *
 * Ports the iOS `DhikrView.verseText` + `AyahNumber.formatted`.
 */
fun Dhikr.displayText(): AnnotatedString {
    if (ayat.isEmpty()) return AnnotatedString(arabicText)
    return buildAnnotatedString {
        ayat.forEach { ayah ->
            val number = ayah.number
            if (number == null) {
                // Un-numbered opening line: place it on its own line.
                append(ayah.text)
                append("\n")
            } else {
                append(ayah.text)
                append(" ﴾${number.toArabicIndic()}﴿ ")
            }
        }
    }
}

/** Renders a non-negative integer with Arabic-Indic digits (0-9 → ٠-٩). */
fun Int.toArabicIndic(): String =
    toString().map { char ->
        if (char in '0'..'9') ARABIC_INDIC_DIGITS[char - '0'] else char
    }.joinToString(separator = "")

private val ARABIC_INDIC_DIGITS = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
