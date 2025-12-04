package ceui.lisa.hermes.common

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi

private const val FILE_NAME = "pixiv_session.json"

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun saveJsonToDownloads(
    context: Context,
    fileName: String = FILE_NAME,
    jsonContent: String
) {
    val resolver = context.contentResolver
    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    val relativePath = Environment.DIRECTORY_DOWNLOADS

    // 查询是否已存在同名文件
    resolver.query(
        collection,
        arrayOf(MediaStore.Downloads._ID),
        "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?",
        arrayOf(fileName, "$relativePath/"),
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
            val uri = ContentUris.withAppendedId(collection, id)
            resolver.delete(uri, null, null)
        }
    }

    // 创建新文件
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.RELATIVE_PATH, "$relativePath/")  // 🔥 使用系统允许的目录
        put(MediaStore.Downloads.MIME_TYPE, "application/json")
        put(MediaStore.Downloads.IS_PENDING, 1)
    }

    val uri = resolver.insert(collection, values)
    uri?.let {
        resolver.openOutputStream(it)?.use { outStream ->
            outStream.write(jsonContent.toByteArray())
        }
        values.clear()
        values.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(it, values, null, null)
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
suspend fun readJsonFromDownloads(
    context: Context,
    fileName: String = FILE_NAME
): String? {
    val resolver = context.contentResolver

    // 现在只放在 Download 根目录
    val relativePath = "${Environment.DIRECTORY_DOWNLOADS}/"

    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    val cursor = resolver.query(
        collection,
        arrayOf(MediaStore.Downloads._ID),
        "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?",
        arrayOf(fileName, relativePath),
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Downloads._ID))
            val uri = ContentUris.withAppendedId(collection, id)
            return resolver.openInputStream(uri)?.use { inputStream ->
                inputStream.bufferedReader().readText()
            }
        }
    }

    return null
}

