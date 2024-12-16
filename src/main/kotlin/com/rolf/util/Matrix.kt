package com.rolf.util

import java.util.*
import kotlin.collections.plus
import kotlin.math.abs

open class Matrix<T>(internal val input: MutableList<MutableList<T>>) {

    fun topLeft() = Point(0, 0)
    fun topRight() = Point(width() - 1, 0)
    fun bottomLeft() = Point(0, height() - 1)
    fun bottomRight() = Point(width() - 1, height() - 1)
    fun center() = Point(width() / 2, height() / 2)

    fun allElements(): List<T> {
        return input.flatten()
    }

    fun allPoints(): List<Point> {
        val points = mutableListOf<Point>()
        for (y in 0 until height()) {
            for (x in 0 until width()) {
                points.add(Point(x, y))
            }
        }
        return points
    }

    fun height(): Int {
        return input.size
    }

    fun width(): Int {
        if (height() == 0) return 0
        return input[0].size
    }

    fun isOutside(x: Int, y: Int): Boolean {
        return x < 0 || x >= width() || y < 0 || y >= height()
    }

    fun isOutside(point: Point): Boolean {
        return isOutside(point.x, point.y)
    }

    fun getColumns(): List<List<T>> {
        val result = mutableListOf<List<T>>()
        for (x in 0 until width()) {
            result.add(getColumn(x))
        }
        return result
    }

    fun getColumn(x: Int): List<T> {
        val result = mutableListOf<T>()
        for (row in input) {
            result.add(row[x])
        }
        return result
    }

    fun getRows(): List<List<T>> {
        val result = mutableListOf<List<T>>()
        for (y in 0 until height()) {
            result.add(getRow(y))
        }
        return result
    }

    fun getRow(y: Int): List<T> {
        return input[y].toList()
    }

    fun insertRow(index: Int, defaultValue: T) {
        val row = mutableListOf<T>()
        for (i in 0..width()) {
            row.add(defaultValue)
        }
        input.add(index, row)
    }

    fun insertColumn(index: Int, defaultValue: T) {
        for (y in 0 until height()) {
            input[y].add(index, defaultValue)
        }
    }

    fun getBorderPoints(): List<Point> {
        val points = mutableListOf<Point>()
        for (x in 0 until width()) {
            points.add(Point(x, 0))
            points.add(Point(x, height() - 1))
        }
        for (y in 0 until height()) {
            points.add(Point(0, y))
            points.add(Point(width() - 1, y))
        }
        return points
    }

    fun getTopEdgePoints(): List<Point> {
        return (0 until width()).map { Point(it, 0) }
    }

    fun getBottomEdgePoints(): List<Point> {
        return (0 until width()).map { Point(it, height() - 1) }
    }

    fun getLeftEdgePoints(): List<Point> {
        return (0 until height()).map { Point(0, it) }
    }

    fun getRightEdgePoints(): List<Point> {
        return (0 until height()).map { Point(width() - 1, it) }
    }

    fun getTopEdge(): List<T> {
        return getRow(0)
    }

    fun getBottomEdge(): List<T> {
        return getRow(height() - 1)
    }

    fun getLeftEdge(): List<T> {
        return getColumn(0)
    }

    fun getRightEdge(): List<T> {
        return getColumn(width() - 1)
    }

    fun get(x: Int, y: Int): T {
        return input[y][x]
    }

    fun get(point: Point): T {
        return get(point.x, point.y)
    }

    fun set(x: Int, y: Int, value: T) {
        input[y][x] = value
    }

    fun set(point: Point, value: T) {
        set(point.x, point.y, value)
    }

    fun count(value: T): Int {
        return count(setOf(value))
    }

    fun count(values: Set<T>): Int {
        return allElements().count { it in values }
    }

    fun find(value: T): List<Point> {
        return allPoints().filter { get(it) == value }
    }

    fun find(values: Set<T>): List<Point> {
        return allPoints().filter { values.contains(get(it)) }
    }

    fun wrap(point: Point): Point {
        val x = (point.x % width() + 2 * width()) % width()
        val y = (point.y % height() + 2 * height()) % height()
        return Point(x, y)
    }

    fun getLeft(point: Point, wrap: Boolean = false): Point? {
        return move(point, -1, 0, wrap)
    }

    fun getRight(point: Point, wrap: Boolean = false): Point? {
        return move(point, 1, 0, wrap)
    }

    fun getUp(point: Point, wrap: Boolean = false): Point? {
        return move(point, 0, -1, wrap)
    }

