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
    private val externalStorageDir = context.getExternalFilesDir(null)?.absolutePath
    private val externalClipartDir = "$externalStorageDir/ClipArts/"
    private val badgeExt = ".txt"
    private val clipExt = ".png"

    private fun checkDirectory(): Boolean {
        externalStorageDir?.let {
            val directory = File(it)
            val directoryClips = File(externalClipartDir)
            if (!directory.exists())
                return directory.mkdirs()
            if (!directoryClips.exists())
                return directoryClips.mkdirs()
            return true
        }
        return false
    }

    fun saveFile(filename: String, json: String) {
        checkDirectory()
        externalStorageDir?.let {
            val saveFile = File(it, "$filename$badgeExt")
            saveFile.writeText(json)
        }
    }

    fun getAllFiles(): List<ConfigInfo> {
        checkDirectory()
        val list = mutableListOf<ConfigInfo>()

        externalStorageDir?.let {
            val files = File(externalStorageDir).listFiles() ?: return list
            for (i in files.indices) {
                if (getFileExtension(files[i].name) == badgeExt) {
                    val json = files[i].readText()
                    if (checkValidJSON(json))
                        list.add(ConfigInfo(json, files[i].name))
                }
            }
        }
        return list.asReversed()
    }

    private fun getFileExtension(name: String): String {
        val lastIndexOf = name.lastIndexOf(".")
        return if (lastIndexOf == -1) {
            ""
        } else name.substring(lastIndexOf)
    }

    fun deleteFile(fileName: String) {
        checkDirectory()
        val deleteFile = File(externalStorageDir, fileName)
        deleteFile.delete()
    }

    fun getAbsolutePathofFiles(fileName: String): String {
        return "$externalStorageDir/$fileName"
    }

    fun checkIfFilePresent(fileName: String): Boolean {
        checkDirectory()
        return (File(externalStorageDir, "$fileName$badgeExt").exists())
    }

    fun checkIfFilePresent(context: Context, uri: Uri?): Boolean {
        checkDirectory()
        return (File(externalStorageDir, getFileName(context, uri ?: Uri.EMPTY)).exists())
    }

    fun copyFileToDirectory(context: Context, uri: Uri?): Boolean {
        checkDirectory()
        val inputStream = context.contentResolver.openInputStream(uri ?: Uri.EMPTY)
        var fileName = getFileName(context, uri ?: Uri.EMPTY)
        if (!fileName.contains(badgeExt))
            fileName += badgeExt
        val dest = File(externalStorageDir, fileName)
        inputStream?.let {
            val jsonString = BufferedReader(InputStreamReader(it)).readLine()
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

    fun getFileName(context: Context, uriOriginal: Uri): String {
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
        val saveFile = File(externalStorageDir, fileName)
        saveFile.writeText(MoshiUtils.getAdapter().toJson(badgeConfig))
    }

    fun saveClipArt(bitmap: Bitmap): Boolean {
        checkDirectory()
        val file = File.createTempFile("clip", clipExt, File(externalClipartDir))
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
        val file = File(externalClipartDir, fileName)
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

        val files = File(externalClipartDir).listFiles() ?: return list
        for (i in files.indices) {
            if (getFileExtension(files[i].name) == clipExt) {
                list[files[i].name] = Drawable.createFromPath(files[i].absolutePath)
            }
        }
        return list
    }

    fun getClipartFromPath(filename: String): Drawable? {
        return Drawable.createFromPath(File(externalClipartDir, filename).absolutePath)
    }

    fun deleteClipart(fileName: String) {
        checkDirectory()
        val deleteFile = File(externalClipartDir, fileName)
        deleteFile.delete()
    }
}
