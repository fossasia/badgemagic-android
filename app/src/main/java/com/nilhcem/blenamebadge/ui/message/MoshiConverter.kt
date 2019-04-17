package com.nilhcem.blenamebadge.ui.message

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.tape2.ObjectQueue
import okio.buffer
import okio.sink
import java.io.IOException
import java.io.OutputStream

internal class MoshiConverter<T>(moshi: Moshi, type: Class<T>) : ObjectQueue.Converter<T> {
    private val jsonAdapter: JsonAdapter<T> = moshi.adapter(type)

    @Throws(IOException::class)
    override fun from(bytes: ByteArray): T {
        return jsonAdapter.fromJson(okio.Buffer().write(bytes))!!
    }

    @Throws(IOException::class)
    override fun toStream(`val`: T, os: OutputStream) {
        os.sink().buffer().use { sink -> jsonAdapter.toJson(sink, `val`) }
    }
}
