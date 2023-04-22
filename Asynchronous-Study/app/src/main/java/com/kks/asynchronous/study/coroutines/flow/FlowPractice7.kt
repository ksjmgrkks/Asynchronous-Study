package com.kks.asynchronous.study.coroutines.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    // zip은 양쪽의 데이터를 한꺼번에 묶어 새로운 데이터를 만들어 냅니다.
    val nums = (1..3).asFlow()
    val strs = flowOf("일", "이", "삼")
    nums.zip(strs) { a, b -> "${a}은(는) $b" }
        .collect { println(it) }

    // combine은 양쪽의 데이터를 같은 시점에 묶지 않고 한 쪽이 갱신되면 새로 묶어 데이터를 만듭니다.
    val nums2 = (1..3).asFlow().onEach { delay(100L) }
    val strs2 = flowOf("일", "이", "삼").onEach { delay(200L) }
    nums2.combine(strs2) { a, b -> "${a}은(는) $b" }
        .collect { println(it) }
    /* 예제에서는 적합한 코드는 아닙니다. 하지만 데이터가 짝을 이룰 필요없이 최신의 데이터를 이용해 가공해야 하는 경우에 사용할 수 있습니다.
       안드로이드에서 예시를 생각해보면, 2개의 api 호출 묶어 데이터를 생성할 때 좋은 경우는 zip을 이용하고,
       최신 UIState를 업데이트 해줘야 하는 상황에서는 짝을 이룰 필요는 없고, 하나라도 emit이 되면 업데이트를 하기 위해 combine이 적절해 보입니다.
       둘 중에 어떤 것이 더 낫다라는 개념이 아닌 상황에 맞게 사용하면 됩니다. */
}