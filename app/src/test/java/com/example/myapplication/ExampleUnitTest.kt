package com.example.myapplication

import org.junit.Test

import org.junit.Assert.*

class ExampleUnitTest {
    // TODO unit test simple
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    //
    @Test
    fun multiply_isSquare() {
        val number = 5
        val result = number * number
        assertEquals(25, result)
    }
}