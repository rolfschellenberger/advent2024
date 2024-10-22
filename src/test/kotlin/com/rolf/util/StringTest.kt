package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class StringTest {

    @Test
    fun getNumeric() {
        assertTrue("1234".isNumeric())
        assertTrue("-23".isNumeric())
        assertFalse("123.45".isNumeric())
        assertFalse("asdf234".isNumeric())
    }

    @Test
    fun getGetNumbers() {
        assertEquals("1234", "1234".getNumbers())
        assertEquals("012340", "012340".getNumbers())
        assertEquals("012340", "bla012die340bla".getNumbers())
        assertEquals("", "".getNumbers())
        assertEquals("", "             ".getNumbers())
    }

    @Test
    fun testJoinSideBySide() {
        val a = "123\n789"
        val b = "456\nABC\nDEF"
        val joined = joinSideBySide(listOf(a, b))
        assertEquals("123 456\n789 ABC\n    DEF", joined)
    }

    @Test
    fun testCharacterCounts() {
        val string = "abcefkfkfooooapppruiooolll"
        val map = getCharacterCounts(string)
        assertEquals(3, map.size)
        assertEquals(listOf('a', 'b', 'c', 'e', 'f', 'k', 'f', 'k', 'f', 'a', 'r', 'u', 'i'), map[1])
        assertEquals(listOf('p', 'o', 'l'), map[3])
        assertEquals(listOf('o'), map[4])
    }

    @Test
    fun testCharacterCountsNotInOrder() {
        val string = "abcefkfkfooooapppruiooolll"
        val map = getCharacterCounts(string, inOrder = false)
        assertEquals(4, map.size)
        assertEquals(listOf('b', 'c', 'e', 'i', 'r', 'u'), map[1])
        assertEquals(listOf('a', 'k'), map[2])
        assertEquals(listOf('f', 'l', 'p'), map[3])
        assertEquals(listOf('o'), map[7])
    }

    @Test
    fun testCharacterCountsUnique() {
        val string = "abcefkfkfooooapppruiooolll"
        val map = getCharacterCounts(string, uniqueChars = true)
        assertEquals(3, map.size)
        assertEquals(listOf('a', 'b', 'c', 'e', 'f', 'k', 'r', 'u', 'i'), map[1])
        assertEquals(listOf('p', 'o', 'l'), map[3])
        assertEquals(listOf('o'), map[4])
    }
}
