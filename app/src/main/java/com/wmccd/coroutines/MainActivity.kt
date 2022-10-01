package com.wmccd.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wmccd.coroutines.builders.Builders
import com.wmccd.coroutines.scopes.Scopers
import com.wmccd.coroutines.waiters.Waiters
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
        Scopers().scope(this.lifecycleScope)
        runBlocking { delay(2000) }
        Builders().build()
        runBlocking { delay(2000) }
        Waiters().waiter()

//        asyncAndAwait()
//        thread100000()
//        coroutine100000()
//        coroutineScopes()
        //coroutineJobs()
        //dispatchers()
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






}