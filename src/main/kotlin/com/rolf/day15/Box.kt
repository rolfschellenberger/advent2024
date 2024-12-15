package com.rolf.day15

import com.rolf.util.Point

data class Box(
    val positions: MutableSet<Point>,
) {
    val left get() = positions.minByOrNull { it.x }!!
    val right get() = positions.maxByOrNull { it.x }!!
}
