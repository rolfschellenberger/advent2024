package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class BinaryTest {

    @Test
    fun testConstructor() {
        val a = Binary(4, 8)
        val b = Binary("00100", 9)
        assertEquals(4, a.value)
        assertEquals(8, a.bits)
        assertEquals("100", a.toString())
        assertEquals("00000100", a.toString(true))
        assertEquals(4, b.value)
        assertEquals(9, b.bits)
        assertEquals("100", b.toString())
        assertEquals("000000100", b.toString(true))
        assertEquals(16, Binary(4).bits)
        assertEquals(16, Binary("101").bits)
    }

    @Test
    fun testToString() {
        val four = Binary(4, 16)
        assertEquals(4, four.value)
        assertEquals("100", four.toString())
        assertEquals("0000000000000100", four.toString(true))

        val nine = Binary(9, 4)
        assertEquals(9, nine.value)
        assertEquals("1001", nine.toString())
        assertEquals("1001", nine.toString(true))

        val eleven = Binary(11, 3)
        // 11 = 1011 -> 3 bits -> 011 -> 3
        assertEquals(3, eleven.value)
        assertEquals("11", eleven.toString())
        assertEquals("011", eleven.toString(true))
    }

    @Test
    fun testAnd() {
        val a1 = Binary("11001101")
        assertEquals("1100", a1.and("00111100").toString())
        val a2 = Binary("11001101")
        assertEquals("1001", a2.and(9).toString())

        // Within the bits
        val b = Binary(11, 4)
        assertEquals(9, b.and(13).value)

        // Overflow input
        val c = Binary(19, 4)
        assertEquals(3, c.and(7).value)

        // Overflow add
        val d = Binary(11, 4)
        assertEquals(9, d.and(25).value)
    }

    @Test
    fun testOr() {
        val a = Binary("00101")
        assertEquals("1101", a.or("01001").toString())

        val b = Binary(41)
        assertEquals(59, b.or(51).value)
    }

    @Test
    fun testXor() {
        val a = Binary("00101")
        assertEquals("1100", a.xor("01001").toString())

        val b = Binary(53)
        assertEquals(31, b.xor(42).value)
    }

    @Test
    fun testNot() {
        val a = Binary("101100", 6)
        assertEquals("10011", a.not().toString())
    }

    @Test
    fun testShiftLeft() {
        val a = Binary("101111")
        assertEquals("101111000", a.shiftLeft(3).toString())

        val b = Binary("101111", 6)
        assertEquals("111000", b.shiftLeft(3).toString())
    }

    @Test
    fun testShiftRight() {
        val a = Binary("101111")
        assertEquals("101", a.shiftRight(3).toString())

        val b = Binary("101111", 6)
        assertEquals("101", b.shiftRight(3).toString())
    }

    @Test
    fun testCopy() {
        val a = Binary("101")
        val b = a.copy()
        assertEquals(a, b)

        a.and("110")
        val c = a.copy()
        assertEquals("100", a.toString())
        assertEquals("101", b.toString())
        assertEquals("100", c.toString())

        assertNotEquals(a, b)
        assertNotEquals(b, c)
        assertEquals(a, c)

        val d = Binary("10101100")
        val e = d.copy(4)
        assertEquals("1100", e.toString(true))
    }

    @Test
    fun testGet() {
        val a = Binary("1011001")
        assertEquals(16, a.length)
        assertTrue(a[0])
        assertFalse(a[1])
        assertFalse(a[2])
        assertTrue(a[3])
        assertTrue(a[4])
        assertFalse(a[5])
        assertTrue(a[6])
        assertFalse(a[7])
        assertFalse(a[8])
        assertFalse(a[9])
        assertFalse(a[10])
        assertFalse(a[11])
        assertFalse(a[12])
        assertFalse(a[13])
        assertFalse(a[14])
        assertFalse(a[15])
    }

    @Test
    fun testSet() {
        val a = Binary("11010001")
        a[0] = false
        assertEquals("11010000", a.toString())
        a[0] = false
        assertEquals("11010000", a.toString())
        a[5] = true
        assertEquals("11110000", a.toString())
        a[5] = true
        assertEquals("11110000", a.toString())
        a[15] = true
        assertEquals("1000000011110000", a.toString())
    }

    @Test
    fun testPlus() {
        assertEquals(Binary("1000"), Binary(5) + Binary(3))
        assertEquals(Binary("11"), Binary(5) + Binary(-2))
    }

    @Test
    fun testMinus() {
        assertEquals(Binary("10"), Binary(5) - Binary(3))
        assertEquals(Binary("111"), Binary(5) - Binary(-2))
    }

    @Test
    fun testTimes() {
        assertEquals(Binary("1111"), Binary(5) * Binary(3))
    }

    @Test
    fun testDiv() {
        assertEquals(Binary("1"), Binary(5) / Binary(3))
        assertEquals(Binary("101"), Binary(15) / Binary(3))
    }
}
