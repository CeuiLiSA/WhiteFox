package ceui.lisa.hermes.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ceui.lisa.hermes.R

@Composable
fun DebugLabel() {
    Text(text = localizedString(R.string.hermes_debug))
}