    fun getDown(point: Point, wrap: Boolean = false): Point? {
        return move(point, 0, 1, wrap)
    }

    fun getLeftUp(point: Point, wrap: Boolean = false): Point? {
        return move(point, -1, -1, wrap)
    }

    fun getLeftDown(point: Point, wrap: Boolean = false): Point? {
        return move(point, -1, 1, wrap)
    }

    fun getRightUp(point: Point, wrap: Boolean = false): Point? {
        return move(point, 1, -1, wrap)
    }

    fun getRightDown(point: Point, wrap: Boolean = false): Point? {
        return move(point, 1, 1, wrap)
    }

    fun getForward(point: Point, direction: Direction, wrap: Boolean = false): Point? {
        return when (direction) {
            Direction.NORTH -> getUp(point, wrap)
            Direction.EAST -> getRight(point, wrap)
            Direction.SOUTH -> getDown(point, wrap)
            Direction.WEST -> getLeft(point, wrap)
        }
    }

    fun getAllDirections(start: Point): List<Point> {
        return allPoints().filterNot { it == start }.map {
            val gcd = abs(greatestCommonDivisor(start.x - it.x, start.y - it.y))
            Point((it.x - start.x) / gcd, (it.y - start.y) / gcd)
        }.distinct()
    }

    fun getNextDirection(point: Point, direction: Point): Point {
        return Point(point.x + direction.x, point.y + direction.y)
    }

    private fun move(point: Point, xDelta: Int, yDelta: Int, wrap: Boolean = false): Point? {
        var x = point.x + xDelta
        var y = point.y + yDelta
        if (wrap) {
            val wrapped = wrap(Point(x, y))
            x = wrapped.x
            y = wrapped.y
        }
        if (x < 0 || x >= width()) return null
        if (y < 0 || y >= height()) return null
        return Point(x, y)
    }

    fun getNeighbours(
        point: Point,
        horizontal: Boolean = true,
        vertical: Boolean = true,
        diagonal: Boolean = true,
        includeOwn: Boolean = false,
        wrap: Boolean = false,
    ): Set<Point> {
        val result = mutableSetOf<Point>()
        if (includeOwn) result.add(point)
        if (horizontal) {
            getLeft(point, wrap)?.let { result.add(it) }
            getRight(point, wrap)?.let { result.add(it) }
        }
        if (vertical) {
            getUp(point, wrap)?.let { result.add(it) }
            getDown(point, wrap)?.let { result.add(it) }
        }
        if (diagonal) {
            getLeftUp(point, wrap)?.let { result.add(it) }
            getLeftDown(point, wrap)?.let { result.add(it) }
            getRightUp(point, wrap)?.let { result.add(it) }
            getRightDown(point, wrap)?.let { result.add(it) }
        }
        return result
    }

    fun getArea(topLeftInclusive: Point, bottomRightInclusive: Point): List<Point> {
        val points = mutableListOf<Point>()
        for (y in topLeftInclusive.y..bottomRightInclusive.y) {
            for (x in topLeftInclusive.x..bottomRightInclusive.x) {
                points.add(Point(x, y))
            }
        }
        return points
    }

    fun getOppositePointOverX(x: Int, y: Int, centerX: Int): Point {
        val diff = abs(centerX - x)
        return if (centerX > x) {
            Point(centerX + diff, y)
        } else {
            Point(centerX - diff, y)
        }
    }

    fun getOppositePointOverY(x: Int, y: Int, centerY: Int): Point {
        val diff = abs(centerY - y)
        return if (centerY > y) {
            Point(x, centerY + diff)
        } else {
            Point(x, centerY - diff)
        }
    }

    fun subMatrix(topLeftInclusive: Point, bottomRightInclusive: Point): Matrix<T> {
        val rows = mutableListOf<MutableList<T>>()
        for (y in topLeftInclusive.y..bottomRightInclusive.y) {
            val row = mutableListOf<T>()
            for (x in topLeftInclusive.x..bottomRightInclusive.x) {
                row.add(get(x, y))
            }
            rows.add(row)
        }
        return Matrix(rows)
    }

    fun cutOut(topLeftInclusive: Point, bottomRightInclusive: Point) {
        val sub = subMatrix(topLeftInclusive, bottomRightInclusive)
        input.clear()
        input.addAll(sub.input)
    }

    fun grow(left: Int, right: Int, top: Int, bottom: Int, defaultValue: T) {
        val newMatrix = buildDefault(width() + left + right, height() + top + bottom, defaultValue)

        for (point in allPoints()) {
            newMatrix.set(point.x + left, point.y + top, get(point))
        }

        input.clear()
        input.addAll(newMatrix.input)
    }

