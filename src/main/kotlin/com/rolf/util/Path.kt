package com.rolf.util

data class Path(val locations: List<Point>, val score: Int = 0) : Comparable<Path> {

    val size: Int
        get() {
            return locations.size
        }

    fun isEmpty(): Boolean {
        return locations.isEmpty()
    }

    fun isNotEmpty(): Boolean {
        return locations.isNotEmpty()
    }

    override fun compareTo(other: Path): Int {
        return score.compareTo(other.score)
    }
}
