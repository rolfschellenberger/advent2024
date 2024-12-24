package com.rolf.day24

import com.rolf.util.Binary

data class Operation(
    val input1: String,
    val operation: String,
    val input2: String,
    var output: String,
) {
    fun calculate(wireValues: MutableMap<String, Binary>): Binary {
        val a = wireValues[input1]!!
        val b = wireValues[input2]!!
//        println("$a $operation $b -> $output")
        return when (operation) {
            "AND" -> a.copy().and(b.value)
            "OR" -> a.copy().or(b.value)
            "XOR" -> a.copy().xor(b.value)
            else -> throw IllegalArgumentException("Unknown operation: $operation")
        }
    }
}
