package com.rolf.util

import kotlin.math.*

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    fun rotateRight(xLength: Int, yLength: Int, angleDegrees: Double): Point {
        val radians = Math.toRadians(angleDegrees)
        val centerX = (xLength - 1) / 2.0
        val centerY = (yLength - 1) / 2.0
        val newX = centerX + (x - centerX) * cos(radians) - (y - centerY) * sin(radians)
        val newY = centerY + (x - centerX) * sin(radians) + (y - centerY) * cos(radians)
        return Point(newX.roundToInt(), newY.roundToInt())
    }

    // This is the angle compared to the x-axis, ranging from 0 to 360
    fun angleBetween(other: Point): Double {
        return (Math.toDegrees(atan2(other.y - y.toDouble(), other.x - x.toDouble())) + 360) % 360
    }

    fun distance(other: Point): Int {
        return abs(x - other.x) + abs(y - other.y)
    }

    fun touching(other: Point): Boolean {
        return abs(x - other.x) <= 1 && abs(y - other.y) <= 1
    }

    fun move(deltaX: Int, deltaY: Int): Point {
        return Point(x + deltaX, y + deltaY)
    }

    override fun compareTo(other: Point): Int {
        val yCompare = y.compareTo(other.y)
        if (yCompare != 0) return yCompare
        return x.compareTo(other.x)
    }
}
