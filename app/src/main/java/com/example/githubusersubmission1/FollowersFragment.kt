package com.example.githubusersubmission1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusersubmission1.adapter.UsersAdapter
import com.example.githubusersubmission1.data.ResponseUserLists
import com.example.githubusersubmission1.databinding.FragmentFollowersBinding
import com.example.githubusersubmission1.epiay.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        val recyclerView = binding.recyclerview2
        val progressBar = binding.progressBar2
        progressBar.visibility = View.VISIBLE
        recyclerView.adapter = null
        ApiConfig.getService().getFollowers(DetailActivity.login)
            .enqueue(object : Callback<ArrayList<ResponseUserLists>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseUserLists>>,
                    response: Response<ArrayList<ResponseUserLists>>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val responseFollowings = response.body()
                        val dataUsers = responseFollowings
                        val usersAdapter = dataUsers?.let { UsersAdapter(it) }
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            setHasFixedSize(true)
                            usersAdapter?.notifyDataSetChanged()
                            adapter = usersAdapter
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResponseUserLists>>, t: Throwable) {
                    Toast.makeText(activity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}