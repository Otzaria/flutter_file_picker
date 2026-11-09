package com.mr.flutter.plugin.filepicker

import java.io.IOException
import java.io.OutputStream

/**
 * Writes the selected file's bytes to a document provider's output stream.
 * A null stream is a valid ContentResolver result and must never be reported
 * as a successful export.
 */
internal object SaveFileWriter {
    @Throws(IOException::class)
    fun write(stream: OutputStream?, bytes: ByteArray) {
        val destination = stream
            ?: throw IOException("Document provider did not return a writable output stream.")
        destination.use {
            it.write(bytes)
            it.flush()
        }
    }
}
