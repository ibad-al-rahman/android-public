package org.ibadalrahman.publicsector.main.view
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { RootContent(mainViewModel = mainViewModel) }
    }
}
