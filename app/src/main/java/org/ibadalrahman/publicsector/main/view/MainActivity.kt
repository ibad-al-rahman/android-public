package org.ibadalrahman.publicsector.main.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel
import org.ibadalrahman.publicsector.ui.theme.PublicSectorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PublicSectorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Text(text = "Hello World")
                }
            }
        }
    }
}
