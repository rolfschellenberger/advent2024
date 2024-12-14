package com.rolf.day14

import com.rolf.Day
import com.rolf.util.MatrixInt
import com.rolf.util.MatrixString
import com.rolf.util.Point

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val robots = lines.map { Robot.parse(it) }
        val map = when (robots.size) {
            12 -> MatrixInt.buildDefault(11, 7, 0)
            else -> MatrixInt.buildDefault(101, 103, 0)
        }

        // Now simulate the robots for 100 seconds
        robots.forEach { robot ->
            robot.simulate(map, 100)
        }

        // Visualize the robots
        robots.forEach { robot ->
            val value = map.get(robot.position)
            map.set(robot.position, value + 1)
        }

        // Get the quadrants of the map
        val xCenter = map.width() / 2
        val yCenter = map.height() / 2

        val q1 = map.copy()
        val q2 = map.copy()
        val q3 = map.copy()
        val q4 = map.copy()

        q1.cutOut(Point(0, 0), Point(xCenter - 1, yCenter - 1))
        q2.cutOut(Point(xCenter + 1, 0), Point(map.width() - 1, yCenter - 1))
        q3.cutOut(Point(0, yCenter + 1), Point(xCenter - 1, map.height() - 1))
        q4.cutOut(Point(xCenter + 1, yCenter + 1), Point(map.width() - 1, map.height() - 1))

        val q1Count = q1.allPoints().sumOf { q1.get(it) }
        val q2Count = q2.allPoints().sumOf { q2.get(it) }
        val q3Count = q3.allPoints().sumOf { q3.get(it) }
        val q4Count = q4.allPoints().sumOf { q4.get(it) }
        println(q1Count * q2Count * q3Count * q4Count)
    }

    override fun solve2(lines: List<String>) {
        val robots = lines.map { Robot.parse(it) }
        // Skip for the test input
        if (robots.size < 20) {
            return
        }

        for (i in 0 until 1000000) {
            val map = MatrixString.buildDefault(101, 103, " ")
            robots.forEach { robot ->
                robot.simulate(map, 1)
                map.set(robot.position, "#")
            }

            // Check if there is something visual in the center
            val water = map.waterFill(map.center(), setOf(" "))
            if (water.size > 100) {
                println(map)
                println("Steps: ${i + 1}")
                break
            }
        }
    }
}
