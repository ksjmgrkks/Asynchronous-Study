package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun requestFlow(i: Int): Flow<String> = flow {
    emit("$i: First")
    delay(500) // wait 500 ms
    emit("$i: Second")
}

/*
* 플로우에서는 3가지 유형의 flatMap을 지원하고 있습니다.
* flatMapConcat, flatMapMerge, flatMapLatest입니다.
* flatMapConcat은 첫번째 요소에 대해서 플레트닝을 하고 나서 두번째 요소를 합니다.
* */
fun main() = runBlocking<Unit> {

    // flatMapConcat은 첫번째 요소에 대해서 플레트닝을 하고 나서 두번째 요소를 합니다.
    // 하나 처리가 다 완료되고 나서 그 다음 처리를 해야할 때
    val startTime1 = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapConcat {
            requestFlow(it)
        }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime1} ms from start")
        }

    // flatMapMerge는 첫 요소의 플레트닝을 시작하며 이어 다음 요소의 플레트닝을 시작합니다.
    // 굳이 하나 처리가 다 완료되고 나서 그 다음 처리를 할 필요가 없고,
    // 최종 결과를 모두 받는 것이 중요하며, 속도가 중요할 때
    val startTime2 = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapMerge {
            requestFlow(it)
        }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime2} ms from start")
        }

    // flatMapLatest는 다음 요소의 플레트닝을 시작하며 이전에 진행 중이던 플레트닝을 취소합니다.
    // 거래량, 주가처럼 최종 실시간 정보가 중요할 때
    val startTime3 = System.currentTimeMillis() // remember the start time
    (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
        .flatMapLatest {
            requestFlow(it)
        }
        .collect { value -> // collect and print
            println("$value at ${System.currentTimeMillis() - startTime3} ms from start")
        }
}