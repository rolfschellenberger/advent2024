package com.rolf.day25

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.groupLines
import com.rolf.util.splitLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun realRun(): Boolean {
        return true
    }

    override fun solve1(lines: List<String>) {
        val groups = groupLines(lines, "")
        val (locks, keys) = parse(groups)

        var count = 0
        for (lock in locks) {
            for (key in keys) {
                if (fits(lock, key)) {
                    count++
                }
            }
        }
        println(count)
    }

    private fun fits(lock: List<Int>, key: List<Int>): Boolean {
        if (lock.size != key.size) {
            return false
        }
        for (i in lock.indices) {
            if (lock[i] + key[i] > 7) return false
        }
        return true
    }

    private fun parse(groups: List<List<String>>): Pair<List<List<Int>>, List<List<Int>>> {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        for (group in groups) {
            val grid = MatrixString.build(splitLines(group))
            val isLock = grid.getTopEdge().all { it == "#" }
            val pattern = mutableListOf<Int>()
            for (x in 0 until grid.width()) {
                val col = grid.getColumn(x)
                val filled = col.count { it == "#" }
                pattern.add(filled)
            }
            if (isLock) {
                locks.add(pattern)
            } else {
                keys.add(pattern)
            }
        }
        return Pair(locks, keys)
    }

    override fun solve2(lines: List<String>) {
    }
}
