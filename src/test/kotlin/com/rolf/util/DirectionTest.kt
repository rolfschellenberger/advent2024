package com.rolf.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DirectionTest {

    @Test
    fun testLeft() {
        assertEquals(Direction.WEST, Direction.NORTH.left())
        assertEquals(Direction.NORTH, Direction.EAST.left())
        assertEquals(Direction.EAST, Direction.SOUTH.left())
        assertEquals(Direction.SOUTH, Direction.WEST.left())
    }

    @Test
    fun testRight() {
        assertEquals(Direction.EAST, Direction.NORTH.right())
        assertEquals(Direction.SOUTH, Direction.EAST.right())
        assertEquals(Direction.WEST, Direction.SOUTH.right())
        assertEquals(Direction.NORTH, Direction.WEST.right())
    }

    @Test
    fun testOpposite() {
        assertEquals(Direction.SOUTH, Direction.NORTH.opposite())
        assertEquals(Direction.WEST, Direction.EAST.opposite())
        assertEquals(Direction.NORTH, Direction.SOUTH.opposite())
        assertEquals(Direction.EAST, Direction.WEST.opposite())
    }
}
