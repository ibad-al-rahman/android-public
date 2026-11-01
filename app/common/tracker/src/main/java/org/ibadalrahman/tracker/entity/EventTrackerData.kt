package org.ibadalrahman.tracker.entity

/**
 * The provider-agnostic shape every analytics event is flattened to before it reaches Firebase.
 * [eventName] is the Analytics event key; [params] are its parameters.
 */
data class EventTrackerData(
    val eventName: String,
    val params: List<Pair<String, Any>> = emptyList(),
)
