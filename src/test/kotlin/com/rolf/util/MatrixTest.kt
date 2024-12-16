package com.rolf.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MatrixTest {

    private var matrix = MatrixString.buildDefault(10, 20, ".")

    @Before
    fun before() {
        matrix = MatrixString.buildDefault(10, 20, ".")
    }

    @Test
    fun testAllElements() {
        assertEquals(200, matrix.allElements().size)
        assertEquals(200, matrix.allElements().filter { it == "." }.size)

        matrix.set(3, 3, "a")
        matrix.set(5, 5, "b")
        assertEquals(200, matrix.allElements().size)
        assertEquals(198, matrix.allElements().filter { it == "." }.size)
    }

    @Test
    fun testAllPoints() {
        assertTrue(matrix.allPoints().toSet().contains(Point(9, 19)))

        assertEquals(200, matrix.allPoints().size)
        val xValues = matrix.allPoints().groupBy { it.x }
        assertEquals(10, xValues.size)
        for (y in xValues.values) {
            assertEquals(20, y.size)
        }

        val yValues = matrix.allPoints().groupBy { it.y }
        assertEquals(20, yValues.size)
        for (x in yValues.values) {
            assertEquals(10, x.size)
        }
    }

    @Test
    fun testHeight() {
        assertEquals(20, matrix.height())
    }

    @Test
    fun testWidth() {
        assertEquals(10, matrix.width())
    }

    @Test
    fun testIsOutside() {
        assertTrue(matrix.isOutside(-1, 3))
        assertFalse(matrix.isOutside(0, 3))
        assertFalse(matrix.isOutside(9, 3))
        assertTrue(matrix.isOutside(10, 3))

        assertTrue(matrix.isOutside(3, -1))
        assertFalse(matrix.isOutside(3, 0))
        assertFalse(matrix.isOutside(3, 19))
        assertTrue(matrix.isOutside(3, 20))

        assertTrue(matrix.isOutside(Point(10, 20)))
        assertFalse(matrix.isOutside(Point(3, 3)))
    }

    @Test
    fun testGetColumns() {
        val columns = matrix.getColumns()
        assertEquals(10, columns.size)
        assertEquals(20, columns[0].size)
    }

    @Test
    fun testGetColumn() {
        val column = matrix.getColumn(3)
        assertEquals(20, column.size)
        assertEquals(".", column[0])
        assertEquals(".", column[1])

        matrix.set(3, 1, "B")
        assertEquals(".", column[0])
        assertEquals(".", column[1])
        val newColumn = matrix.getColumn(3)
        assertEquals(".", newColumn[0])
        assertEquals("B", newColumn[1])
    }

    @Test
    fun testGetRows() {
        val rows = matrix.getRows()
        assertEquals(20, rows.size)
        assertEquals(10, rows[0].size)
    }

    @Test
    fun testGetRow() {
        val row = matrix.getRow(3)
        assertEquals(10, row.size)
        assertEquals(".", row[0])
        assertEquals(".", row[1])

        matrix.set(1, 3, "B")
        assertEquals(".", row[0])
        assertEquals(".", row[1])
        val newRow = matrix.getRow(3)
        assertEquals(".", newRow[0])
        assertEquals("B", newRow[1])
    }

    @Test
    fun testEdges() {
        matrix.set(matrix.topLeft(), "A")
        matrix.set(matrix.topRight(), "B")
        matrix.set(matrix.bottomLeft(), "C")
        matrix.set(matrix.bottomRight(), "D")

        assertEquals("A", matrix.getTopEdge().first())
        assertEquals("B", matrix.getTopEdge().last())
        assertEquals("C", matrix.getBottomEdge().first())
        assertEquals("D", matrix.getBottomEdge().last())
        assertEquals("A", matrix.getLeftEdge().first())
        assertEquals("C", matrix.getLeftEdge().last())
        assertEquals("B", matrix.getRightEdge().first())
        assertEquals("D", matrix.getRightEdge().last())
    }

    @Test
    fun testTopEdgePoints() {
        val edgePoints = matrix.getTopEdgePoints()
        assertEquals(10, edgePoints.size)
        assertEquals(
            listOf(
                Point(0, 0),
                Point(1, 0),
                Point(2, 0),
                Point(3, 0),
                Point(4, 0),
                Point(5, 0),
                Point(6, 0),
                Point(7, 0),
                Point(8, 0),
                Point(9, 0)
            ), edgePoints
        )
    }

    @Test
    fun testBottomEdgePoints() {
        val edgePoints = matrix.getBottomEdgePoints()
        assertEquals(10, edgePoints.size)
        assertEquals(
            listOf(
                Point(0, 19),
                Point(1, 19),
                Point(2, 19),
                Point(3, 19),
                Point(4, 19),
                Point(5, 19),
                Point(6, 19),
                Point(7, 19),
                Point(8, 19),
                Point(9, 19)
            ), edgePoints
        )
    }

    @Test
    fun testLeftEdgePoints() {
        val edgePoints = matrix.getLeftEdgePoints()
        assertEquals(20, edgePoints.size)
        assertEquals(
            listOf(
                Point(0, 0),
                Point(0, 1),
                Point(0, 2),
                Point(0, 3),
                Point(0, 4),
                Point(0, 5),
                Point(0, 6),
                Point(0, 7),
                Point(0, 8),
                Point(0, 9),
                Point(0, 10),
                Point(0, 11),
                Point(0, 12),
                Point(0, 13),
                Point(0, 14),
                Point(0, 15),
                Point(0, 16),
                Point(0, 17),
                Point(0, 18),
                Point(0, 19)
            ), edgePoints
        )
    }

    @Test
    fun testRightEdgePoints() {
        val edgePoints = matrix.getRightEdgePoints()
        assertEquals(20, edgePoints.size)
        assertEquals(
            listOf(
                Point(9, 0),
                Point(9, 1),
                Point(9, 2),
                Point(9, 3),
                Point(9, 4),
                Point(9, 5),
                Point(9, 6),
                Point(9, 7),
                Point(9, 8),
                Point(9, 9),
                Point(9, 10),
                Point(9, 11),
                Point(9, 12),
                Point(9, 13),
                Point(9, 14),
                Point(9, 15),
                Point(9, 16),
                Point(9, 17),
                Point(9, 18),
                Point(9, 19)
            ), edgePoints
        )
    }

    @Test
    fun testCenter() {
        val m1 = MatrixString.buildDefault(5, 5, ".")
        assertEquals(Point(2, 2), m1.center())

        val m2 = MatrixString.buildDefault(10, 10, ".")
        assertEquals(Point(5, 5), m2.center())
    }

    @Test
    fun testGetSetCount() {
        assertEquals(200, matrix.count("."))
        assertEquals(0, matrix.count("A"))
        assertEquals(0, matrix.count("B"))
        assertEquals(0, matrix.count("C"))

        val point = Point(3, 5)
        assertEquals(".", matrix.get(point))
        assertEquals(".", matrix.get(3, 5))
        matrix.set(point, "B")
        assertEquals("B", matrix.get(point))
        matrix.set(6, 7, "C")
        assertEquals("C", matrix.get(6, 7))

        assertEquals(198, matrix.count("."))
        assertEquals(0, matrix.count("A"))
        assertEquals(1, matrix.count("B"))
        assertEquals(1, matrix.count("C"))
        assertEquals(2, matrix.count(setOf("A", "B", "C")))
    }

    @Test
    fun testWrap() {
        assertEquals(Point(0, 0), matrix.wrap(Point(0, 0)))
        assertEquals(Point(1, 5), matrix.wrap(Point(1, 5)))
        assertEquals(Point(3, 5), matrix.wrap(Point(13, 5)))
        assertEquals(Point(6, 5), matrix.wrap(Point(1056, 5)))
        assertEquals(Point(6, 2), matrix.wrap(Point(1056, 5542)))
        assertEquals(Point(3, 17), matrix.wrap(Point(3, 437)))
        assertEquals(Point(9, 15), matrix.wrap(Point(-1, -5)))
        assertEquals(Point(6, 4), matrix.wrap(Point(-4354, -1236)))
    }

    @Test
    fun testLeft() {
        assertNull(matrix.getLeft(Point(0, 4)))
        assertEquals(Point(9, 0), matrix.getLeft(Point(0, 0), wrap = true))
        assertEquals(Point(6, 4), matrix.getLeft(Point(-93, 4), wrap = true))
    }

    @Test
    fun testRight() {
        assertNull(matrix.getRight(Point(9, 4)))
        assertEquals(Point(0, 0), matrix.getRight(Point(9, 0), wrap = true))
        assertEquals(Point(8, 4), matrix.getRight(Point(-93, 4), wrap = true))
    }

    @Test
    fun testUp() {
        assertNull(matrix.getUp(Point(4, 0)))
        assertEquals(Point(0, 19), matrix.getUp(Point(0, 0), wrap = true))
        assertEquals(Point(4, 3), matrix.getUp(Point(4, -56), wrap = true))
    }

    @Test
    fun testDown() {
        assertNull(matrix.getDown(Point(4, 19)))
        assertEquals(Point(0, 0), matrix.getDown(Point(0, 19), wrap = true))
        assertEquals(Point(4, 5), matrix.getDown(Point(4, -56), wrap = true))
    }

    @Test
    fun testLeftUp() {
        assertNull(matrix.getLeftUp(Point(5, 0)))
        assertNull(matrix.getLeftUp(Point(0, 5)))
        assertEquals(Point(9, 19), matrix.getLeftUp(Point(0, 0), wrap = true))
        assertEquals(Point(5, 3), matrix.getLeftUp(Point(-4, -56), wrap = true))
    }

    @Test
    fun testLeftDown() {
        assertNull(matrix.getLeftDown(Point(5, 19)))
        assertNull(matrix.getLeftDown(Point(0, 5)))
        assertEquals(Point(9, 0), matrix.getLeftDown(Point(0, 19), wrap = true))
        assertEquals(Point(5, 5), matrix.getLeftDown(Point(-4, -56), wrap = true))
    }

    @Test
    fun testRightUp() {
        assertNull(matrix.getRightUp(Point(5, 0)))
        assertNull(matrix.getRightUp(Point(9, 5)))
        assertEquals(Point(0, 19), matrix.getRightUp(Point(9, 0), wrap = true))
        assertEquals(Point(7, 3), matrix.getRightUp(Point(-4, -56), wrap = true))
    }

    @Test
    fun testRightDown() {
        assertNull(matrix.getRightDown(Point(5, 19)))
        assertNull(matrix.getRightDown(Point(9, 5)))
        assertEquals(Point(0, 0), matrix.getRightDown(Point(9, 19), wrap = true))
        assertEquals(Point(7, 5), matrix.getRightDown(Point(-4, -56), wrap = true))
    }

    @Test
    fun testGetForward() {
        assertEquals(Point(3, 3), matrix.getForward(Point(3, 4), Direction.NORTH))
        assertEquals(Point(4, 4), matrix.getForward(Point(3, 4), Direction.EAST))
        assertEquals(Point(3, 5), matrix.getForward(Point(3, 4), Direction.SOUTH))
        assertEquals(Point(2, 4), matrix.getForward(Point(3, 4), Direction.WEST))
    }

    @Test
    fun testGetAllDirections() {
        val m1 = MatrixString.buildDefault(10, 10, ".")
        val directions1 = m1.getAllDirections(m1.center())
        assertEquals(64, directions1.size)

        val m2 = MatrixString.buildDefault(5, 5, ".")
        val directions2 = m2.getAllDirections(Point(3, 4))
        assertTrue(directions2.contains(Point(1, -4)))
        assertEquals(16, directions2.size)
    }

    @Test
    fun testGetArea() {
        val from = Point(4, 6)
        val to = Point(8, 9)
        matrix.set(from, "A")
        matrix.set(to, "B")

        val area = matrix.getArea(from, to)
        assertEquals(20, area.size)
        assertEquals(1, area.count { matrix.get(it) == "A" })
        assertEquals(1, area.count { matrix.get(it) == "B" })
        assertEquals(18, area.count { matrix.get(it) == "." })
    }

    @Test
    fun testSubMatrix() {
        matrix.set(3, 4, "B")
        matrix.set(5, 5, "C")
        val sub = matrix.subMatrix(Point(3, 4), Point(5, 5))
        assertEquals(3, sub.width())
        assertEquals(2, sub.height())
        assertEquals("B", sub.get(sub.topLeft()))
        assertEquals("C", sub.get(sub.bottomRight()))
    }

    @Test
    fun testCutOut() {
        matrix.set(3, 4, "B")
        matrix.set(5, 5, "C")
        assertEquals(10, matrix.width())
        assertEquals(20, matrix.height())
        assertEquals("B", matrix.get(3, 4))
        assertEquals("C", matrix.get(5, 5))

        matrix.cutOut(Point(3, 4), Point(7, 6))
        assertEquals(5, matrix.width())
        assertEquals(3, matrix.height())
        assertEquals("B", matrix.get(0, 0))
        assertEquals("C", matrix.get(2, 1))
    }

    @Test
    fun testGrow() {
        matrix.cutOut(Point(0, 0), Point(9, 4))
        assertEquals(10, matrix.width())
        assertEquals(5, matrix.height())

        matrix.grow(0, 5, 0, 0, "r")
        assertEquals(15, matrix.width())
        assertEquals(5, matrix.height())

        matrix.grow(0, 0, 0, 5, "b")
        assertEquals(15, matrix.width())
        assertEquals(10, matrix.height())

        matrix.grow(3, 0, 0, 0, "l")
        assertEquals(18, matrix.width())
        assertEquals(10, matrix.height())

        matrix.grow(0, 0, 4, 0, "t")
        assertEquals(18, matrix.width())
        assertEquals(14, matrix.height())

        val expected1 =
            """
            tttttttttttttttttt
            tttttttttttttttttt
            tttttttttttttttttt
            tttttttttttttttttt
            lll..........rrrrr
            lll..........rrrrr
            lll..........rrrrr
            lll..........rrrrr
            lll..........rrrrr
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            lllbbbbbbbbbbbbbbb
            """.trimIndent()
        assertEquals(expected1, matrix.toString())
    }

    @Test
    fun testToString() {
        val smaller = MatrixString.buildDefault(10, 4, "x")
        val expected1 =
            """
            xxxxxxxxxx
            xxxxxxxxxx
            xxxxxxxxxx
            xxxxxxxxxx
            """.trimIndent()
        assertEquals(expected1, smaller.toString())

        smaller.set(3, 2, "B")
        val expected2 =
            """
            xxxxxxxxxx
            xxxxxxxxxx
            xxxBxxxxxx
            xxxxxxxxxx
            """.trimIndent()
        assertEquals(expected2, smaller.toString())

        val expected3 = "x x x x x x x x x x|x x x x x x x x x x|x x x B x x x x x x|x x x x x x x x x x"
        assertEquals(expected3, smaller.toString(" ", "|"))
    }

    @Test
    fun testBuild() {
        val lines = readLines("/matrix.txt")
        val input = splitLines(lines, chunkSize = 1)
        val matrixString = MatrixString.build(input)
        assertEquals(100, matrixString.width())
        assertEquals(100, matrixString.height())
        assertEquals("8", matrixString.get(17, 7))

        val matrixInt = MatrixInt.build(input)
        assertEquals(100, matrixInt.width())
        assertEquals(100, matrixInt.height())
        assertEquals(8, matrixInt.get(17, 7))

        val matrixLong = MatrixLong.build(input)
        assertEquals(100, matrixLong.width())
        assertEquals(100, matrixLong.height())
        assertEquals(8L, matrixLong.get(17, 7))

        val intToString = MatrixString.build(matrixInt)
        assertEquals(100, intToString.width())
        assertEquals(100, intToString.height())
        assertEquals("8", intToString.get(17, 7))
    }

    @Test
    fun testMatrixInt() {
        val matrix = MatrixInt.buildDefault(5, 3, 4)
        val expected =
            """
            44444
            44444
            44444
            """.trimIndent()
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testMatrixLong() {
        val matrix = MatrixLong.buildDefault(2, 5, 5)
        val expected =
            """
            55
            55
            55
            55
            55
            """.trimIndent()
        assertEquals(expected, matrix.toString())
    }

    @Test
    fun testReplace() {
        matrix.set(3, 3, "a")
        matrix.set(3, 4, "a")
        matrix.set(3, 5, "a")
        matrix.set(4, 6, "b")
        matrix.set(4, 7, "b")

        assertEquals(3, matrix.count("a"))
        assertEquals(2, matrix.count("b"))
        assertEquals(195, matrix.count("."))
        assertEquals(0, matrix.count("z"))
        assertEquals(0, matrix.count("#"))

        val map = mapOf(
            "a" to "z",
            "." to "#"
        )
        matrix.replace(map)

        assertEquals(0, matrix.count("a"))
        assertEquals(2, matrix.count("b"))
        assertEquals(0, matrix.count("."))
        assertEquals(3, matrix.count("z"))
        assertEquals(195, matrix.count("#"))
    }

    @Test
    fun testRotate() {
        matrix.cutOut(Point(0, 0), Point(9, 9))
        matrix.set(matrix.topLeft(), "A")
        matrix.set(matrix.topRight(), "B")
        matrix.set(matrix.bottomLeft(), "C")
        matrix.set(matrix.bottomRight(), "D")

        matrix.rotateRight()

        assertEquals("C", matrix.get(matrix.topLeft()))
        assertEquals("A", matrix.get(matrix.topRight()))
        assertEquals("D", matrix.get(matrix.bottomLeft()))
        assertEquals("B", matrix.get(matrix.bottomRight()))
    }

    @Test
    fun testFlip() {
        matrix.cutOut(Point(0, 0), Point(9, 9))
        matrix.set(matrix.topLeft(), "A")
        matrix.set(matrix.topRight(), "B")
        matrix.set(matrix.bottomLeft(), "C")
        matrix.set(matrix.bottomRight(), "D")

        matrix.flip()

        assertEquals("B", matrix.get(matrix.topLeft()))
        assertEquals("A", matrix.get(matrix.topRight()))
        assertEquals("D", matrix.get(matrix.bottomLeft()))
        assertEquals("C", matrix.get(matrix.bottomRight()))

        matrix.flip(horizontal = false)

        assertEquals("D", matrix.get(matrix.topLeft()))
        assertEquals("C", matrix.get(matrix.topRight()))
        assertEquals("B", matrix.get(matrix.bottomLeft()))
        assertEquals("A", matrix.get(matrix.bottomRight()))
    }

    @Test
    fun testCopy() {
        val a = MatrixInt.buildDefault(4, 4, 0)
        a.set(3, 3, 3)
        val b = a.copy()
        a.set(1, 1, 1)
        assertEquals(3, b.get(3, 3))
        assertEquals(0, b.get(1, 1))
    }

    @Test
    fun testEquals() {
        val a = MatrixInt.buildDefault(3, 3, 0)
        val b = MatrixInt.buildDefault(3, 3, 1)
        assertFalse(a == b)

        b.replace(mapOf(1 to 0))
        assertTrue(a == b)
    }

    @Test
    fun testPath() {
        val input = """
            ..#.......
            .#........
            .#........
            .#........
            .#........
            .#........
            .#.#......
            .#.#......
            .#.#......
            ...#......
            """.trimIndent().lines()
        val maze = MatrixString.build(splitLines(input))
        val walls = maze.find("#").toSet()

        val start = Point(0, 0)
        val end = Point(9, 9)
        val distance = maze.findPath(start, end, walls)
        assertEquals(27, distance.size)

        // Diagonal test
        val distance2 = maze.findPath(start, end, walls, diagonal = true)
        assertEquals(11, distance2.size)

        // 2,6 closing path
        val notAllowed = Point(2, 6)
        val distance3 = maze.findPath(start, end, walls + notAllowed)
        assertTrue(distance3.isEmpty())

        // Find path does not use the maze
        maze.set(notAllowed, "#")
        val distance4 = maze.findPath(start, end, walls)
        assertEquals(27, distance4.size)
        maze.set(notAllowed, ".")

        // Find multiple locations
        val end2 = Point(3, 4)
        val distance5 = maze.findPath(start, setOf(end, end2), walls)
        assertEquals(18, distance5.size)

        // Find by ignoring the wall value
        val distance6 = maze.findPathByValue(start, end, setOf("#"))
        assertEquals(27, distance6.size)
        maze.set(notAllowed, "#")
        val distance7 = maze.findPathByValue(start, end, setOf("#"))
        assertEquals(0, distance7.size)
    }

    @Test
    fun testPriorityPath() {
        val input = """
            ..#.......
            ........b.
            .########a
            .#........
            .#.######.
            .#.#......
            .#.######.
            .#.#......
            .#.#......
            ...#......
            """.trimIndent().lines()
        val maze = MatrixString.build(splitLines(input))
        val walls = maze.find("#").toSet()

        val start = Point(0, 0)
        val end = Point(9, 9)
        val path = maze.findPath(start, end, walls)
        assertEquals(19, path.size)

        // Take a detour when "a" is expensive.
        val path2 = maze.findPath(start, end, walls, customScoreFunction = this::customScoreFunction1)
        assertEquals(31, path2.size)

        // Take a small detour when "b" is expensive.
        val path3 = maze.findPath(start, end, walls, customScoreFunction = this::customScoreFunction2)
        assertEquals(21, path3.size)
    }

    private fun customScoreFunction1(grid: Matrix<String>, from: Point, to: Point, path: Path): Int {
        return when (grid.get(to)) {
            "a" -> 100
            else -> 1
        }
    }

    private fun customScoreFunction2(grid: Matrix<String>, from: Point, to: Point, path: Path): Int {
        return when (grid.get(to)) {
            "b" -> 100
            else -> 1
        }
    }

    @Test
    fun testWaterFill() {
        val input = """
            ..........
            ..#.......
            .###......
            .#.#......
            .#..#.....
            .#...#....
            .#######..
            .#.##..#..
            .#.#.###..
            .#.#......
            .#######..
            ..........
            """.trimIndent().lines()
        val maze1 = MatrixString.build(splitLines(input))
        val waterPoints1 = maze1.waterFill(Point(0, 0), setOf("#"), diagonal = false)
        waterPoints1.forEach {
            maze1.set(it, "~")
        }
        val expected1 =
            """
            ~~~~~~~~~~
            ~~#~~~~~~~
            ~###~~~~~~
            ~#.#~~~~~~
            ~#..#~~~~~
            ~#...#~~~~
            ~#######~~
            ~#.##..#~~
            ~#.#~###~~
            ~#.#~~~~~~
            ~#######~~
            ~~~~~~~~~~
            """.trimIndent()
        assertEquals(expected1, maze1.toString())

        val maze2 = MatrixString.build(splitLines(input))
        val waterPoints2 = maze2.waterFill(Point(0, 0), setOf("#"), diagonal = true)
        waterPoints2.forEach {
            maze2.set(it, "~")
        }
        val expected2 =
            """
            ~~~~~~~~~~
            ~~#~~~~~~~
            ~###~~~~~~
            ~#~#~~~~~~
            ~#~~#~~~~~
            ~#~~~#~~~~
            ~#######~~
            ~#.##~~#~~
            ~#.#~###~~
            ~#.#~~~~~~
            ~#######~~
            ~~~~~~~~~~
            """.trimIndent()
        assertEquals(expected2, maze2.toString())

        val maze3 = MatrixString.build(splitLines(input))
        maze3.set(1,0, "#")
        maze3.set(1,1, "#")
        maze3.set(0,1, "#")
        val waterPoints3 = maze3.waterFill(Point(0, 0), setOf("#"), diagonal = false)
        waterPoints3.forEach {
            maze3.set(it, "~")
        }
        val expected3 = """
            ~#........
            ###.......
            .###......
            .#.#......
            .#..#.....
            .#...#....
            .#######..
            .#.##..#..
            .#.#.###..
            .#.#......
            .#######..
            ..........
            """.trimIndent()
        assertEquals(expected3, maze3.toString())
    }
}
