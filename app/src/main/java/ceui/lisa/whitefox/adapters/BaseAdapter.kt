package ceui.lisa.whitefox.adapters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import ceui.lisa.whitefox.adapters.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
abstract class BaseAdapter<Bean, Layout : ViewDataBinding>(
        val values: MutableList<Bean>)
    : RecyclerView.Adapter<BindViewHolder<Layout>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindViewHolder<Layout> {
        val view = LayoutInflater.from(parent.context)
                .inflate(layout(), parent, false)
        return BindViewHolder<Layout>(view)
    }

    override fun getItemCount(): Int = values.size

    abstract fun layout(): Int

    public fun clear() {
        Log.d("trace ", "clear")
        val size = values.size
        if (size != 0) {
            values.clear()
            notifyItemRangeRemoved(0, size)
        }
    }
}