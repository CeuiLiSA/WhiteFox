package ceui.lisa.hermes.common

import android.graphics.BitmapFactory
import java.io.File

fun getImageDimensions(file: File): Pair<Int, Int> {
    val options = BitmapFactory.Options().apply {
        // 设置为 true 只解析图片的宽高，不加载图片到内存中
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(file.absolutePath, options)
    return Pair(options.outWidth, options.outHeight)
}