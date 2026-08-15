package org.ibadalrahman.adhkar.domain.entity

/**
 * A single ayah of a Quranic passage: its text and, when it carries one, its number within the
 * surah. [number] is `null` for the Basmalah (and other un-numbered opening lines such as the
 * isti'adhah), which precede a surah's numbered ayat and are not themselves numbered here.
 */
data class Ayah(
    val number: Int?,
    val text: String,
)
