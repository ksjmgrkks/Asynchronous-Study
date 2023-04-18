package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

suspend fun printRandom() {
    delay(500L)
    println(Random.nextInt(0, 500))
}

fun main() {
    /*
    * 어디에도 속하지 않지만 원래부터 존재하는 전역 GlobalScope가 있습니다.
    * 이 전역 스코프를 이용하면 코루틴을 쉽게 수행할 수 있습니다.
    * Thread.sleep(1000L)를 쓴 까닭은 main이 runBlocking이 아니기 때문입니다.
    * delay 메서드를 수행할 수 없습니다.GlobalScope는 어떤 계층에도 속하지 않고 영원히 동작하게 된다는 문제점이 있습니다.
    * 프로그래밍에서 전역 객체를 잘 사용하지 않는 것 처럼 GlobalScope도 잘 사용하지 않습니다.
    * */
    val job1 = GlobalScope.launch(Dispatchers.IO) {
        launch { printRandom() }
    }
    Thread.sleep(1000L)

    /*
    * GlobalScope보다 권장되는 형식은 CoroutineScope를 사용하는 것입니다.
    *  CoroutineScope는 인자로 CoroutineContext를 받는데 코루틴 엘리먼트를 하나만 넣어도 좋고
    * 이전에 배웠듯 엘리먼트를 합쳐 코루틴 컨텍스트를 만들어도 됩니다.
    * 하나의 코루틴 엘리먼트, 디스패처 Dispatchers.Default만 넣어도 코루틴 컨텍스트가 만들어지기 때문에 이렇게 사용할 수 있습니다.
    * 이제부터 scope로 계층적으로 형성된 코루틴을 관리할 수 있습니다. 우리의 필요에 따라 코루틴 스코프를 관리할 수 있습니다.
    * */
    val scope = CoroutineScope(Dispatchers.Default)
    val job2 = scope.launch(Dispatchers.IO) {
        launch { printRandom() }
    }
    Thread.sleep(1000L)
}