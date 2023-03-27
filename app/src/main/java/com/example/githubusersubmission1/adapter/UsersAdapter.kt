package com.example.githubusersubmission1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusersubmission1.R
import com.example.githubusersubmission1.data.ItemsItem
import com.example.githubusersubmission1.data.ResponseUsersSearch
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(val dataUser: List<ItemsItem?>?) :
    RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar = view.findViewById<CircleImageView>(R.id.iv_avatar)
        val loginUsername = view.findViewById<TextView>(R.id.tv_username)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_lists, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.loginUsername.text = dataUser?.get(position)?.login

        Glide.with(holder.imgAvatar)
            .load(dataUser?.get(position)?.avatarUrl)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgAvatar)
        holder.itemView.setOnClickListener {
            val name = dataUser?.get(position)?.login
            val avatar = dataUser?.get(position)?.avatarUrl
            listener?.onItemClick(name,avatar)
        }
    }

    override fun getItemCount(): Int {
        if (dataUser != null) {
            return dataUser.size
        }
        return 0
    }

    interface OnItemClickListener {
        fun onItemClick(name: String?,avatar: String?)
    }

}
