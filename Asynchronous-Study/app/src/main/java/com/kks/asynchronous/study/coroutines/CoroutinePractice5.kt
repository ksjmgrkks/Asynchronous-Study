package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() = runBlocking<Unit> {
    val elapsed = measureTimeMillis {
        val job = launch { // 부모
            launch(Job()) { // 자식 1
                println("launch1: ${Thread.currentThread().name}")
                delay(5000L)
            }

            launch { // 자식 2
                println("launch2: ${Thread.currentThread().name}")
                delay(10L)
            }
        }
        job.join() // 상속관계에 있는 모든 코루틴이 끝날때까지 기다려줌
    }
    println(elapsed) //자식 1에 Job()이 없으면 5초정도, 있으면 자식2 완료되면 바로 끝남
}