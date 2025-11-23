package org.ibadalrahman.settings.repository.data.domain

enum class Theme(val code: Int) {
    System(-1), // AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    Light(1),   // AppCompatDelegate.MODE_NIGHT_NO
    Dark(2);    // AppCompatDelegate.MODE_NIGHT_YES

    companion object {
        fun from(code: Int): Theme? = entries.find { it.code == code }
    }
}
