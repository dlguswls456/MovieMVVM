package com.example.moviemvvm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviemvvm.databinding.ActivityMainBinding
import com.example.moviemvvm.ui.single_movie_details.SingleMovieActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn.setOnClickListener {
            val intent = Intent(this, SingleMovieActivity::class.java)
            intent.putExtra("id", 299534)
            this.startActivity(intent)
        }
    }
}