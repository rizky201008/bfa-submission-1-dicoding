package com.example.githubusersubmission1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusersubmission1.R
import com.example.githubusersubmission1.data.ResponseUserLists
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(val dataUsers: ArrayList<ResponseUserLists>) :
    RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {
    private var listener: UsersAdapter.OnItemClickListener? = null
    fun setOnItemClickListener(listener: UsersAdapter.OnItemClickListener) {
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
        holder.loginUsername.text = dataUsers.get(position).login

        Glide.with(holder.imgAvatar)
            .load(dataUsers[position].avatarUrl)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgAvatar)
        holder.itemView.setOnClickListener {
            val name = dataUsers[position].login
            val avatar = dataUsers[position].avatarUrl
            listener?.onItemClick(name, avatar)
        }
    }

    override fun getItemCount(): Int {
        return dataUsers.size
    }

    interface OnItemClickListener {
        fun onItemClick(name: String?, avatar: String?)
    }
}
