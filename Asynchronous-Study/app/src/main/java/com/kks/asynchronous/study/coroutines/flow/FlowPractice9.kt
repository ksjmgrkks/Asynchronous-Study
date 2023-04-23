package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

fun simple1(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i) // emit next value
    }
}

fun simple2(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }
        .map { value ->
            check(value <= 1) { "Crashed on $value" }
            "string $value"
        }

fun simple3(): Flow<String> =
    flow {
        for (i in 1..3) {
            println("Emitting $i")
            emit(i) // emit next value
        }
    }
        .map { value ->
            check(value <= 1) { "Crashed on $value" }
            "string $value"
        }

fun simple4(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    // 예외는 collect을 하는 수집기 측(collect 내부)에서도 try-catch 식을 이용해 할 수 있습니다.
    try {
        simple1().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }

    // 어느 곳에서 발생한 예외라도 처리가 가능합니다. (flow 내부든, collect 하는 수집기 내부든)
    try {
        simple2().collect { value -> println(value) }
    } catch (e: Throwable) {
        println("Caught $e")
    }
    // flow 빌더 코드 블록 내에서 예외를 처리하는 것은 예외 투명성을 어기는 것입니다.
    // flow 외부의 에러를 감지할 수 없기 때문입니다.
    // 플로우에서는 catch 연산자를 이용하는 것을 권합니다.
    // catch 블록에서 예외를 새로운 데이터로 만들어 emit을 하거나,
    // 다시 예외를 던지거나, 로그를 남길 수 있습니다.
    simple3()
        .catch { e -> emit("Caught $e") } // emit on exception
        .collect { value -> println(value) }

    // catch 연산자는 업스트림(catch 연산자를 쓰기 전의 코드)에만 영향을 미치고
    // 다운스트림에는 영향을 미치지 않습니다. 이를 catch 투명성이라 합니다.
    simple4() // upstream
        .catch { e -> println("Caught $e") } // does not catch downstream exceptions
        .collect { value -> //downstream
            check(value <= 1) { "Collected $value" }
            println(value)
        }
}