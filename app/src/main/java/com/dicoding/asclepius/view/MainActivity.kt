package com.dicoding.asclepius.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import androidx.activity.viewModels


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageClassifier: ImageClassifierHelper
    private val imageViewModel: ImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageClassifier = ImageClassifierHelper(this)
        imageClassifier.setupImageClassifier()

        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            if (imageViewModel.imageUri.value != null) {
                analyzeImage()
            } else {
                showToast("Silakan pilih gambar terlebih dahulu.")
            }
        }

        imageViewModel.imageUri.observe(this) { uri ->
            uri?.let {
                binding.previewImageView.setImageURI(it)
            }
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val selectedImageUri = data?.data
            imageViewModel.setImageUri(selectedImageUri)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        imageViewModel.imageUri.value?.let { uri ->
            val (result, confidenceScore) = imageClassifier.classifyStaticImage(uri)
            showToast("Hasil: $result dengan confidence score: $confidenceScore%")
            moveToResult(result, confidenceScore)
        }
    }

    private fun moveToResult(result: String, confidenceScore: Int) {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("RESULT", result)
            putExtra("CONFIDENCE_SCORE", confidenceScore)
            putExtra("IMAGE_URI", imageViewModel.imageUri.value.toString())
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}