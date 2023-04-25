package com.kks.asynchronous.study.rx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kks.asynchronous.study.databinding.ItemListBinding
import com.kks.asynchronous.study.rx.room.User

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private val userList: MutableList<User> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = userList[position]
        holder.binding.textView.text = item.name
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun getAllUser(allUserList: List<User>) {
        val previousSize = userList.size
        userList.addAll(allUserList)
        notifyItemRangeInserted(previousSize, allUserList.size)
    }


    fun addItem(item: User) {
        userList.add(item)
        notifyItemInserted(userList.size - 1)
    }

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}
