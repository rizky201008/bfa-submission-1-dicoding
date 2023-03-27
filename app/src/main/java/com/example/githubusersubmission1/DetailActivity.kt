package com.example.githubusersubmission1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.githubusersubmission1.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    companion object {
        var login: String = ""
        var avatar: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val usernameDetail = binding.tvLoginDetail
        val avatarDetail = binding.avatarDetail

        usernameDetail.text = login
        Glide.with(avatarDetail)
            .load(avatar)
            .error(R.drawable.ic_launcher_background)
            .into(avatarDetail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> { // menangani klik tombol back
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}