package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {

    flowOf(1, 2, 3, 4, 5).collect { value ->
        println(value)
    }

    listOf(6, 7, 8, 9, 10).asFlow().collect { value ->
        println(value)
    }

    (11..20).asFlow().collect {// 기본 람다 파라미터는 it
        println(it)
    }
}