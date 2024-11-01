package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.dicoding.asclepius.ml.CancerClassification
import org.tensorflow.lite.support.image.TensorImage
import java.io.InputStream


class ImageClassifierHelper(private val context: Context) {

    private lateinit var model: CancerClassification

    fun setupImageClassifier() {
        // TODO Menyiapkan Image Classifier untuk memproses gambar.
        model = CancerClassification.newInstance(context)
    }

    fun classifyStaticImage(imageUri: Uri): Pair<String, Int> {
        val bitmap = getBitmapFromUri(imageUri)
        val image = TensorImage.fromBitmap(bitmap)

        val outputs = model.process(image)
        val probability = outputs.probabilityAsCategoryList

        val bestCategory = probability.maxByOrNull { it.score }

        return if (bestCategory != null) {
            val label = bestCategory.label ?: "Unknown"
            val score = (bestCategory.score * 100).toInt()
            Pair(label, score)
        } else {
            Pair("Unknown", 0)
        }
    }

    fun getConfidenceScore(imageUri: Uri): Float {
        // TODO Mengembalikan confidence score dari hasil prediksi.
        val bitmap = getBitmapFromUri(imageUri)
        val image = TensorImage.fromBitmap(bitmap)

        val outputs = model.process(image)
        val probability = outputs.probabilityAsCategoryList
        return probability.maxByOrNull { it.score }?.score ?: 0f
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap {
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        return BitmapFactory.decodeStream(inputStream)
    }
}