package com.wmccd.coroutines.builders

import com.wmccd.coroutines.Divider
import kotlinx.coroutines.*

class Builders {

    fun build(){
        launch()
        runBlocking { delay(500)  }
        async()
        runBlocking { delay(500)  }
        blocking()
    }

    private fun launch(){
        launchWithThreadSleep()
        launchWithoutThreadSleep()
    }

    private fun async(){
        asyncRunningInRunBlockingThread()
        asyncRunningInRegularScope()
    }

    private fun blocking(){
        blockingTheMainThread()
        blockingUntilAllChildrenComplete()
    }



    private fun launchWithThreadSleep(){
        Divider().divide()
        println("launchWithThreadSleep")

        GlobalScope.launch {
            delay(100)
            println("the")
        }
        GlobalScope.launch {
            delay(100)
            println("mighty")
        }
        Thread.sleep(150)
        println("bobbins")

        //the
        //mighty
        //bobbins
    }

    private fun launchWithoutThreadSleep(){
        Divider().divide()
        println("launchWithoutThreadSleep")


        GlobalScope.launch {
            delay(100)
            println("the")
        }
        GlobalScope.launch {
            delay(100)
            println("mighty")
        }
        println("bobbins")

        //bobbins
        //the
        //mighty
    }


    private fun asyncRunningInRunBlockingThread(){
        Divider().divide()
        println("asyncRunningInRunBlockingThread")

        runBlocking {
            val deferredMighty = GlobalScope.async {
                delay(100)
                "Mighty"
            }

            val deferredBobbins = GlobalScope.async {
                delay(100)
                "Bobbins"
            }

            println("The ${deferredMighty.await()} ${deferredBobbins.await()}")
        }
        println("Outside")

        //The Mighty Bobbins
        //Outside
    }

    private fun asyncRunningInRegularScope(){
        Divider().divide()
        println("asyncRunningInRegularScope")

        GlobalScope.launch {
            val deferredMighty = GlobalScope.async {
                delay(100)
                "Mighty"
            }

            val deferredBobbins = GlobalScope.async {
                delay(100)
                "Bobbins"
            }

            println("The ${deferredMighty.await()} ${deferredBobbins.await()}")
        }
        println("Outside")

        //Outside
        //The Mighty Bobbins
    }






    private fun blockingTheMainThread(){
        /** This builder is completely different to the others.
         * Coroutines should not block the thread the coroutine is running on but there are times that can be helpful.
         * ...but they are rare **/

        Divider().divide()
        println("blockingTheMainThread")

        runBlocking {
            println("the")
            delay(100)
            println("mighty")
        }
        println("bobbins")

        //the
        //mighty
        //bobbins
    }


    private fun blockingUntilAllChildrenComplete(){

        Divider().divide()
        println("blockingUntilAllChildrenComplete")

        /** the parent runBlocking waits for all its children to complete **/

        runBlocking {
            this.launch {
                delay(100)
                println("hello")
            }
            launch {
                delay(100)
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




}