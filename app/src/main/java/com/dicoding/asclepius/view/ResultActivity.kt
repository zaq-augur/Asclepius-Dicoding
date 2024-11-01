package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra("RESULT") ?: "Unknown"
        val confidenceScore = intent.getIntExtra("CONFIDENCE_SCORE", 0)
        val imageUriString = intent.getStringExtra("IMAGE_URI")

        binding.resultText.text = "$result $confidenceScore%"

        imageUriString?.let { uriString ->
            val uri = Uri.parse(uriString)
            binding.resultImage.setImageURI(uri)
        }
    }

}