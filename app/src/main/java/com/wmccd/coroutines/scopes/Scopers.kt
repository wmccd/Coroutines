package com.wmccd.coroutines.scopes

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.wmccd.coroutines.Divider
import kotlinx.coroutines.*

class Scopers {

    fun scope(lifecycleScope: LifecycleCoroutineScope) {
        coroutineScopes(lifecycleScope)
    }

    private fun coroutineScopes(lifecycleScope: LifecycleCoroutineScope) {
        usingLifeCycleScope(lifecycleScope)
        usingViewModelScope()
        runBlocking {
            createCoroutineScopeInSuspendFunctions()
        }
        usingGlobalScope()
    }

    class VMS: ViewModel(){}
    private fun allScopes(lifecycleScope: LifecycleCoroutineScope) {
        GlobalScope.launch {
            println("I last as long as the app")
        }

        val vms = VMS()
        vms.viewModelScope.launch {
            println("I last as long as the viewModel")
        }

        lifecycleScope.launch{
            println("I last as long as the activity or fragment")
        }
    }

    suspend fun coroutineScopeInSuspendFunctions(){
        coroutineScope {
            launch {
                println("coroutineScope (with a small 'c' is available in a suspend function")
            }
        }
    }


    private fun usingGlobalScope() {
        Divider().divide()
        println("usingGlobalScope")

        GlobalScope.launch{
            println("I last as long as the app")
        }
    }

    private fun usingLifeCycleScope(lifecycleScope: LifecycleCoroutineScope) {
        Divider().divide()
        println("usingLifeCycleScope")
        lifecycleScope.launch{
            println("I last as long as the activity or fragment")
        }
    }

    class VM: ViewModel(){}
    private fun usingViewModelScope() {
        Divider().divide()
        println("usingViewModelScope")
        val viewModel = VM()
        viewModel.viewModelScope.launch {
            println("I last as long as the viewModel")
        }
    }

    suspend fun createCoroutineScopeInSuspendFunctions(){
        Divider().divide()
        println("createCoroutineScopeInSuspendFunctions")
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
}