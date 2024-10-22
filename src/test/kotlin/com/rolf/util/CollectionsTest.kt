package com.rolf.util

import org.junit.Assert.*
import org.junit.Test

class CollectionsTest {

    @Test
    fun testPermutations() {
        val permutations = getPermutations(listOf("a", "b"))
        assertEquals(2, permutations.size)
        assertEquals(listOf(listOf("a", "b"), listOf("b", "a")), permutations)

        val permutations2 = getPermutations(listOf("a", "b", "c"))
        assertEquals(6, permutations2.size)
        assertEquals(
            listOf(
                listOf("a", "b", "c"),
                listOf("a", "c", "b"),
                listOf("b", "a", "c"),
                listOf("b", "c", "a"),
                listOf("c", "a", "b"),
                listOf("c", "b", "a")
            ), permutations2
        )

        val permutations3 = getPermutations(listOf("a", "a"))
        assertEquals(2, permutations3.size)
        assertEquals(1, permutations3.toSet().size)

        val permutations4 = getPermutations(listOf("a", "a", "b"))
        assertEquals(6, permutations4.size)
        assertEquals(3, permutations4.toSet().size)

        assertEquals(5040, getPermutations(listOf("a", "b", "c", "d", "e", "f", "g")).size)

        val permutations5 = getPermutations(listOf("a", "b", "c"), size = 2)
        assertEquals(6, permutations5.size)
        assertEquals(
            listOf(
                listOf("a", "b"),
                listOf("a", "c"),
                listOf("b", "a"),
                listOf("b", "c"),
                listOf("c", "a"),
                listOf("c", "b")
            ), permutations5
        )
    }

    @Test
    fun testPermutationsFunction() {
        val results = mutableListOf<List<String>>()
        fun doIt(permutation: List<String>) {
            results.add(permutation)
        }

        getPermutations(listOf("a", "b", "c"), ::doIt)
        assertEquals(6, results.size)
        assertEquals(
            listOf(
                listOf("a", "b", "c"),
                listOf("a", "c", "b"),
                listOf("b", "a", "c"),
                listOf("b", "c", "a"),
                listOf("c", "a", "b"),
                listOf("c", "b", "a")
            ), results
        )
    }

    @Test
    fun testCombinations() {
        val options = listOf("a", "b", "c", "d")
        val expected = listOf(
            listOf("a", "b", "c", "d"),
            listOf("a", "b", "c"),
            listOf("a", "b", "d"),
            listOf("a", "b"),
            listOf("a", "c", "d"),
            listOf("a", "c"),
            listOf("a", "d"),
            listOf("a"),
            listOf("b", "c", "d"),
            listOf("b", "c"),
            listOf("b", "d"),
            listOf("b"),
            listOf("c", "d"),
            listOf("c"),
            listOf("d")
        )
        val combinations = getCombinations(options)
        assertEquals(15, combinations.size)
        assertEquals(expected, getCombinations(options))

        assertEquals(0, getCombinations(listOf<String>()).size)
        assertEquals(1, getCombinations(listOf("a")).size)
    }

    @Test
    fun testCombinationsFunction() {
        val results = mutableListOf<List<String>>()
        fun doIt(combination: List<String>) {
            results.add(combination)
        }

        getCombinations(listOf("a", "b", "c", "d"), ::doIt)
        assertEquals(15, results.size)
        assertEquals(
            listOf(
                listOf("a", "b", "c", "d"),
                listOf("a", "b", "c"),
                listOf("a", "b", "d"),
                listOf("a", "b"),
                listOf("a", "c", "d"),
                listOf("a", "c"),
                listOf("a", "d"),
                listOf("a"),
                listOf("b", "c", "d"),
                listOf("b", "c"),
                listOf("b", "d"),
                listOf("b"),
                listOf("c", "d"),
                listOf("c"),
                listOf("d")
            ), results
        )

        fun earlyTerminate(options: List<String>): Boolean {
            return options.contains("b")
        }

        results.clear()
        getCombinations(listOf("a", "b", "c", "d"), ::doIt, ::earlyTerminate)
        assertEquals(7, results.size)
        assertEquals(
            listOf(
                listOf("a", "c", "d"),
                listOf("a", "c"),
                listOf("a", "d"),
                listOf("a"),
                listOf("c", "d"),
                listOf("c"),
                listOf("d")
            ), results
        )
    }

