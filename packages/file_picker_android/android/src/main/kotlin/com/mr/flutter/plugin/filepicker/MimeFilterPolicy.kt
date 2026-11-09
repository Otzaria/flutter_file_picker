package com.mr.flutter.plugin.filepicker

/**
 * Builds Android's MIME filters without hiding valid but unrecognized file
 * extensions. The system picker accepts MIME types, not file extensions:
 * if even one requested extension cannot be mapped, applying only the known
 * types would silently make the other files invisible.
 *
 * Kept free of Android dependencies for deterministic JVM testing.
 */
internal object MimeFilterPolicy {
    private const val ANY_MIME_TYPE = "*/*"
    private const val CSV_EXTENSION = "csv"
    private const val CSV_MIME_TYPE = "text/csv"

    fun resolve(
        allowedExtensions: List<String>?,
        mimeForExtension: (String) -> String?
    ): ArrayList<String> {
        if (allowedExtensions.isNullOrEmpty()) {
            return arrayListOf(ANY_MIME_TYPE)
        }

        val mimes = linkedSetOf<String>()
        for (requested in allowedExtensions) {
            val extension = FileExtension.normalize(requested)
            if (extension.isEmpty()) return arrayListOf(ANY_MIME_TYPE)

            val mime = mimeForExtension(extension)
            if (extension == CSV_EXTENSION) {
                if (!mime.isNullOrBlank()) mimes.add(mime)
                mimes.add(CSV_MIME_TYPE)
                continue
            }
            if (mime.isNullOrBlank()) {
                // Fail open instead of silently dropping a supported file type.
                return arrayListOf(ANY_MIME_TYPE)
            }
            mimes.add(mime)
        }
        return ArrayList(mimes)
    }
}
