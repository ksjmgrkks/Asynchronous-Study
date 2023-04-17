package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking {
//        val value1 = async(start = CoroutineStart.LAZY) { getRandom1() } // 바로 실행하지 않고, start 후에 실행함.
//        val value2 = async(start = CoroutineStart.LAZY) { getRandom2() }
//
//        value1.start() // 큐에 수행 예약을 함.
//        value2.start()

        // 그냥 getRandom1(), getRandom2()를 각각 받아오면, 동시에 실행되지 않고,
        // 2초 이상 걸리지만, async-await을 사용하게 되면, 동시에 작업하기 때문에 약 1초정도 걸린다.
    try {
        doSomething()
    } catch (e: IllegalStateException) {
        println("doSomething failed: $e") //4. 최종적으로 올라온 예외를 처리해줌
    }
}

suspend fun getRandom1(): Int {
    try {
        delay(1000L)
        return Random.nextInt(0, 500)
    } finally {
        println("getRandom1 is cancelled.")
    }
}

suspend fun getRandom2(): Int {
    delay(500L)
    println("getRandom2 문제 발생")
    throw IllegalStateException()
}

suspend fun doSomething() = coroutineScope {//3. 부모 코루틴 역시 캔슬
    val value1 = async { getRandom1() } // 2. 형제 코루틴(getRandom2)이 문제 발생 -> 취소
    val value2 = async { getRandom2() } // 1. 자식 코루틴 문제 발생

    // deferred.await은 job.join + 결과 리턴 이라고 보면 됨
    try {
        println("${value1.await()} + ${value2.await()} = ${value1.await() + value2.await()}")
    } finally {
        println("doSomething is cancelled.")
    }
}