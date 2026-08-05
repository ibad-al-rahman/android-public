package org.ibadalrahman.services.notifications

/**
 * Pure selection logic for the rolling alarm — no Android dependencies, so it is unit-testable on
 * the JVM. Given all candidate notification instants and the current time, it decides which events
 * form the next batch to fire.
 *
 * Rules:
 * - Only events **strictly after** [nowMillis] are eligible. The `>` (not `>=`) guard means the
 *   alarm that just fired can never re-select itself.
 * - The batch is the earliest eligible event plus any other eligible event within
 *   [COALESCE_WINDOW_MILLIS] of it. Coalescing sidesteps the ~10-minute Doze throttle on
 *   consecutive exact alarms when an adhkar time nearly coincides with a prayer.
 */
object NextEventSelector {

    const val COALESCE_WINDOW_MILLIS = 60_000L

    /**
     * The next batch of events to post, sorted by time, or an empty list when nothing is eligible.
     * The alarm should be armed for the batch's earliest instant.
     */
    fun selectNextBatch(candidates: List<NotificationEvent>, nowMillis: Long): List<NotificationEvent> {
        val eligible = candidates
            .filter { it.timeMillis > nowMillis }
            .sortedBy { it.timeMillis }

        val first = eligible.firstOrNull() ?: return emptyList()

        return eligible.takeWhile { it.timeMillis - first.timeMillis <= COALESCE_WINDOW_MILLIS }
    }
}
