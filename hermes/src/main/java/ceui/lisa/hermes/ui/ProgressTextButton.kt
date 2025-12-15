package ceui.lisa.hermes.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ProgressTextButton(text: String, longTask: suspend () -> Unit) {
    var isProgressing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .defaultMinSize(minWidth = 64.dp, minHeight = 36.dp)
    ) {
        if (isProgressing) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 3.dp
            )
        } else {
            TextButton(onClick = {
                scope.launch {
                    isProgressing = true
                    try {
                        longTask()
                    } catch (ex: Exception) {
                        Timber.e(ex, "ProgressTextButton")
                    } finally {
                        isProgressing = false
                    }
                }
            }) {
                Text(text = text)
            }
        }
    }
}