package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.MyPlayer
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.CommentsBean
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.ui.PlayerActivity

class CommentListAdapter(values: MutableList<CommentsBean>) : BaseAdapter<CommentsBean, RecySongItemBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_song_item
    }

    override fun onBindViewHolder(holder: BindViewHolder<RecySongItemBinding>, position: Int) {
        holder.baseBind.songName.text = values[position].content
    }

}