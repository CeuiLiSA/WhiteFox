package ceui.lisa.whitefox.adapters

import android.view.View
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.databinding.RecyCommentBinding
import ceui.lisa.whitefox.models.CommentsBean
import com.bumptech.glide.Glide
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CommentListAdapter(values: MutableList<CommentsBean>) : BaseAdapter<CommentsBean, RecyCommentBinding>(values) {

    override fun layout(): Int {
        return R.layout.recy_comment
    }

    override fun onBindViewHolder(holder: BindViewHolder<RecyCommentBinding>, position: Int) {
        Glide.with(holder.baseBind.userHead.context)
                .load(values[position].user?.avatarUrl)
                .into(holder.baseBind.userHead)
        holder.baseBind.content.text = values[position].content
        holder.baseBind.userName.text = values[position].user?.nickname
        holder.baseBind.time.text = timeStamp2Date(values[position].time)
        holder.baseBind.likeCount.text = values[position].likedCount.toString()
        if (values[position].beReplied == null) {
            holder.baseBind.replyComment.visibility = View.GONE
        } else {
            holder.baseBind.replyComment.visibility = View.VISIBLE
            holder.baseBind.replyContent.text = values[position].beReplied!![0].content
        }
    }

    fun timeStamp2Date(time: String?): String? {
        if (time == null) {
            return null
        }
        if (time.length != 13 && time.length != 12) {
            return "没有日期数据"
        }
        val timeLong = time.toLong()
        val sdf = SimpleDateFormat("yyyy年MM月dd日 HH:mm") //要转换的时间格式
        val date: Date
        return try {
            date = sdf.parse(sdf.format(timeLong))
            sdf.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}