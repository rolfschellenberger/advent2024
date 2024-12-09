package com.rolf.day09

import com.rolf.Day
import com.rolf.util.splitLine

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun solve1(lines: List<String>) {
        val numbers = lines.map { splitLine(it).map { it.toInt() } }.flatten()
//        println(numbers)

        val array = IntArray(numbers.sum()) { -1 }
//        println(array.toList())
        var file = true
        var pos = 0
        var fileId = 0
        for (number in numbers) {
            for (i in 0 until number) {
//                println("number: $number, fileId: $fileId, file: $file, pos: $pos")
                if (file) {
                    array[pos] = fileId
                }
                pos++
            }
            if (file) {
                fileId++
            }
            file = !file
//            println()
        }
//        println(array.toList())

        // Move the right most non-zero number to the first -1 position in the array
        // Repeat until there are only -1's at the end of the array
        while (!sorted(array)) {
//            println(array.toList())

            val lastPositiveNumber = array.indexOfLast { it >= 0 }
            val firstNegativeNumber = array.indexOfFirst { it < 0 }
            array[firstNegativeNumber] = array[lastPositiveNumber]
            array[lastPositiveNumber] = -1
        }

//        println(array.toList().joinToString(""))

        var sum = 0L
        for ((index, number) in array.withIndex()) {
            if (number > 0) {
                sum += index * number
            }
        }
        println(sum)
    }

    private fun sorted(array: IntArray): Boolean {
        val lastNonNegativeNumber = array.indexOfLast { it >= 0 }
        val firstNegativeNumber = array.indexOfFirst { it < 0 }
        return firstNegativeNumber > lastNonNegativeNumber
    }

    override fun solve2(lines: List<String>) {
        val numbers = lines.map { splitLine(it).map { it.toInt() } }.flatten()
//        println(numbers)
        val chunks = toChunks(numbers).toMutableList()
//        chunks.forEach { println(it) }

        // From right to left: try to move each chunk to the left most available free space
        for (i in chunks.indices.reversed()) {
//            println(i)
//            println(chunks[i])
            val chunk = chunks[i]
            if (chunk.isFile) {
                val firstEmptySpot = firstEmptySpot(chunks.subList(0, i), chunk)
//                println(firstEmptySpot)
                if (firstEmptySpot >= 0) {
                    // Reduce empty spot
                    chunks[firstEmptySpot] =
                        chunks[firstEmptySpot].copy(size = chunks[firstEmptySpot].size - chunk.size)
                    // Move chunk
                    chunks.removeAt(i)
                    chunks.add(firstEmptySpot, chunk)
                    chunks.add(i, Chunk(chunk.size, false, chunk.fileId))
//                    chunks[firstEmptySpot] = chunk
//                    chunks[i] = chunk.copy(isFile = false)
                }
            }
//            println(chunks)
//            printChunks(chunks)
//            println()
        }
//        printChunks(chunks)

        var sum = 0L
        var index = 0
        for (chunk in chunks) {
            if (chunk.isFile) {
                for (i in 0 until chunk.size) {
                    val chunkIndex = index + i
                    sum += chunkIndex * chunk.fileId
//                    println("index: ${index + i}")
//                    println(chunk.fileId)
                }
//                println(chunk)
//                sum += index * chunk.fileId
            }
            index += chunk.size
        }
        println(sum)
    }

    private fun printChunks(chunks: MutableList<Chunk>) {
        val builder = StringBuilder()

        for (chunk in chunks) {
            if (chunk.isFile) {
                for (i in 0 until chunk.size) {
                    builder.append(chunk.fileId)
                }
            } else {
                for (i in 0 until chunk.size) {
                    builder.append(".")
                }
            }
        }

        println(builder.toString())
    }

    private fun firstEmptySpot(
        chunks: MutableList<Chunk>,
        chunk: Chunk,
    ): Int {
//        println(chunks)
        for ((index, space) in chunks.withIndex()) {
            if (!space.isFile && space.size >= chunk.size) {
                return index
            }
        }
        return -1
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

//    private fun firstEmptySpot(array: MutableList<Chunk>, minLength: Int): Int {
//        // Find a minimum number of -1's in a row in the array
//        var length = 0
//        for ((index, number) in array.withIndex()) {
//            if (number == -1) {
//                length++
//            } else {
//                length = 0
//            }
//            if (length >= minLength) {
//                return index - length
//            }
//        }
//        return -1
//    }
}
