package com.kks.asynchronous.study.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis


/*
* 코루틴 스코프와 슈퍼바이저 잡을 합친듯 한 SupervisorScope가 있습니다.
* */
suspend fun supervisoredFunc() = supervisorScope {
    // 슈퍼바이저 스코프를 사용할 때 주의점은 무조건 자식 수준에서 예외를 핸들링 해야한다는 것입니다.
    // 자식의 실패가 부모에게 전달되지 않기 때문에 자식 수준에서 예외를 처리해야합니다.
    // supervisorScope 옆에 파라미터로 붙일 수 없다는 말입니다.
    launch { printRandom1() }
    launch(ceh) { printRandom2() }
}
/*
* SupervisorJob은 예외에 의한 취소를 아래쪽으로 내려가게 한다.
* 즉, 자신과 자신의 자식만 취소하고, 부모와 형제는 취소시키지 않는다.
* 역으로 말하면, SupervisorJob을 사용하지 않으면, 예외 발생시
* 자식 + 형제 + 부모 모두를 취소시킨다.
* */
fun main() = runBlocking<Unit> {
    // printRandom2가 실패했지만 printRandom1은 제대로 수행된다.
    //joinAll은 복수의 Job에 대해 join를 수행하여 완전히 종료될 때까지 기다린다.
    val scope1 = CoroutineScope(Dispatchers.IO + SupervisorJob() + ceh)
    val job1 = scope1.launch { printRandom1() }
    val job2 = scope1.launch { printRandom2() }
    joinAll(job1, job2)


    val scope2 = CoroutineScope(Dispatchers.IO)
    val job = scope2.launch {
        supervisoredFunc()
    }
    job.join()
}