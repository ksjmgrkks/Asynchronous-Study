package com.kks.asynchronous.study.rx

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kks.asynchronous.study.databinding.ItemListBinding

class MyAdapter(private val itemList: List<ClipData.Item>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        holder.binding.textView.text = item.text
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)
}
