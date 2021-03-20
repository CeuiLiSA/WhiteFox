package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.Player
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.ui.PlayerActivity

class SongListAdapter(values: MutableList<Song>) : BaseAdapter<Song, RecySongItemBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_song_item
    }

    override fun onBindViewHolder(holder: BindViewHolder<RecySongItemBinding>, position: Int) {
        holder.baseBind.number.text = (position + 1).toString()
        holder.baseBind.songName.text = values[position].name
        if (values[position].ar?.size == 1) {
            holder.baseBind.singerName.text = values[position].ar!![0].name + " - " + values[position].al?.name
        } else {
            var autherName = ""
            values[position].ar?.forEach {
                autherName = autherName + it.name + "/"
            }
            holder.baseBind.singerName.text = autherName + " - " + values[position].al?.name
        }


        if (values[position].alia != null && values[position].alia?.size == 1) {
            holder.baseBind.songAlia.text = "(" + values[position].alia!![0] + ")"
        } else {
            holder.baseBind.songAlia.text = ""
        }

        holder.itemView.setOnClickListener { v ->
            Player.get().setPlayList(values, position)
            val intent = Intent(v.context, PlayerActivity::class.java)
            v.context.startActivity(intent)
        }
    }


}