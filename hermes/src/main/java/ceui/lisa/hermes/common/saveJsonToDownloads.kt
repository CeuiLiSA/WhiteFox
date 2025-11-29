package ceui.lisa.hermes.common

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi

private const val DIR = "Pixiv Session"
private const val FILE_NAME = "pixiv_session.json"

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun saveJsonToDownloads(
    context: Context,
    fileName: String = FILE_NAME,
    jsonContent: String
) {
    val resolver = context.contentResolver
    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val relativePath = "${Environment.DIRECTORY_DOWNLOADS}/${DIR}/"

    // 查询是否已存在同名文件
    resolver.query(
        collection,
        arrayOf(MediaStore.Downloads._ID),
        "${MediaStore.Downloads.DISPLAY_NAME} = ? AND ${MediaStore.Downloads.RELATIVE_PATH} = ?",
        arrayOf(fileName, relativePath),
        null
    )?.use { cursor ->
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
            val uri = ContentUris.withAppendedId(collection, id)
            resolver.delete(uri, null, null) // 删除旧文件
        }
    }

    // 创建新文件
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
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
suspend fun readJsonFromDownloads(context: Context, fileName: String = FILE_NAME): String? {
    val resolver = context.contentResolver

    val selection = "${MediaStore.Downloads.DISPLAY_NAME} = ? AND " +
            "${MediaStore.Downloads.RELATIVE_PATH} = ?"
    val selectionArgs = arrayOf(fileName, "${Environment.DIRECTORY_DOWNLOADS}/${DIR}/")

    val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

    resolver.query(
        collection,
        arrayOf(MediaStore.Downloads._ID),
        selection,
        selectionArgs,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Downloads._ID))
            val uri = ContentUris.withAppendedId(collection, id)
            resolver.openInputStream(uri)?.use { inputStream ->
                return inputStream.bufferedReader().use { it.readText() }
            }
        }
    }

    return null
}

