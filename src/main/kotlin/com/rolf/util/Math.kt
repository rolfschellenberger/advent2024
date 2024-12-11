package com.rolf.util

import kotlin.math.absoluteValue

fun Int.isEven(): Boolean = this % 2 == 0

fun Int.isOdd(): Boolean = this % 2 != 0

fun Int.isPrime(certainty: Int = 5): Boolean = this.toBigInteger().isProbablePrime(certainty)

fun Int.lastDigit(): Int = (this.absoluteValue % 10)

fun Int.subInt(startIndex: Int, endIndex: Int): Int {
    return "$this".substring(startIndex, endIndex).toInt()
}

fun Int.subInt(startIndex: Int): Int {
    return "$this".substring(startIndex).toInt()
}

fun Long.isEven(): Boolean = this % 2 == 0L

fun Long.isOdd(): Boolean = this % 2 != 0L

fun Long.isPrime(certainty: Int = 5): Boolean = this.toBigInteger().isProbablePrime(certainty)

fun Long.lastDigit(): Long = (this.absoluteValue % 10)

fun Long.subLong(startIndex: Int, endIndex: Int): Long {
    return "$this".substring(startIndex, endIndex).toLong()
}

fun Long.subLong(startIndex: Int): Long {
    return "$this".substring(startIndex).toLong()
}

fun factorial(num: Int): Long {
    var factorial: Long = 1
    for (i in num downTo 2) {
        factorial *= i
    }
    return factorial
}

fun greatestCommonDivisor(a: Int, b: Int): Int {
    return greatestCommonDivisor(a.toLong(), b.toLong()).toInt()
}

fun greatestCommonDivisor(a: Long, b: Long): Long {
    return if (b == 0L) {
        a
    } else {
        greatestCommonDivisor(b, a % b)
    }
}

fun leastCommonMultiple(a: Long, b: Long): Long {
    return (a * b) / greatestCommonDivisor(a, b)
}

fun leastCommonMultiple(numbers: List<Long>): Long {
    var result = numbers.first()
    for (i in 1 until numbers.size) {
        result = leastCommonMultiple(result, numbers[i])
    }
    return result
}

