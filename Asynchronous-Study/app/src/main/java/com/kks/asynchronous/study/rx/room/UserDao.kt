package com.kks.asynchronous.study.rx.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Single<List<User>>

    @Insert
    fun insertAll(vararg users: User) : Completable

    @Delete
    fun delete(user: User) : Completable
}