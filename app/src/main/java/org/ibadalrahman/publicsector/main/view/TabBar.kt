package org.ibadalrahman.publicsector.main.view

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.ibadalrahman.publicsector.navigation.TabBarItem

@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    tabBarItems: List<TabBarItem>,
    selectedRoute: String,
    onNavigationSelected: (route: String) -> Unit,
) {
    NavigationBar(modifier = modifier) {
        tabBarItems.forEach { item ->
            val isSelected = selectedRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigationSelected(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.icon,
                        contentDescription = null
                    )
                }
            )
        }
    }
}
