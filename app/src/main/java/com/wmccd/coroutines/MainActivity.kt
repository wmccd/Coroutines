package com.wmccd.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start()

    }

    fun start(){
        //asyncAndAwait()
        //thread100000()
        //coroutine100000()
        //coroutineBuilders()
        //coroutineScopes()
        //coroutineJobs()
        dispatchers()
    }

    private fun dispatchers() {
        defaultDispatcher()
    }

    private fun defaultDispatcher() {
        runBlocking {
            coroutineScope {
                repeat(1000) {
                    launch {
                        List(1000){
                            Random.nextLong()
                        }.maxOrNull()
                        val threadName = Thread.currentThread().name
                        println("Running on thread: $threadName")
                    }
                }
            }
        }
    }

    private fun coroutineJobs() {
        //launchJobAndCancel()
        //asyncJobAndCancel()
        //launchJobAndJoin()
        launchJobsAndJoins()
    }

    private fun launchJobAndJoin() {
        GlobalScope.launch {
            println("Hello1")
            val job:Job = launch {
                println("Hello2")
                delay(100)
                println("Hello3")
            }
            println("Hello4")
            job.join()
            println("World5")

        }

        //Hello1
        //Hello4
        //Hello2
        //Hello3
        //Hello5
    }

    private fun launchJobsAndJoins() {
        GlobalScope.launch {
            val job1 = launch {
                delay(100)
                println("Hello1")
            }
            val job2 = launch {
                delay(100)
                println("Hello2")
            }
            println("Hello3")
            job1.join()
            job2.join()
            println("World4")

        }

        //Hello3
        //Hello1
        //Hello2
        //Hello4
    }

    private fun launchJobAndCancel() {
        GlobalScope.launch {
            val job:Job = launch {
                println("Hello")
                delay(200)
                println("World")
            }
            delay(100)
            job.cancel("cancelled launch job")
            job.join()
            delay(300)
            println("World 1")

        }

        //Hello
        //World1
    }

    private fun asyncJobAndCancel() {
        GlobalScope.launch {
            val job:Job = async {
                println("Bello")
                delay(200)
                println("Borld")
            }
            delay(100)
            job.cancel ("cancelled launch job")
            job.join()
            delay(300)
            println("Borld 1")

        }

        //Bello
        //Borld1
    }


    private fun coroutineScopes() {
        usingGlobalScope()
        usingLifeCycleScope()
        usingViewModelScope()
        runBlocking {
            createCoroutineScopeInSuspendFunctions()
        }
    }

    private fun usingGlobalScope() {
        GlobalScope.launch{
            println("I last as long as the app")
        }
    }

    private fun usingLifeCycleScope() {
        lifecycleScope.launch{
            println("I last as long as the activity or fragment")
        }
    }

    class VM: ViewModel(){}
    private fun usingViewModelScope() {
        val viewModel = VM()
        viewModel.viewModelScope.launch {
            println("I last as long as the viewModel")
        }
    }

    suspend fun createCoroutineScopeInSuspendFunctions(){
        println("coroutineScope 1")
        coroutineScope {

            println("coroutineScope 2")
            println("coroutineScope 3")
        }
        println("coroutineScope 4")
    }

    private fun coroutineBuilders(){
        /** There are three types of coroutine builders provided in the kotlinx coroutine library **/
        //usingLaunchBuilder1()
        //usingLaunchBuilder2()
        //usingAsyncBuilder1()
        //usingAsyncBuilder2()
        //usingRunBlocking1()
        //usingRunBlocking2()
        usingRunBlocking3()
    }

    private fun usingAsyncBuilder1(){

        runBlocking {
            val deferredMighty = GlobalScope.async {
                delay(1000)
                "Mighty"
            }

            val deferredBobbins = GlobalScope.async {
                delay(1000)
                "Bobbins"
            }

            println("The ${deferredMighty.await()} ${deferredBobbins.await()}")
        }
        println("Outside 1")

        //The Mighty Bobbins
        //Outside1
    }

    private fun usingAsyncBuilder2(){

        GlobalScope.launch {
            val deferredMighty = GlobalScope.async {
                delay(1000)
                "Mighty"
            }

            val deferredBobbins = GlobalScope.async {
                delay(1000)
                "Bobbins"
            }

            println("The ${deferredMighty.await()} ${deferredBobbins.await()}")
        }
        println("Outside 2")

        //Outside2
        //The Mighty Bobbins
    }

    private fun usingLaunchBuilder1(){

        //note the Thread.sleep

        GlobalScope.launch {
            delay(1000)
            println("the")
        }
        GlobalScope.launch {
            delay(1000)
            println("mighty")
        }
        Thread.sleep(1500)
        println("bobbins")

        //the
        //mighty
        //bobbins
    }

    private fun usingLaunchBuilder2(){

        //note the Thread.sleep is missing

        GlobalScope.launch {
            delay(1000)
            println("the")
        }
        GlobalScope.launch {
            delay(1000)
            println("mighty")
        }
        println("bobbins")

        //bobbins
        //the
        //mighty
    }

    private fun usingRunBlocking1(){
        /** This builder is completely different to the others.
         * Coroutines should not block the thread the coroutine is running on but there are times that can be helpful.
         * ...but they are rare **/
        runBlocking {
            println("the")
            delay(1000)
            println("mighty")
        }
        println("bobbins")

        //the
        //mighty
        //bobbins
    }

    private fun usingRunBlocking2(){

        runBlocking {
            delay(1000)
            println("hello")
        }
        println("world")

        //hello
        //world
    }

    private fun usingRunBlocking3(){
    /** the parent runBlocking waits for all its children to complete **/

        runBlocking {
            this.launch {
                delay(1000)
                println("hello")
            }
            launch {
                delay(1000)
                println("to the")
            }
            println("say")
        }
        println("world")

        //say
        //hello
        //to the
        //world
    }

    private fun thread100000(){
        //generate out of memory exception
        repeat(100000){
            thread {
                Thread.sleep(1000L)
                print(".")
            }
        }
    }

    private fun coroutine100000(){
        val myContext = Dispatchers.IO
        val myScope: CoroutineScope = CoroutineScope(myContext)
        var s: String = ""
        println("coroutine100000")
        val job = myScope.launch {
            repeat(100000){
                myScope.launch {
                    delay(1000L)
                    s += "|"
                }
            }
        }
        println(s)

    }

    private fun asyncAndAwait(){
        val myContext = Dispatchers.IO
        val myScope: CoroutineScope = CoroutineScope(myContext)
        val job = myScope.launch {

            val config = async{
                delay(50)
                getConfig()
            }
            val new = async { getNews( config.await() ) }
            val user = async { getUser() }
            showNews(new.await() && user.await())
            bobbins()
        }
    }

    private fun getConfig(): Boolean{
        println("config")
        return true
    }

    private fun getNews(b : Boolean): Boolean{
        println("news")
        return true
    }

    private fun getUser(): Boolean{
        println("user")
        return true
    }

    private fun showNews(b: Boolean){
        println("show")
    }

    suspend fun bobbins(){
        delay(50)
        println("bobbins")
    }
}