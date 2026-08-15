package org.ibadalrahman.adhkar.collection.domain.entity

/**
 * The collection list has no domain side effects — picking a collection is pure navigation, handled
 * as a [org.ibadalrahman.mvi.MviBoundary.ViewAction]. This type exists only to satisfy the MVI base
 * class's type parameters.
 */
sealed interface AdhkarCollectionAction
