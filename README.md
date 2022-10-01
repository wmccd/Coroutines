# Coroutines

## Key Behaviour
Kotlin coroutines introduce the ability to suspend a coroutine at some point and then resume it in the future

We can run the code on the Main thread and suspend it while we request details from an API. When the coroutine is suspended the thread is not blocked, so processing can continue on the thread to change view, execute logic and run other coroutines. Once the data is received from the API the coroutine can continue from the point where it stopped.

## Coroutine Builders

Every suspending function needs to be called by a suspending function, which in turn is called by another function, and so on. All this has to start somewhere. It starts with a coroutine builder. There are three types:

* launch
* async
* runBlocking

*launch*() and *async* are **suspending** functions, *runBlocking* is a **blocking** function.

Generally speaking a coroutine should never block threads, only suspend them. However, there are times when it is required such as involving the main() function, other wise the program will end to quickly.



    //Blocking the main thread with Thread.sleep()
    //Without it main would have finished without printing Hello
    
    fun main(){    	
    	GlobalScope.launch{       //launch starts the coroutine
    		delay(100)            //coroutine is suspended       
    		println("Hello")      //coroutine resumes after suspenion
    	}
    	
    	Thread.sleep(200)
    	println("World")
    } 
    
    //Hello
    //World
    

Instead of using a Thread sleep statement to delay the early completion we can use *runBlocking*


    //Blocking the main thread with Thread.sleep()
    //Without it main would have finished without printing Hello
    
    fun main(){    	
    	runBlocking{              //runBlocking starts the coroutine
    		delay(100)            //coroutine and **Thread** is suspended       
    		println("Hello")      //coroutine and **Thread **resumes after suspenion
    	}
    	
    	println("World")
    } 
    
    //Hello
    //World
    
Aside from delaying the overall program, avoid runBlocking    
    
*aync*() is similar to *launch*() but is designed to produce a value. This value is returned by the lambda

    fun main(){
    	runBlocking{
    		val deferredResult: Deferred<Int> = GlobalScope.async{
    		    println("Hello")
    		    42
    		}
    	
    		//do other stuff...
    		val result: Int = deferredResult.await()
    		println(result)
    	}
    }    
    
    //Hello
    //42
    

We can wait for multiple asyncs to complete

    fun main(){
    	runBlocking{
    		val deferredResult1: Deferred<Int> = GlobalScope.async{
    		    println("Hello")
    		    42
    		}
 
    		val deferredResult2: Deferred<Int> = GlobalScope.async{
    		    println("Hello")
    		    43
    		}   	
    		//do other stuff...
    		println("results ${deferredResult1.await()} ${deferredResultw.await()}"  )
    	}
    }    
    
    //Hello
    //Hello
    //results 42 43
    

lauch() returns a Job object, and async() returns a Deferred object that implements the Job interface. The Job object can be used to control flow. However given that async has the await() option, it is of less use.

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
            job1.cancelAndJoin() //using cancelAndJoin helps free up resources
            job2.cancelAndJoin()
            println("World4")
        }

        //Hello3
        //Hello1
        //Hello2
        //Hello4
    }



## Coroutine Scope

When you run launch(), aync() or runBlocking, you are generate a CoroutineScope that exists withing the curly braces. This where the coroutine runs, suspends and resumes.

    //Note launch and async need a Scope object to help generate CorourtineScope 
    
    runBlocking{ 
    	//this: CoroutineScope
    }
    
    GloablScope.launch{
    	//this: CoroutineScope
    }
   
    GlobalScope.async{
    	//this: CoroutineScope
    }
    

<img width="716" alt="Screenshot 2022-10-01 at 10 35 31" src="https://user-images.githubusercontent.com/13928099/193403219-f72de686-8307-4259-960d-fc3290836d3f.png">

These are scopes you can use to start *launch* and *async*:    

* **GlobalScope**: Lasts the lifetime of the app. Can result in memory leaks. Avoid.
* **LifeCycleScope**: Last as long as the LifeCycle aware object used to create it. e.g. An activity.
* **ViewModelScope**: Lasts as long as the view model object used to create it.
* **CoroutineScope**: Use if you require launch() or async() in a suspend function

If you have a repository function that requires two pieces of data to be fetch asynchronously it is *much* more preferable to use coroutineScope (which only exists in a suspend function), rather than passing a scope into the suspend function as a parameter.

    suspend fun createCoroutineScopeInSuspendFunctions(){

        println("coroutineScope 1")
        coroutineScope {
            launch {
                println("coroutineScope 2")
            }
            val deferred = async{
                println("coroutineScope 3")
                delay(100)
                42
            }
            println("deferredResult ${deferred.await() }")
        }
        println("coroutineScope 4")
    }
    
    //coroutineScope 1
    //coroutineScope 2
    //coroutineScope 3
    //coroutineScope 42
    //coroutineScope 4
    
    
## Structured Concurrency    

This is all about parents and children, and the inheritence of CoroutineScope.


    fun main(){
    	runBlocking{ //Parent providing CoroutineScope
    		launch{   //Child inheriting CoroutineScope
    			delay(100)
    			println("Hello")
    		}
    		launch{ //Also child. Same as this.launch
    			delay(200)
    			println("World")
    		}
    		println("Ahoy")
    	}
    }
    //Ahoy
    //100ms delay
    //Hello
    //100ms delay
	//World    

A parent provides scope for its children and they are called in this scope. This leads to a relationship called *Structured Concurrency*. The rules that appy:

* children inherit context from their parent
* a parent suspends until all the children are finished
* when the parent is cancelled, the child coroutines are cancelled too
* when a child raises an error it destroys the parent as well




## Dispatchers

Kotlin coroutines provide the option to determine which Thread (or pool of threads) a coroutine runs on. This is done using Dispatchers.

#### Dispatchers.Default
If you do not set any dispatcher, the one chosen by default is the Dispatcher.Default which is designed to run CPU intensive operations. It has a pool of threads equal to the number of cores on your machine (or phone).

#### Main Dispatcher
Android has a concept of a Main thread which is the only one that can access the UI. Therefore it needs to be used very often but with great care. When the Main thread is blocked, the whole application is frozen.

In Android you can often only use the Dispatchers.Main. If there are CPU intensive operations use Dispatchers.Default.

#### Dispatcher.IO
Use when there are big file read/write operations.
