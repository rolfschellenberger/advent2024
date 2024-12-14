package com.rolf.day14

import com.rolf.util.Matrix
import com.rolf.util.Point

data class Robot(
    val start: Point,
    val velocity: Point,
    var position: Point = start,
) {
    fun <T> simulate(map: Matrix<T>, seconds: Int) {
        position = Point(
            ((position.x + velocity.x * seconds) % map.width() + map.width()) % map.width(),
            ((position.y + velocity.y * seconds) % map.height() + map.height()) % map.height(),
        )
    }

    companion object {
        fun parse(line: String): Robot {
            // Parse p=0,4 v=3,-3
            val (p, v) = line.split(" ")
            val (px, py) = p.split("=")[1].split(",").map { it.toInt() }
            val (vx, vy) = v.split("=")[1].split(",").map { it.toInt() }
            return Robot(Point(px, py), Point(vx, vy))
        }
    }
}
