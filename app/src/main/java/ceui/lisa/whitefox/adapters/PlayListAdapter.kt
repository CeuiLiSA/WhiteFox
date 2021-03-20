package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.databinding.RecyPlaylistItemBinding
import ceui.lisa.whitefox.models.PlaylistBean
import com.bumptech.glide.Glide

class PlayListAdapter(values: MutableList<PlaylistBean>) : BaseAdapter<PlaylistBean, RecyPlaylistItemBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_playlist_item
    }

    override fun onBindViewHolder(holder: BindViewHolder<RecyPlaylistItemBinding>, position: Int) {
        holder.baseBind.songName.text = values[position].name
        holder.baseBind.songAuthor.text = values[position].trackCount.toString() + "首"
        Glide.with(holder.itemView.context).load(values[position].coverImgUrl).into(holder.baseBind.playlistPhoto)
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v?.context, TemplateActivity::class.java)
            intent.putExtra("playlistID", values[position].id)
            v?.context?.startActivity(intent)
        }
    }
}