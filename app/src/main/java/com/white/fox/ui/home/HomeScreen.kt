package com.white.fox.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.Screen
import com.white.fox.ui.product.Product
import com.white.fox.ui.product.ProductScreen

class HomeScreen : Screen {
    @Composable
    override fun Content(navViewModel: NavViewModel) {
        ContentTemplate("Welcome to Nav3") {
            Button(onClick = { navViewModel.navigate(ProductScreen(Product("123"))) }) {
                Text("Click to navigate")
            }
        }
    }
}