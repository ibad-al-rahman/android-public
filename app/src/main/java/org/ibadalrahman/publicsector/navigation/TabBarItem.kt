package org.ibadalrahman.publicsector.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
@Stable
class TabBarItem constructor(
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
)

