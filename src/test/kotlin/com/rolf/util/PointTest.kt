package com.rolf.util

import org.junit.Assert.assertEquals
import org.junit.Test

class PointTest {

    @Test
    fun testPointRotate() {
        assertEquals(Point(0, 2), Point(2, 3).rotateRight(4, 4, 90.0))
        assertEquals(Point(0, 1), Point(1, 2).rotateRight(3, 3, 90.0))
        assertEquals(Point(1, 1), Point(1, 1).rotateRight(3, 3, 90.0))
        assertEquals(Point(4, 7), Point(7, 5).rotateRight(10, 10, 90.0))
        assertEquals(Point(2, 2), Point(7, 2).rotateRight(10, 10, -90.0))
    }

    @Test
    fun testPointDistance() {
        assertEquals(0, Point(3, 6).distance(Point(3, 6)))
        assertEquals(5, Point(3, 6).distance(Point(4, 2)))
    }

    @Test
    fun testOrdering() {
        val list = listOf(Point(3, 4), Point(1, 4), Point(1, 3))
        val sorted = listOf(Point(1, 3), Point(1, 4), Point(3, 4))
        assertEquals(sorted, list.sorted())
    }

    @Test
    fun testAngleBetween() {
        assertEquals(45.0, Point(3, 3).angleBetween(Point(4, 4)), 0.0)
        assertEquals(270.0, Point(3, 3).angleBetween(Point(3, 1)), 0.0)
        assertEquals(180.0, Point(3, 3).angleBetween(Point(1, 3)), 0.0)
        assertEquals(0.0, Point(3, 3).angleBetween(Point(5, 3)), 0.0)
        assertEquals(90.0, Point(3, 3).angleBetween(Point(3, 9)), 0.0)
        assertEquals(135.0, Point(3, 3).angleBetween(Point(2, 4)), 0.0)
        assertEquals(225.0, Point(3, 3).angleBetween(Point(2, 2)), 0.0)
    }

    @Test
    fun testMove() {
        assertEquals(Point(3, 3), Point(2, 2).move(1, 1))
        assertEquals(Point(2, 2), Point(3, 3).move(-1, -1))
        assertEquals(Point(4, 2), Point(3, 3).move(1, -1))
    }
}
