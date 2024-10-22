package com.rolf.util

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

open class Location(val x: Int, val y: Int, val z: Int) : Comparable<Location> {

    fun rotateX(degrees: Double): Location {
        val radians = Math.toRadians(degrees)
        val y1 = round(y * cos(radians) - z * sin(radians)).toInt()
        val z1 = round(y * sin(radians) + z * cos(radians)).toInt()
        return Location(x, y1, z1)
    }

    fun rotateY(degrees: Double): Location {
        val radians = Math.toRadians(degrees)
        val z1 = round(z * cos(radians) - x * sin(radians)).toInt()
        val x1 = round(z * sin(radians) + x * cos(radians)).toInt()
        return Location(x1, y, z1)
    }

    fun rotateZ(degrees: Double): Location {
        val radians = Math.toRadians(degrees)
        val x1 = round(x * cos(radians) - y * sin(radians)).toInt()
        val y1 = round(x * sin(radians) + y * cos(radians)).toInt()
        return Location(x1, y1, z)
    }

    fun distance(other: Location): Long {
        return 0L + abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }

    fun getNeighbours(): List<Location> {
        return listOf(
            Location(x + 1, y, z),
            Location(x - 1, y, z),
            Location(x, y + 1, z),
            Location(x, y - 1, z),
            Location(x, y, z + 1),
            Location(x, y, z - 1)
        )
    }

    fun findPath(
        to: Location,
        notAllowedLocations: Set<Location> = emptySet()
    ): List<Location> {
        val paths: ArrayDeque<List<Location>> = ArrayDeque()
        val seen: MutableSet<Location> = mutableSetOf(this)
        seen.addAll(notAllowedLocations)

        // Function to filter allowed locations
        fun isAllowed(to: Location): Boolean {
            if (seen.contains(to)) return false
            if (notAllowedLocations.contains(to)) return false
            return true
        }

        fun getNeighbours(point: Location): List<Location> {
            return point.getNeighbours().filter { isAllowed(it) }.sorted()
        }

        // Start with the neighbours of the starting point that are allowed to visit.
        for (neighbour in getNeighbours(this)) {
            paths.add(listOf(neighbour))
        }

        while (paths.isNotEmpty()) {
            val path = paths.removeFirst()
            val pathEnd: Location = path.last()

            // Arrived at destination?
            if (to == pathEnd) {
                return path
            }

            // Continue only new locations
            if (pathEnd !in seen) {
                seen.add(pathEnd)

                for (neighbour in getNeighbours(pathEnd)) {
                    paths.add(path + neighbour)
                }
            }
        }
        return emptyList()
    }

    override fun compareTo(other: Location): Int {
        val xCompare = x.compareTo(other.x)
        if (xCompare != 0) return xCompare
        val yCompare = y.compareTo(other.y)
        if (yCompare != 0) return yCompare
        return z.compareTo(other.z)
    }

    override fun toString(): String {
        return "Location(x=$x, y=$y, z=$z)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Location

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}
