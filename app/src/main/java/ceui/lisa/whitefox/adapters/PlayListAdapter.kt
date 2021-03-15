package ceui.lisa.whitefox.adapters

import ceui.lisa.whitefox.models.PlaylistBean

class PlayListAdapter(values: List<PlaylistBean>) : BaseAdapter<PlaylistBean>(values) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.idView.text = values[position].trackCount.toString()
        holder.contentView.text = values[position].name
    }
}