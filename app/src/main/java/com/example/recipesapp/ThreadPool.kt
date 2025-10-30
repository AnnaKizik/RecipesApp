package com.example.recipesapp

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ThreadPool {
    val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
}