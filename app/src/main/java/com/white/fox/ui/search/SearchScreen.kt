package com.white.fox.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ceui.lisa.models.Tag
import com.white.fox.R
import com.white.fox.ui.common.LocalNavViewModel
import com.white.fox.ui.common.PageScreen
import com.white.fox.ui.common.Route
import com.white.fox.ui.setting.localizedString

@Composable
fun SearchScreen() = PageScreen(localizedString(R.string.button_search)) {
    val viewModel = viewModel<SearchViewModel>()
    val keyword by viewModel.keywordFlow.collectAsState()
    val navViewModel = LocalNavViewModel.current
    Column(
        modifier = Modifier
            .imePadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = keyword,
            onValueChange = { viewModel.updateKeyword(it) },
            label = { Text(localizedString(R.string.search_keyword)) },
            textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 16.dp)
                .verticalScroll(rememberScrollState())
                .height(100.dp)
        )

        Button(
            onClick = {
                navViewModel.navigate(Route.TagDetail(Tag(name = keyword)))
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(localizedString(R.string.button_search))
        }
    }
}