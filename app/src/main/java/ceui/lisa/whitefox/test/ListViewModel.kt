package ceui.lisa.whitefox.test

import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList

open class ListViewModel<Bean> : ViewModel() {
    // TODO: Implement the ViewModel

    var isLoaded = false
    var playList: MutableList<Bean> = ArrayList()

}