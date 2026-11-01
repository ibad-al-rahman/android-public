package org.ibadalrahman.forceupdate.domain.entity

sealed interface ForceUpdateAction {
    data object TrackShown : ForceUpdateAction
    data object RequestUpdate : ForceUpdateAction
}
