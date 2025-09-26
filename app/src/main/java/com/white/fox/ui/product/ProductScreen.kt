package com.white.fox.ui.product

import androidx.compose.runtime.Composable
import com.white.fox.ui.common.ContentTemplate
import com.white.fox.ui.common.NavViewModel
import com.white.fox.ui.common.Screen
import kotlinx.serialization.Serializable


@Serializable
data class Product(val id: String)

class ProductScreen(private val product: Product) : Screen {
    @Composable
    override fun Content(navViewModel: NavViewModel) {
        ContentTemplate("Product ${product.id}")
    }
}
