package com.livefree.merchant.util


import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException


/**
 * Created by root on 28/11/19.
 */
object FileUtils {

    fun convertFileToBase64EncodedString(filePath: String, returnImage64: (image: String) -> Unit) {
        try {
            Log.e("Image filePath", filePath)

            var base64 = Base64.encodeToString(convertFileToByteArray(filePath), Base64.DEFAULT)
            returnImage64(base64)
        } catch (e: Exception) {
            Log.e("Image Error", e.toString())
        }
    }

    fun convertFileToByteArray(filePath: String): ByteArray {
        val file = File(filePath.replace("file:", ""))
        try {
            val fis = FileInputStream(file)
            val bos = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            try {
                var readNum: Int
                while ((fis.read(buf)) != -1) {
                    bos.write(buf, 0, fis.read(buf))
                }
            } catch (e: IOException) {
                Timber.e(e)
            }

            return bos.toByteArray()
        } catch (e: Exception) {
            Timber.e(e)
            return byteArrayOf()
        }

    }

    fun encodeToBase64(image: Bitmap, returnImage64: (image: String) -> Unit) {
        val immagex = Bitmap.createScaledBitmap(image, 350, 350, true)
        val baos = ByteArrayOutputStream()
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val imageEncoded = Base64.encodeToString(b, Base64.DEFAULT)
        Log.e("LOOK", imageEncoded)
        returnImage64("data:image/png;base64," + imageEncoded.replace(" ", "").replace("\n", "")
        )
    }
}