    fun replace(map: Map<T, T>) {
        for (point in allPoints()) {
            val value = get(point)
            if (map.containsKey(value)) {
                set(point, map[value]!!)
            }
        }
    }

    fun rotateRight() {
        if (width() != height()) throw Exception("Cannot rotate when dimensions are unequal!")

        val rows = mutableListOf<MutableList<T>>()
        for (x in 0 until width()) {
            rows.add(getColumn(x).reversed().toMutableList())
        }

        input.clear()
        input.addAll(rows)
    }

    fun flip(horizontal: Boolean = true) {
        val copy = copy()

        if (horizontal) {
            for (y in 0 until height()) {
                for (x in 0 until width()) {
                    set(x, y, copy.get(width() - x - 1, y))
                }
            }
        } else {
            for (y in 0 until height()) {
                for (x in 0 until width()) {
                    set(x, y, copy.get(x, height() - y - 1))
                }
            }
        }
    }

    fun findPathByValue(
        from: Point,
        to: Point,
        notAllowedValues: Set<T> = emptySet(),
        diagonal: Boolean = false,
    ): Path {
        val notAllowed = notAllowedValues.map { find(it) }.flatten().toSet()
        return findPath(from, setOf(to), notAllowed, diagonal)
    }

    fun findPath(
        from: Point,
        to: Point,
        notAllowedLocations: Set<Point> = emptySet(),
        diagonal: Boolean = false,
        customAllowedFunction: (grid: Matrix<T>, from: Point, to: Point, path: Path) -> Boolean = { _, _, _, _ -> true },
        customScoreFunction: (grid: Matrix<T>, from: Point, to: Point, path: Path) -> Int = { _, _, _, _ -> 1 },
    ): Path {
        return findPath(from, setOf(to), notAllowedLocations, diagonal, customAllowedFunction, customScoreFunction)
    }

    fun findPath(
        from: Point,
        to: Set<Point>,
        notAllowedLocations: Set<Point> = emptySet(),
        diagonal: Boolean = false,
        customAllowedFunction: (grid: Matrix<T>, from: Point, to: Point, path: Path) -> Boolean = { _, _, _, _ -> true },
        customScoreFunction: (grid: Matrix<T>, from: Point, to: Point, path: Path) -> Int = { _, _, _, _ -> 1 },
    ): Path {
        val paths = PriorityQueue<Path>()
        val seen: MutableSet<Point> = mutableSetOf()
        seen.addAll(notAllowedLocations)

        // Function to filter allowed locations
        fun isAllowed(from: Point, to: Point, path: Path): Boolean {
            if (seen.contains(to)) return false
            if (notAllowedLocations.contains(to)) return false
            if (!customAllowedFunction(this, from, to, path)) return false
            return true
        }

        fun getNeighbours(point: Point, path: Path): List<Point> {
            return getNeighbours(point, diagonal = diagonal).filter { isAllowed(point, it, path) }.sorted()
        }

        // Start with the neighbours of the starting point that are allowed to visit.
        paths.add(Path(listOf(from)))

        while (paths.isNotEmpty()) {
            val path = paths.poll()
            val pathEnd: Point = path.locations.last()

            // Arrived at destination?
            if (to.contains(pathEnd)) {
                return path
            }

            // Continue only new locations
            if (pathEnd !in seen) {
                seen.add(pathEnd)

                for (neighbour in getNeighbours(pathEnd, path)) {
                    val score = customScoreFunction(this, pathEnd, neighbour, path)
                    paths.add(Path(path.locations + neighbour, path.score + score))
                }
            }
        }
        return Path(emptyList())
    }

    // Note to self: When this function is not working, maybe it can be solved faster with a graph of fixed paths?
    fun findAllPaths(
        from: Point,
        to: Point,
        notAllowedLocations: Set<Point> = emptySet(),
        diagonal: Boolean = false,
        customAllowedFunction: (grid: Matrix<T>, from: Point, to: Point, path: Path) -> Boolean = { _, _, _, _ -> true },
        customScoreFunction: (grid: Matrix<T>, from: Point, to: Point, path: Path) -> Int = { _, _, _, _ -> 1 },
    ): List<Path> {
        val seen: MutableSet<Point> = notAllowedLocations.toMutableSet()

        // Function to filter allowed locations
        fun isAllowed(from: Point, to: Point, path: Path): Boolean {
            if (seen.contains(to)) return false
            if (notAllowedLocations.contains(to)) return false
            if (!customAllowedFunction(this, from, to, path)) return false
            return true
        }

        fun getNeighbours(point: Point, path: Path): List<Point> {
            return getNeighbours(point, diagonal = diagonal).filter { isAllowed(point, it, path) }.sorted()
        }

        fun dfs(from: Point, paths: MutableList<Path>, path: Path = Path(listOf(from))) {
            seen.add(from)

            if (from == to) {
                paths.add(path)
            } else {
                for (neighbour in getNeighbours(from, path)) {
                    val score = customScoreFunction(this, from, neighbour, path)
                    val p = Path(path.locations + neighbour, path.score + score)
                    dfs(neighbour, paths, p)
                }
            }

            // Backtrack
            seen.remove(from)
        }

        val paths = mutableListOf<Path>()
        dfs(from, paths)
        return paths
    }

