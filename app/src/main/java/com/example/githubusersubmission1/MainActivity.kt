package com.example.githubusersubmission1

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersubmission1.adapter.UsersAdapter
import com.example.githubusersubmission1.data.ResponseUsersSearch
import com.example.githubusersubmission1.databinding.ActivityMainBinding
import com.example.githubusersubmission1.epiay.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User"
    }

    fun searchUser(usernames:String) {
        val recyclerView = binding.recyclerview1
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        if (usernames.isNullOrEmpty()) {
            recyclerView.adapter = null // kosongkan adapter jika usernames null atau kosong
            progressBar.visibility = View.GONE
            return
        }
        ApiConfig.getService().getSearchUsers(usernames)
            .enqueue(object : Callback<ResponseUsersSearch> {
                override fun onResponse(
                    call: Call<ResponseUsersSearch>,
                    response: Response<ResponseUsersSearch>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseUsers = response.body()
                        val dataUsers = responseUsers?.items
                        val usersAdapter = UsersAdapter(dataUsers)

                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            setHasFixedSize(true)
                            usersAdapter.notifyDataSetChanged()
                            adapter = usersAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUsersSearch>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
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
                searchUser(newText)
                return false
            }
        })
        return true
    }

}