package com.kks.asynchronous.study.rx

import android.annotation.SuppressLint
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
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class RxActivity : AppCompatActivity() {
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val db by lazy { AppDatabase.getInstance(this) }
    private val adapter = MyAdapter()
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRxBinding = DataBindingUtil.setContentView(this, R.layout.activity_rx)
        binding.recyclerView.adapter = adapter

        // getAll()을 한 번만 호출하도록 수정
        @SuppressLint("AutoDispose")
        val disposable = db.userDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Toast.makeText(this, "시작", Toast.LENGTH_SHORT).show()
                adapter.getAllUser(it.toMutableList())
                disposables.dispose()
            }, { error ->
                // 에러 처리 로직
                Log.e("[kks]", "userDao() Error occurred: ${error.message}")
            })
        disposables.add(disposable)

        // 버튼 클릭 이벤트 처리
        binding.button.setOnClickListener {
            db.userDao().insertAll(User(name = binding.editText.text.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDispose(scopeProvider)
                .subscribe({
                    Toast.makeText(this, "${binding.editText.text}(이)가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                    adapter.addItem(User(name = binding.editText.text.toString()))
                }, { error ->
                    Log.e("[kks]", "Error occurred: ${error.message}")
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
