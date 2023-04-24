package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun simple5(): Flow<Int> = (1..3).asFlow()

fun simple6(): Flow<Int> = flow {
    emit(1)
    throw RuntimeException()
}

fun main() = runBlocking<Unit> {

    // 완료를 처리하는 방법 중의 하나는 명령형의 방식으로 finally 블록을 이용하는 것입니다.
    try {
        simple5().collect { value -> println(value) }
    } finally {
        println("Done")
    }

    // onCompletion 연산자를 선언해서 완료를 처리할 수 있습니다.
    simple5()
        .onCompletion { println("Done") }
        .collect { value -> println(value) }

    // onCompletion은 종료 처리를 할 때 예외가 발생되었는지 여부를 알 수 있습니다. try-finally는 예외를 받을 수 없음
    simple6()
        .onCompletion { cause -> if (cause != null) println("Flow completed exceptionally") }
        .catch { cause -> println("Caught exception") }
        .collect { value -> println(value) }
}