package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

fun main() = runBlocking<Unit> {
    val result = withTimeoutOrNull(500L) {
        flowSomething().collect { value ->
            println(value)
        }
        true
    } ?: false
    if (!result) {
        println("취소되었습니다.")
    }
}