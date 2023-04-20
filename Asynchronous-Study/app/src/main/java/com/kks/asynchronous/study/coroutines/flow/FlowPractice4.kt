package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

fun flowSomething1(): Flow<Int> = flow {
    repeat(10) {
        emit(Random.nextInt(0, 500))
        delay(10L)
        emit(1) // flow 빌더 안에서 emit을 여러번 쓸 수 있습니다.
    }
}

suspend fun someCalc(i: Int): Int {
    delay(10L)
    return i * 2
}

fun main() = runBlocking {

    // 플로우에서 map 연산을 통해 데이터를 가공할 수 있습니다.
    flowSomething1().map {
        "$it $it"
    }.collect { value ->
        println(value)
    }

    // filter 기능을 이용해 짝수만 남겨봅시다.
    (1..20).asFlow().filter {
        (it % 2) == 0 // 술어(predicate)
    }.collect {
        println(it)
    }

    // 만약 홀수만 남기고 싶을 때 술어(predicate)를 수정할 수 도 있습니다.
    // 하지만 술어를 그대로 두고 filterNot을 사용할 수도 있습니다.
    (1..20).asFlow().filterNot {
        (it % 2) == 0
    }.collect {
        println(it)
    }

    // transform 연산자를 이용해 조금 더 유연하게 스트림을 변형할 수 있습니다.
    // 마치 연산자 내부를 flow 빌더 안에서 처리하는 것처럼 쓸 수 있습니다.
    (1..20).asFlow().transform {
        emit(it)
        delay(10L)
        emit(someCalc(it))
    }.collect {
        println(it)
    }

    // take 연산자는 몇개의 수행 결과만 취합니다.
    (1..20).asFlow().transform {
        emit(it)
        emit(someCalc(it))
    }.take(5)
        .collect {
            println(it)
        }

    // takeWhile을 이용해 조건을 만족하는 동안만 값을 가져오게 할 수 있습니다.
    (1..20).asFlow().transform {
        emit(it)
        emit(someCalc(it))
    }.takeWhile {
        it < 15
    }.collect {
        println(it)
    }

    // drop 연산자는 처음 몇개의 결과를 버립니다. take가 takeWhile을 가지듯 dropWhile도 있습니다.
    (1..20).asFlow().transform {
        emit(it)
        emit(someCalc(it))
    }.drop(5)
        .collect {
            println(it)
        }

    /*
    * collect, reduce, fold, toList, toSet과 같은 연산자는
    * 플로우를 끝내는 함수라 종단 연산자(terminal operator)라고 합니다.
    * (반대로 filter, map과 같은 연산자는 중간 연산자(intermediate operator) 라고 하며 연산자 적용 후에도 플로우가 계속됩니다.)
    * reduce는 흔히 map과 reduce로 함께 소개되는 함수형 언어의 오래된 메커니즘입니다.
    * 첫번째 값을 결과에 넣은 후 각 값을 가져와 누진적으로 계산합니다.
    * */
    val value1 = (1..10)
        .asFlow()
        .reduce { a, b ->
            a + b
        }
    println(value1)

    // fold 연산자는 reduce와 매우 유사합니다. 초기값이 있다는 차이만 있습니다.
    val value2 = (1..10)
        .asFlow()
        .fold(10) { a, b ->
            a + b
        }
    println(value2)

    // count의 연산자는 술어를 만족하는 자료의 갯수를 셉니다. 짝수의 갯수를 세어봅시다.
    val counter = (1..10)
        .asFlow()
        .count {
            (it % 2) == 0
        }
    println(counter)
}