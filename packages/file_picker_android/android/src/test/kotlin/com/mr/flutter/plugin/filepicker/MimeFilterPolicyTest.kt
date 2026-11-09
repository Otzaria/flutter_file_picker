package com.mr.flutter.plugin.filepicker

import org.junit.Assert.assertEquals
import org.junit.Test

class MimeFilterPolicyTest {
    private val known = mapOf(
        "pdf" to "application/pdf",
        "txt" to "text/plain",
        "csv" to "text/comma-separated-values"
    )

    @Test
    fun knownExtensionsRetainSpecificFilters() {
        assertEquals(
            listOf("application/pdf", "text/plain"),
            MimeFilterPolicy.resolve(listOf("PDF", "txt")) { known[it] }
        )
    }

    @Test
    fun anUnknownExtensionCannotBeHiddenByKnownOnes() {
        // Otzaria supports rare Word backup formats such as .wbk. The
        // platform MIME database differs between Android devices.
        assertEquals(
            listOf("*/*"),
            MimeFilterPolicy.resolve(listOf("pdf", "wbk", "txt")) { known[it] }
        )
    }

    @Test
    fun unsupportedOnlyAndEmptySelectionsUseWildcard() {
        assertEquals(
            listOf("*/*"),
            MimeFilterPolicy.resolve(listOf("wbk")) { known[it] }
        )
        assertEquals(
            listOf("*/*"),
            MimeFilterPolicy.resolve(emptyList()) { known[it] }
        )
        assertEquals(
            listOf("*/*"),
            MimeFilterPolicy.resolve(null) { known[it] }
        )
    }

    @Test
    fun csvHasAStandardMimeTypeEvenWhenAndroidDoesNotKnowIt() {
        assertEquals(
            listOf("text/csv"),
            MimeFilterPolicy.resolve(listOf("CSV")) { null }
        )
        assertEquals(
            listOf("text/comma-separated-values", "text/csv"),
            MimeFilterPolicy.resolve(listOf("csv")) { known[it] }
        )
    }

    @Test
    fun duplicateMimeMappingsAreDeduplicated() {
        assertEquals(
            listOf("text/plain"),
            MimeFilterPolicy.resolve(listOf("txt", "text")) {
                if (it == "txt" || it == "text") "text/plain" else null
            }
        )
    }
}
