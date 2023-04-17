package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*

fun main() = runBlocking {
    doCount()
}

suspend fun doOneTwoThree() = coroutineScope {
    val job1 = launch {
        println("launch1: ${Thread.currentThread().name}")
        delay(1000L) //suspension point
        println("3!")
    }

    val job2 = launch {
        println("launch2: ${Thread.currentThread().name}")
        println("1!")
    }

    val job3 = launch {
        println("launch3: ${Thread.currentThread().name}")
        delay(500L)
        println("2!")
    }

    delay(800L)
    job1.cancel()
    job2.cancel()
    job3.cancel()
    println("4!")
}

suspend fun doCount() = coroutineScope {
    val job1 = launch(Dispatchers.Default) {
        var i = 1
        var nextTime = System.currentTimeMillis() + 100L
        while (i <= 10 && isActive){
            //isActive를 통해 코루틴이 동작할 때만 코드가 동작하게 할 수 있다.
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextTime) {
                println(i)
                nextTime = currentTime + 100L
                i++
            }
        }
    }

    delay(300L)
//    job1.cancel() // 코루틴 취소
//    job1.join() // 코루틴이 실행되고, 취소되는것을 기다려줌
    job1.cancelAndJoin() //cancel과 join을 한꺼번에 하는 코드
    println("doCount Done!")
}