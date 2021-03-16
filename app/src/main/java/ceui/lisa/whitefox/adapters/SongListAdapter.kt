package ceui.lisa.whitefox.adapters

import android.content.Intent
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import ceui.lisa.whitefox.Player
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.OnPlayListener
import ceui.lisa.whitefox.ui.PlayerActivity
import java.lang.String

class SongListAdapter(values: List<Song>) : BaseAdapter<Song, RecySongItemBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_song_item
    }

    override fun onBindViewHolder(holder: VH<RecySongItemBinding>, position: Int) {
        holder.baseBind.number.text = position.toString()
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
            Player.get().setPlayList(values, position, object :OnPlayListener(){
                override fun onPrepared() {
                    val intent = Intent(v.context, PlayerActivity::class.java)
                    v.context.startActivity(intent)
                }

                override fun beforePrepared() {

                }
            })
        }
    }


}