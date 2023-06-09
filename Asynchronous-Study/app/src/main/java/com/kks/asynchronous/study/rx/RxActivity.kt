package com.kks.asynchronous.study.rx

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import autodispose2.autoDispose
import com.jakewharton.rxbinding4.recyclerview.scrollEvents
import com.kks.asynchronous.study.R
import com.kks.asynchronous.study.databinding.ActivityRxBinding
import com.kks.asynchronous.study.rx.room.AppDatabase
import com.kks.asynchronous.study.rx.room.User
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RxActivity : AppCompatActivity() {
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val db by lazy { AppDatabase.getInstance(this) }
    private var adapter = MyAdapter { user ->
        db.userDao().delete(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(scopeProvider)
            .subscribe({
                Toast.makeText(this, "${user.name}(이)가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }, { error ->
                // 에러 처리 로직
                Log.e("[kks]", "delete user Error occurred: ${error.message}")
            })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRxBinding = DataBindingUtil.setContentView(this, R.layout.activity_rx)
        binding.recyclerView.adapter = adapter

        db.userDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDispose(scopeProvider)
            .subscribe({
                adapter.getAllUser(it.toMutableList())
            }, { error ->
                // 에러 처리 로직
                Log.e("[kks]", "userDao() Error occurred: ${error.message}")
            })

        // 추가 버튼 클릭 이벤트 처리
        binding.button.setOnClickListener {
            db.userDao().insertAll(User(name = binding.editText.text.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(scopeProvider)
                .subscribe({
                    adapter.addItem(User(name = binding.editText.text.toString()))
                    Toast.makeText(this, "${binding.editText.text}(이)가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }, { error ->
                    Log.e("[kks]", "add User Error occurred: ${error.message}")
                })
        }

        // 스크롤 이벤트 처리
        binding.recyclerView.scrollEvents()
            .autoDispose(scopeProvider)
            .subscribe { event ->
                Log.d("[kks]","scrollEvents: $event")
            }
    }
}
