package com.weatheriza.ui.location

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.weatheriza.databinding.ActivitySearchBinding

class SearchLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureToolbar()

    }

    private fun configureToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}