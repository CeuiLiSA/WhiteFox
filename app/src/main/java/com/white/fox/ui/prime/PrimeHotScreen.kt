package com.white.fox.ui.prime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.white.fox.R
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.setting.localizedString

@Composable
fun PrimeHotScreen() = PageScreen(localizedString(R.string.sorted_by_popular)) {
    val viewModel = viewModel<PrimeHotViewModel>()
    RefreshTemplate(viewModel) { value, _ ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                value.size,
                key = { index -> value[index].tag.objectUniqueId }) { index -> PrimeTagItem(value[index]) }
        }
    }
}