package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.Player
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.models.Song

class SongListAdapter(values: List<Song>) : BaseAdapter<Song>(values) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentView.text = values[position].name
        holder.itemView.setOnClickListener { v ->
            Player.get().setPlayList(values, position)
        }
    }
}