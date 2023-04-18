package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking<Unit> { //증부모
    val job = launch {// 부모
        launch(Job()) {
            // Job() 을 통해 코루틴의 구조화된 동시성 즉, 상속 관계가 끊어진다.
            // 즉, 더이상 자식 취급 안함
            println(coroutineContext[Job])
            println("launch1: ${Thread.currentThread().name}")
            delay(1000L)
            println("3!")
        }

        launch { // 자식
            println(coroutineContext[Job])
            println("launch2: ${Thread.currentThread().name}")
            delay(1000L)
            println("1!")
        }
    }

    delay(500L)
    job.cancelAndJoin() //Job() 이 삽입된 launch는 이 메서드의 영향을 받지 않는다.(자식이 아니니까!)
    delay(1000L)
}