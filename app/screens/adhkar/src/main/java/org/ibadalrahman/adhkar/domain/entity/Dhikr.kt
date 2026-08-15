package org.ibadalrahman.adhkar.domain.entity

/**
 * A single dhikr in a tour: its Arabic text (verbatim, not localized) and the number of times it
 * should be repeated ([target]).
 *
 * For Quranic passages, [ayat] holds the individual verses (each with its number) so the UI can
 * render Mushaf-style verse markers and [isVerse] is `true`. For plain adhkar, [ayat] is empty and
 * [arabicText] is the sole source of text.
 */
data class Dhikr(
    val id: String,
    val arabicText: String,
    val target: Int,
    val isVerse: Boolean,
    val ayat: List<Ayah>,
) {
    companion object {
        /**
         * Builds a verse dhikr from its individual [ayat]. [arabicText] is derived by joining the
         * ayah texts, so non-verse consumers (and accessibility) still see the full passage.
         */
        fun verse(id: String, ayat: List<Ayah>, target: Int): Dhikr = Dhikr(
            id = id,
            arabicText = ayat.joinToString(separator = " ") { it.text },
            target = target,
            isVerse = true,
            ayat = ayat,
        )

        /** Builds a plain (non-verse) dhikr from a single block of text. */
        fun plain(id: String, arabicText: String, target: Int): Dhikr = Dhikr(
            id = id,
            arabicText = arabicText,
            target = target,
            isVerse = false,
            ayat = emptyList(),
        )
    }
}
