package org.ibadalrahman.adhkar.domain.entity

import androidx.annotation.StringRes
import org.ibadalrahman.resources.R

/**
 * A named, addressable set of adhkar. [slug] is the stable identifier used for navigation (and,
 * later, deep links like `adhkar/tour/morning`). Contents are hardcoded in [AdhkarData] for now —
 * trivially movable to a bundled JSON resource or remote source later without touching the features
 * that consume it.
 */
enum class AdhkarCollection(
    val slug: String,
    @StringRes val titleRes: Int,
) {
    Morning(slug = "morning", titleRes = R.string.morning_adhkar),
    Evening(slug = "evening", titleRes = R.string.evening_adhkar);

    /** The dhikr list for this collection. */
    val adhkar: List<Dhikr>
        get() = when (this) {
            Morning -> AdhkarData.morning
            Evening -> AdhkarData.evening
        }

    companion object {
        /** Resolves a collection from its [slug], or `null` if the slug is not one of ours. */
        fun fromSlug(slug: String?): AdhkarCollection? = entries.firstOrNull { it.slug == slug }
    }
}
