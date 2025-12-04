package com.white.fox.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ceui.lisa.hermes.cache.PrefStore
import com.blankj.utilcode.util.AppUtils
import com.white.fox.ui.common.LocalDependency

@Composable
fun LogoutConfirm(
    state: LogoutConfirmState = rememberLogoutConfirmState(),
) {
    if (state.visible) {
        val dependency = LocalDependency.current
        LogoutConfirmDialog(
            onConfirm = {
                dependency.sessionManager.updateSession(null)
                PrefStore("HybridRepository").clearAll()
                dependency.prefStore.clearAll()
                AppUtils.relaunchApp()

                state.hide()
            },
            onDismiss = { state.hide() }
        )
    }
}

class LogoutConfirmState(
    val visible: Boolean,
    private val setVisible: (Boolean) -> Unit
) {
    fun show() = setVisible(true)
    fun hide() = setVisible(false)
}

@Composable
fun rememberLogoutConfirmState(): LogoutConfirmState {
    val (visible, setVisible) = remember { mutableStateOf(false) }
    return LogoutConfirmState(visible, setVisible)
}
