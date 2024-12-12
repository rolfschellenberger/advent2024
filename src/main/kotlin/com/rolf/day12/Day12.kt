package com.rolf.day12

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        // Find all unique regions
        val map = MatrixString.build(splitLines(lines))
        var result = 0

        val allValues = map.allPoints().map { map.get(it) }.toSet()
        val seen = mutableSetOf<Point>()
        for (point in map.allPoints()) {
            if (seen.contains(point)) {
                continue
            }

            val value = map.get(point)
            var area = map.waterFill(point, allValues - value, diagonal = false)
            if (area.isEmpty()) {
                area = setOf(point)
            }
            seen.addAll(area)

            // So for every point in the area, see how many values they touch that does not equal their value
            var perimeter = 0
            for (point in area) {
                val neighbours = map.getNeighbours(point, diagonal = false, wrap = true) - area
                perimeter += neighbours.size
            }
            result += area.size * perimeter
        }
        println(result)
    }

    override fun solve2(lines: List<String>) {
        // Find all unique regions
        val map = MatrixString.build(splitLines(lines))
        var result = 0

        val allValues = map.allPoints().map { map.get(it) }.toSet()
        val seen = mutableSetOf<Point>()
        for (point in map.allPoints()) {
            if (seen.contains(point)) {
                continue
            }

            val value = map.get(point)
            var area = map.waterFill(point, allValues - value, diagonal = false)
            if (area.isEmpty()) {
                area = setOf(point)
            }
            seen.addAll(area)

            // For every point in the area, find its neighbours with direction, so we can find all neighbours on the
            // same line and direction as an edge
            var edgePoints = mutableSetOf<Pair<Point, Direction>>()
            for (point in area) {
                for (direction in Direction.entries) {
                    val next = map.getForward(point, direction, wrap = true)!!
                    if (!area.contains(next)) {
                        edgePoints.add(next to direction)
                    }
                }
            }

            // Now reduce the edgePoints collection to one edge point per edge
            val edgesSeen = mutableSetOf<Pair<Point, Direction>>()
            var areaResult = 0
            for ((edge, direction) in edgePoints) {
                // Skip if already seen
                if (!edgesSeen.add(edge to direction)) continue

                // Now make sure these edges are connected to the edge
                val connected = findConnectedEdges(map, edge, direction, edgePoints)
                connected.forEach {
                    edgesSeen.add(it to direction)
                }
                areaResult++
            }
            result += area.size * areaResult
        }
        println(result)
    }

    private fun findConnectedEdges(
        map: MatrixString,
        point: Point,
        direction: Direction,
        edgePoints: MutableSet<Pair<Point, Direction>>,
    ): Set<Point> {
        // Get all other edges with same direction and axis
        val edgesInLine = when (direction) {
            Direction.NORTH, Direction.SOUTH -> {
                edgePoints.filter { (e, d) ->
                    d == direction && e.y == point.y
                }.sortedBy { it.first.x }
            }

            Direction.EAST, Direction.WEST -> {
                edgePoints.filter { (e, d) ->
                    d == direction && e.x == point.x
                }.sortedBy { it.first.y }
            }
        }.map {
            it.first
        }

        // Now make sure these edges are all connected
        val connectedEdges = mutableSetOf<Point>(point)
        val neighbours = map.getNeighbours(point, diagonal = false, wrap = true).toMutableList()
        while (neighbours.isNotEmpty()) {
            val neighbour = neighbours.removeFirst()
            if (edgesInLine.contains(neighbour)) {
                connectedEdges.add(neighbour)
                neighbours.addAll(
                    map.getNeighbours(neighbour, diagonal = false, wrap = true) - connectedEdges
                )
            }
        }

        return connectedEdges
    }
}
