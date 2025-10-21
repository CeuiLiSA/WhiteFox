package com.white.fox.ui.common

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel, ArgsT> constructVM(
    crossinline argsProducer: () -> ArgsT, crossinline factory: (ArgsT) -> VM
): VM {
    return viewModel(factory = object : ViewModelProvider.Factory {
        val args = argsProducer()

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return factory(args) as T
        }
    })
}


@Composable
inline fun <reified VM : ViewModel, ArgsT> constructKeyedVM(
    crossinline keyProducer: () -> String,
    crossinline argsProducer: () -> ArgsT,
    crossinline factory: (ArgsT) -> VM
): VM {
    return viewModel(
        key = keyProducer(),
        factory = object : ViewModelProvider.Factory {
            val args = argsProducer()

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory(args) as T
            }
        },
    )
}
