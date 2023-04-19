package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

// Flow는 코틀린에서 쓸 수 있는 비동기 스트림입니다.
fun flowSomething(): Flow<Int> = flow { // 플로우 빌더 함수(flow { })를 이용해 플로우를 만들고
    repeat(10) {
        emit(Random.nextInt(0, 500)) //emit을 통해 값을 방출시킴
        delay(10L)
    }
}

/*
* flow 플로우 빌더 함수를 이용해서 코드블록을 구성하고 emit을 호출해서 스트림에 데이터를 흘려 보냅니다.
* 플로우는 콜드 스트림이기 때문에 요청 측에서 collect를 호출해야 값을 발생하기 시작합니다.
* 1. 콜드 스트림 - 요청이 있는 경우에만 보통 1:1로 값을 전달하기 시작.
* 2. 핫 스트림 - 0개 이상의 상대를 향해 지속적으로 값을 전달.
* */

fun main() = runBlocking {
    flowSomething().collect { value -> //collect를 통해 값을 observe함.
        println(value)
    }
}