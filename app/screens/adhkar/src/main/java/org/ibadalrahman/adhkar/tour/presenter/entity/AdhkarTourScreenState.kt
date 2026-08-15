package org.ibadalrahman.adhkar.tour.presenter.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import org.ibadalrahman.adhkar.domain.entity.AdhkarCollection
import org.ibadalrahman.adhkar.domain.entity.Dhikr

/**
 * The whole tour in one immutable value. [counts] runs parallel to [dhikr] and holds each dhikr's
 * repetition count — counts persist as the user navigates back and forth. [activeIndex] is the
 * current position, or `null` once the tour is finished (which shows the completion screen).
 *
 * Mirrors the iOS `AdhkarTourFeature.State`, flattening its per-dhikr child features into parallel
 * lists on a single state.
 */
@Stable
@Immutable
data class AdhkarTourScreenState(
    val collection: AdhkarCollection?,
    val dhikr: List<Dhikr>,
    val counts: List<Int>,
    val activeIndex: Int?,
) {
    val total: Int get() = dhikr.size

    val isFinished: Boolean get() = collection != null && activeIndex == null

    /** The dhikr currently on screen, or `null` when finished or not yet loaded. */
    val activeDhikr: Dhikr? get() = activeIndex?.let(dhikr::getOrNull)

    /** The active dhikr's current repetition count. */
    val activeCount: Int get() = activeIndex?.let(counts::getOrNull) ?: 0

    /** Whether the active dhikr has reached its repetition target. */
    val isActiveComplete: Boolean
        get() = activeDhikr?.let { activeCount >= it.target } ?: false

    /** 1-based position of the active dhikr, for the "1 / 16" header. `null` once finished. */
    val position: Int? get() = activeIndex?.let { it + 1 }

    /** Progress through the tour: active index over total (full once finished). */
    val progress: Float
        get() = if (total == 0) 0f else (activeIndex ?: total).toFloat() / total.toFloat()

    companion object {
        val Empty = AdhkarTourScreenState(
            collection = null,
            dhikr = emptyList(),
            counts = emptyList(),
            activeIndex = null,
        )
    }
}
