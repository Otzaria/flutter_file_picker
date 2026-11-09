package com.mr.flutter.plugin.filepicker

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

/**
 * Closes every output stream and removes incomplete image data if compression
 * fails or a document provider reports an I/O failure.
 */
internal object CompressedBitmapWriter {
    @Throws(IOException::class)
    fun write(target: File, compress: (OutputStream) -> Boolean) {
        try {
            FileOutputStream(target).use { output ->
                if (!compress(output)) {
                    throw IOException("Unable to write compressed image.")
                }
                output.flush()
            }
        } catch (failure: Exception) {
            target.delete()
            throw failure
        }
    }
}
