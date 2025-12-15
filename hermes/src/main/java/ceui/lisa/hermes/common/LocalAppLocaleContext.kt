package ceui.lisa.hermes.common

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

val LocalAppLocaleContext = compositionLocalOf<Context> {
    error("No Locale Context provided")
}
