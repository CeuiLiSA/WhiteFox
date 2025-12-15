package ceui.lisa.hermes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


data class MenuItem(
    val title: String,
    val action: suspend () -> Unit
)

@Composable
fun ActionMenu(
    items: List<MenuItem>,
    state: ActionMenuState = rememberActionMenuState(),
) {
    if (!state.visible) return

    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = { state.hide() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // 半透明背景
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .pointerInput(Unit) {
                    detectTapGestures { state.hide() }
                }
        ) {
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 80.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    items.forEach { menuItem ->
                        MenuItemRow(menuItem, coroutineScope, state)
                    }
                }
            }
        }
    }
}

@Composable
private fun MenuItemRow(
    menuItem: MenuItem,
    coroutineScope: CoroutineScope,
    state: ActionMenuState
) {
    Text(
        menuItem.title,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    menuItem.action()
                    state.hide()
                }
            }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

class ActionMenuState internal constructor(
    initialVisible: Boolean,
    private val _setVisible: (Boolean) -> Unit
) {
    var visible by mutableStateOf(initialVisible)
        private set

    fun show() = _setVisible(true)
    fun hide() = _setVisible(false)
}

@Composable
fun rememberActionMenuState(): ActionMenuState {
    val (visible, setVisible) = remember { mutableStateOf(false) }
    return ActionMenuState(visible, setVisible)
}
