package com.wmccd.coroutines.waiters

import com.wmccd.coroutines.Divider
import kotlinx.coroutines.*

class Waiters {

    fun waiter(){
        asyncAndAwait()
        runBlocking { delay(500)  }
        launchJobAndJoin()
        runBlocking { delay(500)  }
        launchJobsAndJoins()
        runBlocking { delay(500)  }
        launchJobAndCancel()
        runBlocking { delay(500)  }
        asyncJobAndCancel()
    }

    private fun asyncAndAwait(){
        Divider().divide()
        println("asyncAndAwait")

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

    private fun launchJobAndJoin() {
        Divider().divide()
        println("launchJobAndJoin")

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
        Divider().divide()
        println("launchJobsAndJoins")

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
            job1.cancelAndJoin()
            job2.cancelAndJoin()
            println("World4")
        }

        //Hello3
        //Hello1
        //Hello2
        //Hello4
    }

    private fun launchJobAndCancel() {
        Divider().divide()
        println("launchJobAndCancel")

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
        //World 1
    }

    private fun asyncJobAndCancel() {
        Divider().divide()
        println("asyncJobAndCancel")

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


    /**-----------------**/

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