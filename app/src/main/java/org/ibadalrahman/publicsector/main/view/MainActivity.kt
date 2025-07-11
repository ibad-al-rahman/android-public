package org.ibadalrahman.publicsector.main.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel
import org.ibadalrahman.publicsector.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                RootContent()
            }
        }
    }
}
