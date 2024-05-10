package com.example.imagepro

class ResultCalculator(private val listener: Listener) {
    interface Listener {
        fun onResult()
    }

    fun setResult(resultType: ResultType) {

    }
}