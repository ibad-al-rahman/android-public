package org.ibadalrahman.forceupdate.domain.entity

sealed interface ForceUpdateResult {
    data object UpdateRequested : ForceUpdateResult
}
