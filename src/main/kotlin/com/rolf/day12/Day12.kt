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
        val map = MatrixString.build(splitLines(lines))
        val areas = findAreas(map)

        var sum = areas.sumOf { area ->
            val sides = getSides(map, area)
            sides.sumOf {
                it.size * area.size
            }
        }
        println(sum)
    }

    private fun findAreas(map: MatrixString): List<Set<Point>> {
        val areas = mutableListOf<Set<Point>>()
        val seen = mutableSetOf<Point>()

        val allValues = map.allPoints().map { map.get(it) }.toSet()
        for (point in map.allPoints()) {
            if (seen.contains(point)) {
                continue
            }

            val value = map.get(point)
            var area = map.waterFill(point, allValues - value, diagonal = false)
            seen.addAll(area)
            areas.add(area)
        }
        return areas
    }

    private fun getSides(map: MatrixString, area: Set<Point>): List<Set<Point>> {
        val edgePoints = findEdgePoints(map, area)
        return convertToEdges(map, edgePoints)
    }

    private fun convertToEdges(map: MatrixString, edgePoints: Set<Pair<Point, Direction>>): List<Set<Point>> {
        // Now reduce the edgePoints collection to edges with all their points
        val edges = mutableListOf<Set<Point>>()

        val edgesSeen = mutableSetOf<Pair<Point, Direction>>()
        for ((edge, direction) in edgePoints) {
            // Skip if already seen
            if (!edgesSeen.add(edge to direction)) continue

            // Now make sure these edges are connected to the edge
            val connected = findConnectedEdges(map, edge, direction, edgePoints)
            connected.forEach {
                edgesSeen.add(it to direction)
            }
            edges.add(connected)
        }
        return edges
    }

    private fun findEdgePoints(
        map: MatrixString,
        area: Set<Point>,
    ): Set<Pair<Point, Direction>> {
        val edgePoints = mutableSetOf<Pair<Point, Direction>>()
        // Look in every direction to see if this is not a point part of the area
        for (point in area) {
            for (direction in Direction.entries) {
                val next = map.getForward(point, direction, wrap = true)!!
                if (!area.contains(next)) {
                    edgePoints.add(next to direction)
                }
            }
        }
        return edgePoints
    }

    private fun findConnectedEdges(
        map: MatrixString,
        point: Point,
        direction: Direction,
        edgePoints: Set<Pair<Point, Direction>>,
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

    override fun solve2(lines: List<String>) {
        val map = MatrixString.build(splitLines(lines))
        val areas = findAreas(map)

        var sum = areas.sumOf { area ->
            val sides = getSides(map, area)
            sides.size * area.size
        }
        println(sum)
    }
}
