package com.white.fox.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.white.fox.ui.common.LocalDependency
import com.white.fox.ui.common.constructKeyedVM
import timber.log.Timber

@Composable
fun <T> HistoryContent(recordType: Int, cls: Class<T>) {
    val dependency = LocalDependency.current
    val key = "getUserViewHistoryData-${recordType}"
    val viewModel = constructKeyedVM({ key }, {

    }) { _ ->
        HistoryViewModel(dependency.database, recordType, cls)
    }

    val totalFlow by viewModel.totalFlow.collectAsState()
    totalFlow.forEach {
        Timber.d("dsadsadasw2 ${it}")
    }
}