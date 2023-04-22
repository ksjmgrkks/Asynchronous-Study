package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun simpleFunction(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    // 보내는 쪽과 받는 쪽이 모두 바쁘다고 가정해봅시다.
    val time1 = measureTimeMillis {
        simpleFunction().collect { value ->
            delay(300)
            println(value)
        }
    }
    println("Collected in $time1 ms")

    // buffer로 버퍼를 추가해 보내는 측이 소비를 더 이상 기다리지 않게 합니다.
    val time2 = measureTimeMillis {
        simpleFunction().buffer()
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time2 ms")

    // conflate를 이용하면 중간의 값을 융합(conflate)할 수 있습니다.
    // 처리보다 빨리 발생한 데이터의 중간 값들을 누락합니다.
    val time3 = measureTimeMillis {
        simpleFunction().conflate()
            .collect { value ->
                delay(300)
                println(value)
            }
    }
    println("Collected in $time3 ms")

    // conflate와 같이 방출되는 값을 누락할 수도 있지만
    // 수집 측이 느릴 경우 새로운 데이터가 있을 때 수집 측을 종료시키고
    // 새로 시작하는 방법도 있습니다. collectLatest를 사용합니다.
    val time4 = measureTimeMillis {
        simpleFunction().collectLatest { value ->
            println("값 ${value}를 처리하기 시작합니다.")
            delay(300)
            println(value)
            println("처리를 완료하였습니다.")
        }
    }
    println("Collected in $time4 ms")
}