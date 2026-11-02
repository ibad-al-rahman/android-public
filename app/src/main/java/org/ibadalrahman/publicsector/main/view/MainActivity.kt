package org.ibadalrahman.publicsector.main.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import org.ibadalrahman.publicsector.ui.theme.AppTheme
import org.ibadalrahman.remoteconfig.repository.RemoteConfigRepository
import org.ibadalrahman.settings.repository.SettingsRepository
import org.ibadalrahman.settings.repository.data.domain.Theme
import javax.inject.Inject
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {

    @Inject lateinit var settingsRepository: SettingsRepository
    @Inject lateinit var remoteConfigRepository: RemoteConfigRepository

    private val appUpdateManager: AppUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    // The immediate update flow is a full-screen Play activity; we don't need its result (Play
    // enforces the update), but registering a launcher is the non-deprecated way to start it.
    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val theme by settingsRepository.themeFlow.collectAsState()
            val forceUpdate by remoteConfigRepository.forceUpdateFlow.collectAsState()
            val darkTheme = when(theme) {
                Theme.System -> isSystemInDarkTheme()
                Theme.Light -> false
                Theme.Dark -> true
            }

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    lightScrim = Color.TRANSPARENT,
                    darkScrim = Color.TRANSPARENT,
                    detectDarkMode = { darkTheme }
                )
            )

            AppTheme(darkTheme = darkTheme) {
                RootContent(
                    forceUpdate = forceUpdate,
                    onRequestUpdate = ::runUpdate,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume an interrupted immediate update (Play's contract: the flow must be restarted if the
        // user left the app mid-update). Only acts while an update is already in progress.
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if (info.updateAvailability() ==
                UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                startImmediateUpdate(info)
            }
        }
    }

    /**
     * Launches the Play In-App Update immediate flow. Falls back to the Play Store listing when no
     * newer build is live yet — the force-update flag (not Play Core) is the source of truth for
     * blocking, so the user must always have a way to act.
     */
    private fun runUpdate() {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { info ->
                val canUpdate = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                if (canUpdate) {
                    startImmediateUpdate(info)
                } else {
                    openPlayStoreListing()
                }
            }
            .addOnFailureListener { openPlayStoreListing() }
    }

    private fun startImmediateUpdate(info: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            info,
            updateLauncher,
            AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
        )
    }

    private fun openPlayStoreListing() {
        val marketIntent = Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
        try {
            startActivity(marketIntent)
        } catch (error: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri(),
                )
            )
        }
    }
}
