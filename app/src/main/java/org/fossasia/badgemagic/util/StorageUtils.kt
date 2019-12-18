package org.fossasia.badgemagic.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import org.fossasia.badgemagic.data.fragments.BadgeConfig
import org.fossasia.badgemagic.data.fragments.CONF_FLASH
import org.fossasia.badgemagic.data.fragments.CONF_HEX_STRINGS
import org.fossasia.badgemagic.data.fragments.CONF_INVERTED
import org.fossasia.badgemagic.data.fragments.CONF_MARQUEE
import org.fossasia.badgemagic.data.fragments.CONF_MODE
import org.fossasia.badgemagic.data.fragments.CONF_SPEED
import org.fossasia.badgemagic.data.fragments.ConfigInfo
import org.json.JSONObject

class StorageUtils(val context: Context) {
    private val EXTERNAL_STORAGE_DIRECTORY = context.getExternalFilesDir(null)?.absolutePath
    private val EXTERNAL_CLIPART_DIRECTORY = "${EXTERNAL_STORAGE_DIRECTORY}ClipArts/"
    private val BADGE_EXTENSION = ".txt"
    private val CLIP_EXTENSION = ".png"

    private fun checkDirectory(): Boolean {
        val directory = File(EXTERNAL_STORAGE_DIRECTORY)
        val directoryClips = File(EXTERNAL_CLIPART_DIRECTORY)
        if (!directory.exists())
            return directory.mkdirs()
        if (!directoryClips.exists())
            return directoryClips.mkdirs()
        return true
    }

    fun saveFile(filename: String, json: String) {
        checkDirectory()
        val saveFile = File(EXTERNAL_STORAGE_DIRECTORY, "$filename$BADGE_EXTENSION")
        saveFile.writeText(json)
    }

    fun getAllFiles(): List<ConfigInfo> {
        checkDirectory()
        val list = mutableListOf<ConfigInfo>()

        val files = File(EXTERNAL_STORAGE_DIRECTORY).listFiles() ?: return list
        for (i in files.indices) {
            if (getFileExtension(files[i].name) == BADGE_EXTENSION) {
                val json = files[i].readText()
                if (checkValidJSON(json))
                    list.add(ConfigInfo(json, files[i].name))
            }
        }
        return list
    }

    private fun getFileExtension(name: String): String {
        val lastIndexOf = name.lastIndexOf(".")
        return if (lastIndexOf == -1) {
            ""
        } else name.substring(lastIndexOf)
    }

    fun deleteFile(fileName: String) {
        checkDirectory()
        val deleteFile = File(EXTERNAL_STORAGE_DIRECTORY, fileName)
        deleteFile.delete()
    }

    fun getAbsolutePathofFiles(fileName: String): String {
        return "$EXTERNAL_STORAGE_DIRECTORY/$fileName"
    }

    fun checkIfFilePresent(fileName: String): Boolean {
        checkDirectory()
        return (File(EXTERNAL_STORAGE_DIRECTORY, "$fileName$BADGE_EXTENSION").exists())
    }

    fun checkIfFilePresent(context: Context, uri: Uri?): Boolean {
        checkDirectory()
        return (File(EXTERNAL_STORAGE_DIRECTORY, getFileName(context, uri ?: Uri.EMPTY)).exists())
    }

    fun copyFileToDirectory(context: Context, uri: Uri?): Boolean {
        checkDirectory()
        val inputStream = context.contentResolver.openInputStream(uri ?: Uri.EMPTY)
        var fileName = getFileName(context, uri ?: Uri.EMPTY)
        if (fileName != null) {
            if (!fileName.contains(BADGE_EXTENSION))
                fileName += BADGE_EXTENSION
            val dest = File(EXTERNAL_STORAGE_DIRECTORY, fileName)
            val jsonString = BufferedReader(InputStreamReader(inputStream)).readLine()
            if (checkValidJSON(jsonString)) {
                dest.writeText(jsonString)
                return true
            }
        }
        return false
    }

    private fun checkValidJSON(jsonString: String): Boolean {
        return try {
            val obj = JSONObject(jsonString)
            return obj.has(CONF_HEX_STRINGS) &&
                    obj.has(CONF_INVERTED) &&
                    obj.has(CONF_MARQUEE) &&
                    obj.has(CONF_FLASH) &&
                    obj.has(CONF_MODE) &&
                    obj.has(CONF_SPEED)
        } catch (e: Exception) {
            false
        }
    }

    fun getFileName(context: Context, uriOriginal: Uri): String? {
        var uri = uriOriginal
        var result: String

        if (uri.scheme != null && uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (it != null && it.moveToFirst()) {
                    var index = it.getColumnIndex("_data")
                    if (index == -1)
                        index = it.getColumnIndex("_display_name")
                    result = it.getString(index)
                    uri = Uri.parse(result)
                }
            }
        }

        result = uri.toString()

        val cut = result.lastIndexOf('/')
        if (cut != -1)
            result = result.substring(cut + 1)
        return result
    }

    fun saveEditedBadge(badgeConfig: BadgeConfig?, fileName: String) {
        checkDirectory()
        val saveFile = File(EXTERNAL_STORAGE_DIRECTORY, fileName)
        saveFile.writeText(MoshiUtils.getAdapter().toJson(badgeConfig))
    }

    fun saveClipArt(bitmap: Bitmap): Boolean {
        checkDirectory()
        val file = File.createTempFile("clip", CLIP_EXTENSION, File(EXTERNAL_CLIPART_DIRECTORY))
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun saveEditedClipart(bitmap: Bitmap, fileName: String): Boolean {
        checkDirectory()
        val file = File(EXTERNAL_CLIPART_DIRECTORY, fileName)
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun getAllClips(): HashMap<String, Drawable?> {
        checkDirectory()
        val list = HashMap<String, Drawable?>()

        val files = File(EXTERNAL_CLIPART_DIRECTORY).listFiles() ?: return list
        for (i in files.indices) {
            if (getFileExtension(files[i].name) == CLIP_EXTENSION) {
                list[files[i].name] = Drawable.createFromPath(files[i].absolutePath)
            }
        }
        return list
    }

    fun getClipartFromPath(filename: String): Drawable? {
        return Drawable.createFromPath(File(EXTERNAL_CLIPART_DIRECTORY, filename).absolutePath)
    }

    fun deleteClipart(fileName: String) {
        checkDirectory()
        val deleteFile = File(EXTERNAL_CLIPART_DIRECTORY, fileName)
        deleteFile.delete()
    }
}
