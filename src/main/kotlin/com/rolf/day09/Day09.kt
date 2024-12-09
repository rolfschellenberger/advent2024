package com.rolf.day09

import com.rolf.Day
import com.rolf.util.splitLine

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val numbers = lines.map { splitLine(it).map { it.toInt() } }.flatten()
        val array = toArray(numbers)

        // Move the right most non-zero number to the first -1 position in the array
        // Repeat until there are only -1's at the end of the array
        while (!sorted(array)) {
            val lastPositiveNumber = array.indexOfLast { it >= 0 }
            val firstNegativeNumber = array.indexOfFirst { it < 0 }
            array[firstNegativeNumber] = array[lastPositiveNumber]
            array[lastPositiveNumber] = -1
        }

        println(
            array.withIndex().sumOf { (index, number) ->
                if (number > 0) {
                    index * number.toLong()
                } else {
                    0
                }
            }
        )
    }

    private fun toArray(numbers: List<Int>): IntArray {
        val array = IntArray(numbers.sum()) { -1 }
        var file = true
        var pos = 0
        var fileId = 0
        for (number in numbers) {
            // Create N elements in the array
            for (i in 0 until number) {
                if (file) {
                    array[pos] = fileId
                }
                pos++
            }
            // Increment file id when moving to the next file
            if (file) {
                fileId++
            }
            file = !file
        }
        return array
    }

    private fun sorted(array: IntArray): Boolean {
        val lastNonNegativeNumber = array.indexOfLast { it >= 0 }
        val firstNegativeNumber = array.indexOfFirst { it < 0 }
        return firstNegativeNumber > lastNonNegativeNumber
    }

    override fun solve2(lines: List<String>) {
        val numbers = lines.map { splitLine(it).map { it.toInt() } }.flatten()
        val chunks = toChunks(numbers).toMutableList()

        // From right to left: try to move each chunk to the left most available free space
        for (i in chunks.indices.reversed()) {
            val chunk = chunks[i]
            if (chunk.isFile) {
                val firstEmptySpot = firstEmptySpot(chunks.subList(0, i), chunk)
                if (firstEmptySpot >= 0) {
                    // Reduce empty spot
                    chunks[firstEmptySpot] =
                        chunks[firstEmptySpot].copy(size = chunks[firstEmptySpot].size - chunk.size)
                    // Move chunk
                    chunks.removeAt(i)
                    chunks.add(firstEmptySpot, chunk)
                    chunks.add(i, Chunk(chunk.size, false, chunk.fileId))
                }
            }
        }

        var sum = 0L
        var index = 0
        for (chunk in chunks) {
            if (chunk.isFile) {
                for (i in 0 until chunk.size) {
                    val chunkIndex = index + i
                    sum += chunkIndex * chunk.fileId
                }
            }
            index += chunk.size
        }
        println(sum)
    }

    private fun toChunks(numbers: List<Int>): List<Chunk> {
        val chunks = mutableListOf<Chunk>()
        var fileId = 0
        var file = true
        for (number in numbers) {
            chunks.add(Chunk(number, file, fileId))
            if (file) {
                fileId++
            }
            file = !file
        }
        return chunks
    }

    private fun firstEmptySpot(
        chunks: MutableList<Chunk>,
        chunk: Chunk,
    ): Int {
        for ((index, space) in chunks.withIndex()) {
            if (!space.isFile && space.size >= chunk.size) {
                return index
            }
        }
        return -1
    }
}
