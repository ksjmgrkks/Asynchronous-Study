package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalStdlibApi::class)
fun main() = runBlocking<Unit> {
    val job = launch {// A (부모 컨텍스트)
        launch(Dispatchers.IO + CoroutineName("launch1")) {
            // Dispatchers.IO를 B Context, CoroutineName("launch1")를 C Context라고 했을 때,
            // 최종적으로 CoroutineScope 내의 Coroutine Context는 A + B + C 가 된다.
            // 계속 Coroutine Context를 추가해주고 싶다면 우측에 + 로 적어주면 된다.
            // 만약 우측에 + Job()을 추가해주면 5초 안기다리고 끝난다.
            println("launch1: ${Thread.currentThread().name}")
            println(coroutineContext[CoroutineDispatcher])
            println(coroutineContext[CoroutineName])
            delay(5000L)
        }

        launch(Dispatchers.Default + CoroutineName("launch2")) {
            println("launch2: ${Thread.currentThread().name}")
            println(coroutineContext[CoroutineDispatcher])
            println(coroutineContext[CoroutineName])
            delay(10L)
        }
    }
    job.join()
}