    fun waterFill(
        start: Point,
        notAllowedValues: Set<T>,
        horizontal: Boolean = true,
        vertical: Boolean = true,
        diagonal: Boolean = true,
    ): Set<Point> {
        val watered = mutableSetOf<Point>(start)
        val inspect = mutableSetOf(start)
        val notAllowed = find(notAllowedValues).toSet()
        while (inspect.isNotEmpty()) {
            val neighbours = inspect.map {
                getNeighbours(it, horizontal = horizontal, vertical = vertical, diagonal = diagonal)
            }.flatten().toSet() - watered - notAllowed
            watered += neighbours

            inspect.clear()
            inspect.addAll(neighbours)
        }
        return watered
    }

    open fun copy(): Matrix<T> {
        val inputCopy = input.map { it -> it.map { it }.toMutableList() }.toMutableList()
        return Matrix(inputCopy)
    }

    fun toString(separatorElement: String, separatorLine: String): String {
        val builder = StringBuilder()
        for (y in 0 until height()) {
            for (x in 0 until width()) {
                val value = get(x, y)
                builder.append(value)
                builder.append(separatorElement)
            }
            builder.delete(builder.length - separatorElement.length, builder.length)
            builder.append(separatorLine)
        }
        return builder.removeSuffix(separatorLine).toString()
    }

    override fun toString(): String {
        return toString("", "\n")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix<*>

        if (input != other.input) return false

        return true
    }

    override fun hashCode(): Int {
        return input.hashCode()
    }

    companion object {
        fun <T> buildDefault(width: Int, height: Int, defaultValue: T): Matrix<T> {
            val rows = mutableListOf<MutableList<T>>()
            for (y in 0 until height) {
                val row = mutableListOf<T>()
                for (x in 0 until width) {
                    row.add(defaultValue)
                }
                rows.add(row)
            }
            return Matrix(rows)
        }
    }
}

open class MatrixString(input: MutableList<MutableList<String>>) : Matrix<String>(input) {

    override fun copy(): MatrixString {
        return MatrixString(super.copy().input)
    }

    companion object {
        fun buildDefault(width: Int, height: Int, defaultValue: String): MatrixString {
            return MatrixString(Matrix.buildDefault(width, height, defaultValue).input)
        }

        fun build(input: List<List<String>>): MatrixString {
            return MatrixString(input.map { it.toMutableList() }.toMutableList())
        }

        fun build(other: Matrix<*>): MatrixString {
            val output = buildDefault(other.width(), other.height(), " ")
            for (point in other.allPoints()) {
                output.set(point, other.get(point).toString())
            }
            return output
        }
    }
}

open class MatrixInt(input: MutableList<MutableList<Int>>) : Matrix<Int>(input) {

    override fun copy(): MatrixInt {
        return MatrixInt(super.copy().input)
    }

    companion object {
        fun buildDefault(width: Int, height: Int, defaultValue: Int): MatrixInt {
            return MatrixInt(Matrix.buildDefault(width, height, defaultValue).input)
        }

        fun build(input: List<List<String>>): MatrixInt {
            return MatrixInt(input.map { list -> list.map { it.toInt() }.toMutableList() }.toMutableList())
        }
    }
}

open class MatrixLong(input: MutableList<MutableList<Long>>) : Matrix<Long>(input) {

    override fun copy(): MatrixLong {
        return MatrixLong(super.copy().input)
    }

    companion object {
        fun buildDefault(width: Int, height: Int, defaultValue: Long): MatrixLong {
            return MatrixLong(Matrix.buildDefault(width, height, defaultValue).input)
        }

        fun build(input: List<List<String>>): MatrixLong {
            return MatrixLong(input.map { list -> list.map { it.toLong() }.toMutableList() }.toMutableList())
        }
    }
}
