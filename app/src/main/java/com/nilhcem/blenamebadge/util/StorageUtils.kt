package com.nilhcem.blenamebadge.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.nilhcem.blenamebadge.data.ConfigInfo
import com.nilhcem.blenamebadge.data.CONF_FLASH
import com.nilhcem.blenamebadge.data.CONF_HEX_STRINGS
import com.nilhcem.blenamebadge.data.CONF_MARQUEE
import com.nilhcem.blenamebadge.data.CONF_INVERTED
import com.nilhcem.blenamebadge.data.CONF_MODE
import com.nilhcem.blenamebadge.data.CONF_SPEED
import org.json.JSONObject
import java.io.File
import java.io.BufferedReader
import java.io.InputStreamReader

object StorageUtils {
    private val EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory()
        .absolutePath + "/Badge-Magic/"
    private const val BADGE_EXTENSION = ".txt"

    private fun checkDirectory(): Boolean {
        val directory = File(EXTERNAL_STORAGE_DIRECTORY)
        if (!directory.exists()) {
            return directory.mkdirs()
        }
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

        val files = File(EXTERNAL_STORAGE_DIRECTORY).listFiles()
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
}