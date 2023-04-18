package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

suspend fun printRandom1() {
    delay(1000L)
    println(Random.nextInt(0, 500))
}

suspend fun printRandom2() {
    delay(500L)
    throw ArithmeticException()
}
/*
* 예외를 가장 체계적으로 다루는 방법은 CEH (Coroutine Exception Handler, 코루틴 익셉션 핸들러)를 사용하는 것입니다.
* CoroutineExceptionHandler를 이용해서 우리만의 CEH를 만든 다음 상위 코루틴 빌더의 컨텍스트에 등록합니다.
* CoroutineExceptionHandler에 등록하는 람다에서 첫 인자는 CoroutineContext 두 번째 인자는 Exception입니다.
* 대부분의 경우에는 Exception만 사용하고 나머지(CoroutineContext)는 _로 남겨둡니다.
* + runBlocking에서는 CEH를 사용할 수 없습니다. runBlocking은 자식이 예외로 종료되면 항상 종료되고 CEH를 호출하지 않습니다.
*  fun main() = runBlocking<Unit> {
    val job = launch (ceh) {
        val a = async { getRandom1() }
        val b = async { getRandom2() }
        println(a.await())
        println(b.await())
    }
    job.join()
}
* 위 코드같이 작성은 가능하지만 ceh를 통한 에러 핸들링은 불가능합니다.
* */
val ceh = CoroutineExceptionHandler { _, exception ->
    println("Something happend: $exception")
}

fun main() = runBlocking {
    val scope = CoroutineScope(Dispatchers.IO)
    val job = scope.launch (ceh + CoroutineName("kks")) {
        launch { printRandom1() }
        launch { printRandom2() }
    }
    job.join()
}