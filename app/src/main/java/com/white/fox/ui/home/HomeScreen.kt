package com.white.fox.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.Route
import com.white.fox.ui.common.Screen

class HomeScreen : Screen {
    @Composable
    override fun Content(navViewModel: NavViewModel) {
        val homeViewModel: HomeViewModal = viewModel()
        val homeData = homeViewModel.valueContent.resultFlow.collectAsState().value

        ContentTemplate("Welcome to Nav3") {
            if (homeData == null) {
                CircularProgressIndicator()
            } else {
                // 如果有列表
                LazyColumn {
                    homeData.displayList.let { displayList ->
                        items(displayList) { item ->
                            Text(
                                text = item.title ?: "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navViewModel.navigate(Route.IllustDetail(item.id))
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp) // 可选内边距,
                            )
                        }
                    }
                }
            }
        }
    }
}