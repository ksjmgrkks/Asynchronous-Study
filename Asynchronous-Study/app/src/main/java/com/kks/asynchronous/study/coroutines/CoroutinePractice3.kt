package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking<Unit> {
    /*
    * 1. Default는 코어 수에 비례하는 스레드 풀에서 수행합니다.
    * 2. IO는 코어 수 보다 훨씬 많은 스레드를 가지는 스레드 풀입니다. IO 작업은 CPU를 덜 소모하기 때문입니다.
    * 3. Unconfined는 어디에도 속하지 않습니다. 지금 시점에는 부모의 스레드에서 수행될 것입니다.
    *    하지만, 예측할 수 없기에 사용하지 않는 것을 추천드립니다.
    * 4. newSingleThreadContext는 항상 새로운 스레드를 만듭니다.
    *    스레드를 새로 할당하더라도 반드시 실행되어야하는 작업에 사용됩니다.
    * */

    launch {
        println("부모의 Context / ${Thread.currentThread().name}")
    }

    launch(Dispatchers.Default) {
        println("Default / ${Thread.currentThread().name}")
    }

    launch(Dispatchers.IO) {
        println("IO / ${Thread.currentThread().name}")
    }

    launch(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
    }

    launch(newSingleThreadContext("kks")) {
        println("newSingleThreadContext / ${Thread.currentThread().name}")
    }

    async {
        println("부모의 Context / ${Thread.currentThread().name}")
    }

    async(Dispatchers.Default) {
        println("Default / ${Thread.currentThread().name}")
    }

    async(Dispatchers.IO) {
        println("IO / ${Thread.currentThread().name}")
    }

    async(Dispatchers.Unconfined) {
        println("Unconfined / ${Thread.currentThread().name}")
    }

    async(newSingleThreadContext("kks async")) {
        println("newSingleThreadContext / ${Thread.currentThread().name}")
    }
}