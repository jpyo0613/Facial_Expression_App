package com.example.imagepro

import android.content.Context

class ResultCalculator(
    private val context: Context,
    private val listener: Listener
) {
    interface Listener {
        fun onResult(text: String)
    }

    fun setResult(resultType: ResultType) {

    }
}
