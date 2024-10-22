package com.rolf.util

open class Area(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {

    private val voids = mutableListOf<Area>()

    fun remove(area: Area) {
        // Removing an area from an existing area, should result in creating a void area
        // Find the overlap area first
        val overlapArea = findOverlap(area) ?: return

        // In case this area already has some voids, also remove this area recursively
        for (void in voids) {
            void.remove(overlapArea)
        }

        voids.add(overlapArea)
    }

    private fun findOverlap(area: Area): Area? {
        val xOverlap = findOverlap(xRange, area.xRange) ?: return null
        val yOverlap = findOverlap(yRange, area.yRange) ?: return null
        val zOverlap = findOverlap(zRange, area.zRange) ?: return null
        return Area(xOverlap, yOverlap, zOverlap)
    }

    private fun findOverlap(a: IntRange, b: IntRange): IntRange? {
        // No overlap
        if (a.first > b.last || a.last < b.first) {
            return null
        }

        // Overlap is from 1 to 2 when sorting the values
        val list = listOf(a.first, a.last, b.first, b.last).sorted()
        return list[1]..list[2]
    }

    fun volume(): Long {
        val x = (xRange.last - xRange.first + 1).toLong()
        val y = (yRange.last - yRange.first + 1).toLong()
        val z = (zRange.last - zRange.first + 1).toLong()
        val volume = x * y * z
        val voidVolume = voids.map { it.volume() }.sum()
        return volume - voidVolume
    }

    override fun toString(): String {
        return "Area(xRange=$xRange, yRange=$yRange, zRange=$zRange)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Area

        if (xRange != other.xRange) return false
        if (yRange != other.yRange) return false
        if (zRange != other.zRange) return false

        return true
    }

    override fun hashCode(): Int {
        var result = xRange.hashCode()
        result = 31 * result + yRange.hashCode()
        result = 31 * result + zRange.hashCode()
        return result
    }
}
