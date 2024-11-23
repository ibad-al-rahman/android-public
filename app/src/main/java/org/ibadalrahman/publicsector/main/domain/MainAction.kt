package org.ibadalrahman.publicsector.main.domain

sealed interface MainAction {
    object Noop: MainAction
}
