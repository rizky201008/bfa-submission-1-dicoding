package com.example.githubusersubmission1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.githubusersubmission1.data.ResponseDetailUser
import com.example.githubusersubmission1.databinding.ActivityDetailBinding
import com.example.githubusersubmission1.epiay.ApiConfig
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        var login: String = ""
        var avatar: String = ""
        var FOLLOWER: String = "0"
        var FOLLOWING: String = "0"
        var NAME: String = "noname"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupViewPager()
        loadImage()
        showDetail()
    }

    private fun setupActionBar() {
        supportActionBar?.title = login
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager() {
        val viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(this)

        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> ""
            }
        }.attach()
    }

    private fun loadImage() {
        Glide.with(binding.avatarDetail)
            .load(avatar)
            .error(R.drawable.ic_launcher_background)
            .into(binding.avatarDetail)
    }

    private fun showDetail() {
        val progressBar = binding.progressBar1
        val following = binding.following
        val follower = binding.follower
        val usernameDetail = binding.tvNameDetail
        progressBar.visibility = View.VISIBLE
        ApiConfig.getService().getDetailUser(login).enqueue(object : Callback<ResponseDetailUser> {
            override fun onResponse(call: Call<ResponseDetailUser>, response: Response<ResponseDetailUser>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val dataUsers = response.body()
                    FOLLOWING = dataUsers?.following.toString()
                    FOLLOWER = dataUsers?.followers.toString()
                    NAME = dataUsers?.name.toString()
                    follower.text = getString(R.string.followers, FOLLOWER)
                    following.text = getString(R.string.following, FOLLOWING)
                    usernameDetail.text = NAME
                }
            }

            override fun onFailure(call: Call<ResponseDetailUser>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
