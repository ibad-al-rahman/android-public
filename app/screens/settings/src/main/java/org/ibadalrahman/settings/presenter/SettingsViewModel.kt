package org.ibadalrahman.settings.presenter

import androidx.lifecycle.SavedStateHandle
import org.ibadalrahman.mvi.BaseViewModel
import org.ibadalrahman.mvi.MviBoundary
import org.ibadalrahman.settings.domain.SettingsInteractor
import org.ibadalrahman.settings.domain.entity.SettingsAction
import org.ibadalrahman.settings.domain.entity.SettingsResult
import org.ibadalrahman.settings.presenter.entity.SettingsIntention
import org.ibadalrahman.settings.presenter.entity.SettingsScreenState
import org.ibadalrahman.settings.presenter.entity.SettingsViewAction
import dagger.hilt.android.lifecycle.HiltViewModel
import org.ibadalrahman.base.CoroutineDispatchers
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    coroutineDispatchers: CoroutineDispatchers,
    interactor: SettingsInteractor
): BaseViewModel<
        SettingsScreenState,
        SettingsIntention,
        SettingsViewAction,
        SettingsAction,
        SettingsResult
>(
    savedStateHandle = savedStateHandle,
    coroutineDispatchers = coroutineDispatchers,
    initialState = SettingsScreenState.Empty,
    interactor = interactor
) {
    override fun router(
        intention: SettingsIntention
    ): MviBoundary<SettingsViewAction, SettingsAction, SettingsResult> = when(intention) {
        SettingsIntention.ContactUs -> action(SettingsAction.ContactUs)
        SettingsIntention.Donate -> action(SettingsAction.Donate)
        SettingsIntention.ClearCache -> action(SettingsAction.ClearCache)
        is SettingsIntention.ChangeLanguage -> action(SettingsAction.ChangeLanguage(
            language = intention.language
        ))
        is SettingsIntention.ChangeTheme -> action(SettingsAction.ChangeTheme(
            theme = intention.theme
        ))
    }

    override fun reduce(result: SettingsResult) {
        updateState { SettingsReducer.reduce(prevState = this, result = result) }
    }

    override fun viewActionFrom(result: SettingsResult): SettingsViewAction? = when (result) {
        SettingsResult.NoOp -> null
        SettingsResult.ContactUs -> SettingsViewAction.ContactUs
        SettingsResult.Donate -> SettingsViewAction.Donate
        is SettingsResult.LanguageChanged -> SettingsViewAction.ChangeLanguage(
            language = result.language
        )
        is SettingsResult.ThemeChanged -> null
    }
}
