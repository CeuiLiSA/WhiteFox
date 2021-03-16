package ceui.lisa.whitefox.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import ceui.lisa.whitefox.R

import ceui.lisa.whitefox.adapters.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
abstract class BaseAdapter<Bean, Layout: ViewDataBinding>(
        val values: List<Bean>)
    : RecyclerView.Adapter<VH<Layout>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH<Layout> {
        val view = LayoutInflater.from(parent.context)
                .inflate(layout(), parent, false)
        return VH<Layout>(view)
    }

    override fun getItemCount(): Int = values.size

    abstract fun layout(): Int

}