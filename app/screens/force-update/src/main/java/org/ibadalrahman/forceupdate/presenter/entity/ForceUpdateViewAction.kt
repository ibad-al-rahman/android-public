package org.ibadalrahman.forceupdate.presenter.entity

sealed interface ForceUpdateViewAction {
    /** Ask the host Activity to launch the Play In-App Update flow. */
    data object LaunchUpdate : ForceUpdateViewAction
}
