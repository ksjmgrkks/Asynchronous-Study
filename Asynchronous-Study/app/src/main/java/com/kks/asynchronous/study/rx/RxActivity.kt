package com.kks.asynchronous.study.rx

import android.content.ClipData
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import autodispose2.autoDispose
import com.jakewharton.rxbinding4.recyclerview.scrollEvents
import com.kks.asynchronous.study.R
import com.kks.asynchronous.study.databinding.ActivityRxBinding

class RxActivity : AppCompatActivity() {
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRxBinding = DataBindingUtil.setContentView(this, R.layout.activity_rx)

        val itemList = (1..50).map{ ClipData.Item("Item $it") }

        binding.recyclerView.adapter = MyAdapter(itemList)

        binding.recyclerView.scrollEvents()
            .autoDispose(scopeProvider)
            .subscribe { event ->
                // 스크롤 이벤트 처리
                Log.d("[kks]","scrollEvents: $event")
            }
    }
}