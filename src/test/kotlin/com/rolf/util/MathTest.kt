package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class MathTest {

    @Test
    fun testFactorial() {
        assertEquals(1, factorial(0))
        assertEquals(1, factorial(1))
        assertEquals(2, factorial(2))
        assertEquals(6, factorial(3))
        assertEquals(720, factorial(6))
    }

    @Test
    fun testEven() {
        assertTrue(4.isEven())
        assertTrue(1232141456.isEven())
        assertFalse(3.isEven())
        assertFalse((-3).isEven())
        assertTrue((-4).isEven())
        assertTrue(2342352355520.isEven())
    }

    @Test
    fun testOdd() {
        assertTrue(3.isOdd())
        assertTrue(1232141457.isOdd())
        assertFalse(6.isOdd())
        assertFalse((-6).isOdd())
        assertTrue((-7).isOdd())
        assertTrue(2342352355529.isOdd())
    }

    @Test
    fun testIsPrime() {
        assertFalse(4.isPrime())
        assertTrue(7.isPrime())
        assertTrue(999999000001.isPrime())
    }

    @Test
    fun testLastDigit() {
        assertEquals(3, (-232213).lastDigit())
        assertEquals(3, (6663).lastDigit())
        assertEquals(5, (6665L).lastDigit())
        assertEquals(5, (-1235L).lastDigit())
    }

    @Test
    fun testSubIntLong() {
        assertEquals(2, (-232213).subInt(3, 4))
        assertEquals(63, (6663).subInt(2))
        assertEquals(2L, (-232213L).subLong(3, 4))
        assertEquals(63L, (6663L).subLong(2))
    }

    @Test
    fun testGreatestCommonDivisor() {
        assertEquals(3, greatestCommonDivisor(6, 9))
        assertEquals(6, greatestCommonDivisor(366, 60))
        assertEquals(60, greatestCommonDivisor(360, 60))
        assertEquals(5, greatestCommonDivisor(5, 5))
        assertEquals(-5, greatestCommonDivisor(-5, -5))
    }

    @Test
    fun testLeastCommonMultiple() {
        assertEquals(273, leastCommonMultiple(13, 21))
        assertEquals(360, leastCommonMultiple(120, 90))
    }

    @Test
    fun testLeastCommonMultipleList() {
        assertEquals(360, leastCommonMultiple(listOf(120, 90)))
        assertEquals(903000, leastCommonMultiple(listOf(12, 15, 75, 4, 5, 100, 200, 250, 301)))
        assertEquals(2520, leastCommonMultiple(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)))
    }
}
