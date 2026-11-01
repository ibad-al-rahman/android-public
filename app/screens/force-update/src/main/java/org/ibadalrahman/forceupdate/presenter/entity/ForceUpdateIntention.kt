package org.ibadalrahman.forceupdate.presenter.entity

sealed interface ForceUpdateIntention {
    data object OnAppear : ForceUpdateIntention
    data object OnTapUpdate : ForceUpdateIntention
}
