import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

class Prayer(val name: String, val icon: ImageVector = Icons.Default.WbSunny, val time : String = "12:00 PM", var highlight : Boolean = false)
