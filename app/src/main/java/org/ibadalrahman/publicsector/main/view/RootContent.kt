package org.ibadalrahman.publicsector.main.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.ibadalrahman.publicsector.main.presenter.MainActivityViewModel
import org.ibadalrahman.publicsector.ui.theme.PublicSectorTheme

@Composable
fun RootContent(
    navController: NavHostController = rememberNavController(),
    mainViewModel: MainActivityViewModel
) {
    PublicSectorTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
            Text(text = "Hello World")
        }
    }
}