    @Test
    fun testIntArraySwap() {
        val a = IntArray(5) { i -> i }
        assertEquals("01234", a.joinToString(""))
        a.swap(1, 4)
        assertEquals("04231", a.joinToString(""))
        a.swap(2, 4)
        assertEquals("04132", a.joinToString(""))
    }

    @Test
    fun testCharArraySwap() {
        val a = ('a'..'e').toList().toCharArray()
        assertEquals("abcde", a.joinToString(""))
        a.swap(1, 4)
        assertEquals("aecdb", a.joinToString(""))
        a.swap(2, 4)
        assertEquals("aebdc", a.joinToString(""))
        a.swap('a', 'b')
        assertEquals("beadc", a.joinToString(""))
    }

    @Test
    fun testIntArrayPushLeftRight() {
        val a = IntArray(5) { i -> i }
        assertEquals("01234", a.joinToString(""))
        a.pushRight(3)
        assertEquals("23401", a.joinToString(""))
        a.pushRight(13)
        assertEquals("40123", a.joinToString(""))
        a.pushLeft(14)
        assertEquals("34012", a.joinToString(""))
    }

    @Test
    fun testCharArrayPushLeftRight() {
        val a = ('a'..'e').toList().toCharArray()
        assertEquals("abcde", a.joinToString(""))
        a.pushRight(3)
        assertEquals("cdeab", a.joinToString(""))
        a.pushRight(13)
        assertEquals("eabcd", a.joinToString(""))
        a.pushLeft(14)
        assertEquals("deabc", a.joinToString(""))
    }

    @Test
    fun testIntRangeSize() {
        assertEquals(3, (3..5).size())
        assertEquals(3, (5 until 8).size())
        assertEquals(1, (10..10).size())
    }

    @Test
    fun testIntRangeOverlap() {
        assertEquals(5..8, (1..8).overlap(5..10))
        assertEquals(5..8, (5..10).overlap(1..8))
        assertEquals(5..5, (1..5).overlap(5..10))
        assertEquals(5..8, (1..15).overlap(5..8))
        assertEquals(5..8, (5..8).overlap(1..15))
        assertNull((5..8).overlap(9..15))
    }

    @Test
    fun testIntRangeContains() {
        assertTrue((1..6).contains(1..3))
        assertTrue((1..6).contains(2..6))
        assertTrue((1..6).contains(3..3))
        assertFalse((3..6).contains(1..2))
        assertFalse((3..6).contains(1..4))
        assertFalse((3..6).contains(5..8))
        assertFalse((3..6).contains(7..8))
    }

    @Test
    fun testIntRangeToLongRange() {
        assertEquals(4L..9L, (4..9).toLongRange())
    }

    @Test
    fun testIntRangeRemove() {
        assertEquals(listOf(3..8), (3..8).remove(null))
        assertEquals(listOf(3..8), (3..8).remove(100..200))
        assertEquals(emptyList<IntRange>(), (3..8).remove((3..8)))
        assertEquals(emptyList<IntRange>(), (3..8).remove((1..8)))
        assertEquals(emptyList<IntRange>(), (3..8).remove((3..10)))
        assertEquals(emptyList<IntRange>(), (3..8).remove((1..10)))
        assertEquals(listOf(6..8), (3..8).remove((1..5)))
        assertEquals(listOf(3..4), (3..8).remove((5..10)))
        assertEquals(
            listOf(
                3..4,
                7..8
            ), (3..8).remove((5..6))
        )
    }

    @Test
    fun testIntRangeSplit() {
        assertEquals(listOf(3..8), (3..8).split((10..20)))
        assertEquals(listOf(3..8), (3..8).split((3..8)))
        assertEquals(listOf(3..8), (3..8).split((1..8)))
        assertEquals(listOf(3..8), (3..8).split((3..10)))
        assertEquals(listOf(3..8), (3..8).split((1..10)))
        assertEquals(listOf(3..5, 6..8), (3..8).split((1..5)))
        assertEquals(listOf(3..4, 5..8), (3..8).split((5..10)))
        assertEquals(
            listOf(
                3..4,
                5..6,
                7..8
            ), (3..8).split((5..6))
        )
    }

    @Test
    fun testIntRangeAdd() {
        assertEquals(15..18, (5..8).add(10))
        assertEquals(4..7, (5..8).add(-1))
    }

