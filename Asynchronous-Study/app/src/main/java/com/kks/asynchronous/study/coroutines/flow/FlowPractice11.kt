package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun events(): Flow<Int> = (1..3).asFlow().onEach { delay(100) }

fun main() = runBlocking<Unit> {
    events()
        // addEventListener 대신 플로우의 onEach를 사용할 수 있습니다. 이벤트마다 onEach가 대응하는 것입니다.
        .onEach { event -> log("Event: $event") }
        .collect()
    // 하지만 collect가 flow가 끝날 때 까지 기다리는 것이 문제입니다.
    // 이 예시에서는 1,2,3이 다 출력되고, Done이 출력됩니다. -> UI Event를 받는 경우 문제가 될 수 있습니다.
    log("Done")

    events()
        .onEach { event -> log("${coroutineContext[CoroutineName.Key]}Event: $event") }
        // launchIn을 이용하면 별도의 코루틴에서 flow를 런칭할 수 있습니다. Done과 Event가 다른 코루틴에서 동시에 실행됨
        .launchIn(this)
    log("Done")
}