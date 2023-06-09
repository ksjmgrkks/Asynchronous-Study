package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

fun simple(): Flow<Int> = flow {
    // 플로우는 현재 코루틴 컨텍스트에서 호출 됩니다.
    log("flow를 시작합니다.")
    for (i in 1..10) {
        emit(i)
    }
    /* flow는 다른 컨텍스트로 옮겨갈 수 없기에 아래 코드로 작성시
       java.lang.IllegalStateException: Flow invariant is violated (Flow 불변이 위반됨) 오류 발생!
      withContext(Dispatchers.Default) {
          for (i in 1..10) {
              delay(100L)
              emit(i)
          }
      } */
}.flowOn(Dispatchers.Default) // flowOn 연산자를 통해 컨텍스트를 올바르게 바꿀 수 있습니다. ↑ Upstream에 영향을 줌
/* 출처: https://developer.android.com/kotlin/flow?hl=ko
*  기본적으로 flow 빌더의 생산자는 수집(collect)하는 코루틴의 CoroutineContext에서 실행됩니다.
*  앞에서 언급한 것처럼 다른 CoroutineContext에서 값을 emit할 수 없습니다.
*  이 동작은 경우에 따라 원하지 않는 동작일 수도 있습니다.
*  예를 들어, 이 주제 전체에 사용된 예에서 저장소 레이어는
*  viewModelScope가 사용하는 Dispatchers.Main에서 작업을 실행하면 안 됩니다.
* */

/*
*  flowOn은 업스트림 흐름의 CoroutineContext를 변경합니다.
*  즉, 생산자 및 중간 연산자가 flowOn 전에(또는 위에) 적용됩니다.
*  다운스트림 흐름(flowOn 이후의 중간 연산자 및 소비자)은 영향을 받지 않으며
*  flow에서 collect하는 데 사용되는 CoroutineContext에서 실행됩니다.
*  flowOn 연산자가 여러 개 있는 경우 각 연산자는 현재 위치에서 업스트림을 변경합니다.
* */

fun main() = runBlocking<Unit> {
    launch(Dispatchers.IO) {
        simple()
            .collect { value -> log("${value}를 받음.") }
    }
}