    @Test
    fun testLongRangeSize() {
        assertEquals(3, (3L..5).size())
        assertEquals(3, (5L until 8).size())
        assertEquals(1, (10L..10).size())
    }

    @Test
    fun testLongRangeOverlap() {
        assertEquals(5L..8, (1L..8).overlap(5L..10))
        assertEquals(5L..8, (5L..10).overlap(1L..8))
        assertEquals(5L..5, (1L..5).overlap(5L..10))
        assertEquals(5L..8, (1L..15).overlap(5L..8))
        assertEquals(5L..8, (5L..8).overlap(1L..15))
        assertNull((5L..8).overlap(9L..15))
    }

    @Test
    fun testLongRangeContains() {
        assertTrue((1L..6).contains(1L..3))
        assertTrue((1L..6).contains(2L..6))
        assertTrue((1L..6).contains(3L..3))
        assertFalse((3L..6).contains(1L..2))
        assertFalse((3L..6).contains(1L..4))
        assertFalse((3L..6).contains(5L..8))
        assertFalse((3L..6).contains(7L..8))
    }

    @Test
    fun testLongRangeToIntRange() {
        assertEquals(4L..9L, (4..9).toLongRange())
    }

    @Test
    fun testLongRangeRemove() {
        assertEquals(listOf(3L..8L), (3L..8L).remove(null))
        assertEquals(listOf(3L..8L), (3L..8L).remove(100L..200L))
        assertEquals(emptyList<LongRange>(), (3L..8L).remove((3L..8L)))
        assertEquals(emptyList<LongRange>(), (3L..8L).remove((1L..8L)))
        assertEquals(emptyList<LongRange>(), (3L..8L).remove((3L..10L)))
        assertEquals(emptyList<LongRange>(), (3L..8L).remove((1L..10L)))
        assertEquals(listOf(6L..8L), (3L..8L).remove((1L..5L)))
        assertEquals(listOf(3L..4L), (3L..8L).remove((5L..10L)))
        assertEquals(
            listOf(
                3L..4L,
                7L..8L
            ), (3L..8L).remove((5L..6L))
        )
    }

    @Test
    fun testLongRangeSplit() {
        assertEquals(listOf(3L..8L), (3L..8L).split((10L..20L)))
        assertEquals(listOf(3L..8L), (3L..8L).split((3L..8L)))
        assertEquals(listOf(3L..8L), (3L..8L).split((1L..8L)))
        assertEquals(listOf(3L..8L), (3L..8L).split((3L..10L)))
        assertEquals(listOf(3L..8L), (3L..8L).split((1L..10L)))
        assertEquals(listOf(3L..5L, 6L..8L), (3L..8L).split((1L..5L)))
        assertEquals(listOf(3L..4L, 5L..8L), (3L..8L).split((5L..10L)))
        assertEquals(
            listOf(
                3L..4L,
                5L..6L,
                7L..8L
            ), (3L..8L).split((5L..6L))
        )
    }

    @Test
    fun testLongRangeAdd() {
        assertEquals(15L..18L, (5L..8L).add(10L))
        assertEquals(4L..7L, (5L..8L).add(-1L))
    }

    @Test
    fun testDeQueueShift() {
        val dequeue = ArrayDeque(listOf(1, 2, 3, 4, 5))
        dequeue.shift(-1)
        assertEquals(listOf(2, 3, 4, 5, 1), dequeue.toList())
        dequeue.shift(13)
        assertEquals(listOf(4, 5, 1, 2, 3), dequeue.toList())
    }

    @Test
    fun testListEndsWith() {
        val list = listOf(1, 2, 3, 4, 5, 6)
        assertTrue(list.endsWith(listOf(4, 5, 6)))
        assertTrue(list.endsWith(listOf(6)))
        assertFalse(list.endsWith(listOf(4, 5, 6, 6)))
    }

    @Test
    fun testFindPairs() {
        val options = mapOf(
            0 to setOf("a", "b"),
            1 to setOf("b"),
            2 to setOf("b", "c"),
            3 to setOf("a", "c", "d")
        )
        val pairs = findPairs(options)
        val expected = mapOf(
            0 to "a",
            1 to "b",
            2 to "c",
            3 to "d",
        )
        assertEquals(expected, pairs)
    }
}
