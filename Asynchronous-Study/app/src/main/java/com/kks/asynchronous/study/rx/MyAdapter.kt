package com.kks.asynchronous.study.rx

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kks.asynchronous.study.databinding.ItemListBinding
import com.kks.asynchronous.study.rx.room.User

class MyAdapter(private val callback: (User) -> Unit) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var userList: MutableList<User> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = userList[position]
        holder.binding.textView.text = item.name
        holder.binding.deleteButton.setOnClickListener {
            callback(item)
            userList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, userList.size)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getAllUser(allUsers: MutableList<User>){
        userList = allUsers
        notifyDataSetChanged()
    }

    fun addItem(item: User) {
        userList.add(item)
        notifyItemInserted(userList.size - 1)
    }

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}
