package com.white.fox.ui.setting

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun localizedString(@StringRes id: Int): String {
    return LocalAppLocaleContext.current.getString(id)
}


@Composable
@ReadOnlyComposable
fun localizedString(@StringRes id: Int, vararg formatArgs: Any): String {
    return LocalAppLocaleContext.current.getString(id, *formatArgs)
}