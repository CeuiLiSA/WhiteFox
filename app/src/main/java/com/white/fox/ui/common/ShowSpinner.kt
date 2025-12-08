package com.white.fox.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.white.fox.R
import com.white.fox.ui.setting.localizedString

@Composable
fun ShowSpinner(
    state: SpinnerState = rememberSpinnerState(),
    blockHeight: Dp = 120.dp
) {
    if (!state.visible) return

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val minWidth = screenWidth * 130 / 375F


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
                },
            contentAlignment = Alignment.Center
        ) {
            // Spinner 内容
            Box(
                modifier = Modifier
                    .height(blockHeight)
                    .widthIn(min = minWidth)
                    .background(
                        color = Color.Black, // 纯黑色
                        shape = RoundedCornerShape(12.dp) // 圆角
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        strokeWidth = 5.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = localizedString(R.string.content_loading),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

class SpinnerState internal constructor(
    initialVisible: Boolean,
    private val _setVisible: (Boolean) -> Unit
) {
    var visible by mutableStateOf(initialVisible)
        private set

    fun show() = _setVisible(true)
    fun hide() = _setVisible(false)
}

@Composable
fun rememberSpinnerState(): SpinnerState {
    val (visible, setVisible) = remember { mutableStateOf(false) }
    return SpinnerState(visible, setVisible)
}
