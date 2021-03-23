package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.databinding.RecyPlaylistItemBinding
import ceui.lisa.whitefox.databinding.RecyStringBinding
import ceui.lisa.whitefox.models.PlaylistBean
import com.bumptech.glide.Glide

class LocalSongsAdapter(values: MutableList<String>) : BaseAdapter<String, RecyStringBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_string
    }

    override fun onBindViewHolder(holder: BindViewHolder<RecyStringBinding>, position: Int) {
        holder.baseBind.songName.text = values[position]
    }
}