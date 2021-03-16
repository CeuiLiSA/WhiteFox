package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.Player
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.ui.PlayerActivity

class SongListAdapter(values: List<Song>) : BaseAdapter<Song>(values) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentView.text = values[position].name
        holder.itemView.setOnClickListener { v ->
            Player.get().setPlayList(values, position)
            val intent = Intent(v.context, PlayerActivity::class.java)
            v.context.startActivity(intent)
        }
    }
}