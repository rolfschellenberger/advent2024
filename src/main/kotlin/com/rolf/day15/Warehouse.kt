package com.rolf.day15

import com.rolf.util.Direction
import com.rolf.util.MatrixString
import com.rolf.util.Point

data class Warehouse(
    val walls: Set<Point>,
    val boxes: Set<Box>,
    var robot: Point,
) {
    override fun toString(): String {
        val width = walls.maxOf { it.x } + 1
        val height = walls.maxOf { it.y } + 1
        val newWarehouse = MatrixString.buildDefault(width, height, ".")
        for (wall in walls) {
            newWarehouse.set(wall, "#")
        }
        for (box in boxes) {
            newWarehouse.set(box.left, "[")
            newWarehouse.set(box.right, "]")
        }
        newWarehouse.set(robot, "@")
        return newWarehouse.toString()
    }

    fun move(direction: Direction) {
        // We move the robot into the direction.
        // Boxes can be 'stacked', so we need to keep track of the 'surface' we are pushing.
        val surface = mutableSetOf<Point>(robot)

        // Keep track of all the boxes that should be moved
        val movableBoxes = mutableSetOf<Box>()

        // Keep moving
        var movable = false
        while (true) {
            val nextSurface = surface.map {
                move(it, direction)
            }

            // Stop if we hit a wall
            val nextWalls = walls.filter { nextSurface.contains(it) }
            if (nextWalls.isNotEmpty()) {
                break
            }

            // Find the next boxes we are pushing
            val nextBoxes = boxes.filter { box ->
                box.positions.any { nextSurface.contains(it) }
            }.filterNot { box ->
                movableBoxes.contains(box)
            }
            if (nextBoxes.isEmpty()) {
                movable = true
                break
            }
            movableBoxes.addAll(nextBoxes)

            // Update the surface
            surface.clear()
            surface.addAll(
                nextBoxes.map {
                    it.positions
                }.flatten()
            )
        }

        // Move the robot and the boxes
        if (movable) {
            for (box in movableBoxes) {
                val newPositions = box.positions.map {
                    move(it, direction)
                }
                box.positions.clear()
                box.positions.addAll(newPositions)
            }
            robot = move(robot, direction)
        }
    }

    private fun move(point: Point, direction: Direction): Point {
        return when (direction) {
            Direction.NORTH -> Point(point.x, point.y - 1)
            Direction.SOUTH -> Point(point.x, point.y + 1)
            Direction.WEST -> Point(point.x - 1, point.y)
            Direction.EAST -> Point(point.x + 1, point.y)
        }
    }

    companion object {
        fun build(warehouse: MatrixString): Warehouse {
            val walls = mutableSetOf<Point>()
            val boxes = mutableSetOf<Box>()
            var robot = Point(0, 0)
            for (point in warehouse.allPoints()) {
                val value = warehouse.get(point)
                when (value) {
                    "#" -> {
                        walls.add(Point(point.x * 2, point.y))
                        walls.add(Point(point.x * 2 + 1, point.y))
                    }

                    "." -> {
                    }

                    "O" -> {
                        boxes.add(
                            Box(
                                mutableSetOf(
                                    Point(point.x * 2, point.y),
                                    Point(point.x * 2 + 1, point.y)
                                )
                            )
                        )
                    }

                    "@" -> {
                        robot = Point(point.x * 2, point.y)
                    }

                    else -> throw IllegalArgumentException("Invalid value: $value")
                }
            }

            return Warehouse(
                walls,
                boxes,
                robot
            )
        }
    }
}
