package ceui.lisa.whitefox.adapters

import android.content.Intent
import ceui.lisa.whitefox.TemplateActivity
import ceui.lisa.whitefox.models.PlaylistBean

class PlayListAdapter(values: List<PlaylistBean>) : BaseAdapter<PlaylistBean>(values) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.idView.text = values[position].trackCount.toString()
        holder.contentView.text = values[position].name
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v?.context, TemplateActivity::class.java)
            intent.putExtra("playlistID", values[position].id)
            v?.context?.startActivity(intent)
        }
    }
}