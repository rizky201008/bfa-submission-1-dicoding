package com.example.githubusersubmission1

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersubmission1.adapter.UsersAdapter
import com.example.githubusersubmission1.adapter.UsersSearchAdapter
import com.example.githubusersubmission1.data.ResponseUserLists
import com.example.githubusersubmission1.data.ResponseUsersSearch
import com.example.githubusersubmission1.databinding.ActivityMainBinding
import com.example.githubusersubmission1.databinding.FragmentFollowersBinding
import com.example.githubusersubmission1.epiay.ApiConfig
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val handler = Handler(Looper.getMainLooper()!!)
    private var runnable: Runnable? = null
    private val DEBOUNCE_DELAY = 500L // waktu tunda 0.5 detik
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
        showUsers()
    }

    private fun showUsers() {
        val recyclerView = binding.recyclerview1
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        recyclerView.adapter = null
        ApiConfig.getService().getUsers()
            .enqueue(object : Callback<ArrayList<ResponseUserLists>>,
                UsersAdapter.OnItemClickListener {
                override fun onResponse(
                    call: Call<ArrayList<ResponseUserLists>>,
                    response: Response<ArrayList<ResponseUserLists>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseFollowings = response.body()
                        val dataUsers = responseFollowings
                        val usersAdapter = dataUsers?.let { UsersAdapter(it) }
                        usersAdapter?.setOnItemClickListener(this)
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            setHasFixedSize(true)
                            usersAdapter?.notifyDataSetChanged()
                            adapter = usersAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponseUserLists>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

                override fun onItemClick(name: String?, avatar: String?) {
                    DetailActivity.login = name.toString()
                    DetailActivity.avatar = avatar.toString()
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    startActivity(intent)
                }

            })
    }

    fun searchUser(usernames: String) {
        val recyclerView = binding.recyclerview1
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        if (usernames.isEmpty()) {
            recyclerView.adapter = null // kosongkan adapter jika usernames null atau kosong
            progressBar.visibility = View.GONE
            showUsers()
            return
        }
        ApiConfig.getService().getSearchUsers(usernames)
            .enqueue(object : Callback<ResponseUsersSearch>,
                UsersSearchAdapter.OnItemClickListener {
                override fun onResponse(
                    call: Call<ResponseUsersSearch>,
                    response: Response<ResponseUsersSearch>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseUsers = response.body()
                        val dataUsers = responseUsers?.items
                        val usersSearchAdapter = UsersSearchAdapter(dataUsers)
                        usersSearchAdapter.setOnItemClickListener(this)
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            setHasFixedSize(true)
                            usersSearchAdapter.notifyDataSetChanged()
                            adapter = usersSearchAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUsersSearch>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onItemClick(name: String?, avatar: String?) {
                    DetailActivity.login = name.toString()
                    DetailActivity.avatar = avatar.toString()
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    startActivity(intent)
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.hintSearch)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                runnable?.let { handler.removeCallbacks(it) } // reset tunda waktu
                runnable = Runnable { searchUser(newText) }.also {
                    handler.postDelayed(it, DEBOUNCE_DELAY)
                }
                return false
            }
        })
        return true
    }

}