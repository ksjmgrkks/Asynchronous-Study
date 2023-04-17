package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*

fun main() = runBlocking {
    doOneTwoThree()
    println("runBlocking: ${Thread.currentThread().name}")
    println("5!")

    val result = withTimeoutOrNull(10000L){ // 10초 이상 걸리면 강제로 취소
        doCount()
    } ?: false // 실패시 false
    println(result)
}

suspend fun doOneTwoThree() = coroutineScope {
    val job1 = launch {
        try {
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        } finally {
            // 파일, 소켓, DB 등을 close 해주는 코드를 보통 작성함
            println("job1 is finishing!")
        }

        withContext(NonCancellable){
            //반드시 실행되어야하는 Cancel 되어서는 안되는 코드 작성
        }
    }

    val job2 = launch {
        try {
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        } finally {
            println("job2 is finishing!")
        }
    }

    val job3 = launch {
        try {
            println("launch3: ${Thread.currentThread().name}")
            delay(1000L)
            println("2!")
        } finally {
            println("job3 is finishing!")
        }
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