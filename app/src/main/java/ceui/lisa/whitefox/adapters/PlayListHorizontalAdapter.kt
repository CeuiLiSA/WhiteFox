package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.databinding.RecyPlaylistHorizontalItemBinding
import ceui.lisa.whitefox.databinding.RecyPlaylistItemBinding
import ceui.lisa.whitefox.models.PlaylistBean
import com.bumptech.glide.Glide

class PlayListHorizontalAdapter(values: List<PlaylistBean>) : BaseAdapter<PlaylistBean, RecyPlaylistHorizontalItemBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_playlist_horizontal_item
    }

    override fun onBindViewHolder(holder: VH<RecyPlaylistHorizontalItemBinding>, position: Int) {
        holder.baseBind.songName.text = values[position].name
        Glide.with(holder.itemView.context).load(values[position].picUrl).into(holder.baseBind.playlistPhoto)
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v?.context, TemplateActivity::class.java)
            intent.putExtra("playlistID", values[position].id)
            v?.context?.startActivity(intent)
        }
    }
}