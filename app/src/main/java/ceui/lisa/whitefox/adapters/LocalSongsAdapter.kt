package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.MyPlayer
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.databinding.RecyPlaylistItemBinding
import ceui.lisa.whitefox.databinding.RecyStringBinding
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.ui.PlayerActivity
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import java.io.File

class LocalSongsAdapter(values: MutableList<File>) : BaseAdapter<File, RecyStringBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_string
    }

    override fun onBindViewHolder(holder: BindViewHolder<RecyStringBinding>, position: Int) {
        holder.baseBind.songName.text = values[position].name
        holder.baseBind.songSize.text = FileUtils.getSize(values[position])
    }
}