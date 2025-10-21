package ceui.lisa.hermes.viewmodel

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

@MainThread
inline fun <reified VM : ViewModel, ArgT1 : Any> ComponentActivity.constructVM(
    crossinline arg1Producer: () -> ArgT1,
    noinline vmCtr: (ArgT1) -> VM,
): Lazy<VM> {
    val factoryProducer: () -> ViewModelProvider.Factory = {
        val activity = this
        object : AbstractSavedStateViewModelFactory(activity, null) {
            val arg1 = arg1Producer()

            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                return vmCtr(arg1) as T
            }
        }
    }

    return ViewModelLazy(
        VM::class,
        storeProducer = { viewModelStore },
        factoryProducer = factoryProducer,
        extrasProducer = { this.defaultViewModelCreationExtras },
    )
}

