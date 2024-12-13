package com.rolf.day13

data class Game(
    val ax: Double,
    val ay: Double,
    val bx: Double,
    val by: Double,
    val px: Double,
    val py: Double,
) {
    fun score(): Long {
        // So we need to solve the following equation:
        // ax * s + bx * t = px
        // ay * s + by * t = py
        // We can solve this by multiplying the first equation by by and the second by bx:
        // ax * s * by + bx * t * by = px * by
        // ay * s * bx + by * t * bx = py * bx
        // Subtract the two equations:
        // s * (ax * by - ay * bx) = px * by - py * bx
        // Divide by the determinant:
        // s = (px * by - py * bx) / (ax * by - ay * bx)
        val s = (px * by - py * bx) / (ax * by - ay * bx)
        // Now we can solve for t:
        // ax * s + bx * t = px
        // bx * t = px - ax * s
        // t = (px - ax * s) / bx
        val t = (px - ax * s) / bx
        // Solvable, so return score
        if (s.rem(1) == 0.0 && t.rem(1) == 0.0) {
            return s.toLong() * 3 + t.toLong()
        }
        // Not solvable, no score
        return 0
    }
}
