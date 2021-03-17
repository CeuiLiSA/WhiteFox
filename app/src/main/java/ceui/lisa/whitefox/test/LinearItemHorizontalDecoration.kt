package ceui.lisa.whitefox.test

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class LinearItemHorizontalDecoration(private val space: Int) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.top = space
        outRect.right = space
        outRect.bottom = space
        if (parent.getChildPosition(view) == 0) {
            outRect.left = DensityUtil.dp2px(24.0f)
        }
    }
}