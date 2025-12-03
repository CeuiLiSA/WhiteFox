package com.white.fox.ui.prime

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.white.fox.R
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.RefreshTemplate
import com.white.fox.ui.common.constructVM
import com.white.fox.ui.setting.localizedString

@Composable
fun PrimeHotScreen() = PageScreen(localizedString(R.string.sorted_by_popular)) {
    val context = LocalContext.current
    val viewModel = constructVM({ (context as ComponentActivity).assets }) { assets ->
        PrimeHotViewModel(assets)
    }
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