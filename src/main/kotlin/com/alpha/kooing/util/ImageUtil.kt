package com.alpha.kooing.util

import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater

class ImageUtil {
    companion object {
        fun compressImage(bytes: ByteArray): ByteArray {
            val deflater = Deflater(Deflater.BEST_COMPRESSION)
            deflater.setInput(bytes)
            deflater.finish()

            val outputStream = ByteArrayOutputStream(bytes.size)
            val buffer = ByteArray(1024)
            while (!deflater.finished()) {
                val count = deflater.deflate(buffer)
                outputStream.write(buffer, 0, count)
            }
            try {
                outputStream.close()
            }catch (e: Exception) {
                e.printStackTrace()
            }
            return outputStream.toByteArray()
        }

        fun decompressImage(bytes: ByteArray): ByteArray {
            val inflater = Inflater()
            inflater.setInput(bytes)
            val buffer = ByteArray(1024)
            val outputStream = ByteArrayOutputStream(bytes.size)
            try {
                while (!inflater.finished()) {
                    val count = inflater.inflate(buffer)
                    outputStream.write(buffer, 0, count)
                }
                outputStream.close()
            }catch (e: Exception) {
                e.printStackTrace()
            }
            return outputStream.toByteArray()
        }
    }
}