package com.mr.flutter.plugin.filepicker

/**
 * Choose the smallest power-of-two sample size that constrains both image
 * dimensions. Checking both dimensions independently avoids decoding a
 * huge panoramic image when its short side is already below the limit.
 */
internal object ImageSampling {
    fun forMaximumDimension(width: Int, height: Int, maxDimension: Int): Int {
        require(maxDimension > 0) { "maxDimension must be positive" }
        if (width <= 0 || height <= 0) return 1

        var sampleSize = 1
        while (width / sampleSize > maxDimension ||
            height / sampleSize > maxDimension) {
            sampleSize *= 2
        }
        return sampleSize
    }
}
