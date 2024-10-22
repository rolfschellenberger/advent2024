package com.rolf.util

fun String.isNumeric() = toIntOrNull() != null
fun String.getNumbers() = this.filter { it.isDigit() }

fun joinSideBySide(lines: List<String>): String {
    var height = 0
    var textWidth = 0
    for (line in lines) {
        val rows = line.lines()
        height = maxOf(height, rows.size)
        for (row in rows) {
            textWidth = maxOf(textWidth, row.length)
        }
    }
    val result = MatrixString.buildDefault(lines.size * 2 - 1, height, " ".repeat(textWidth))

    for ((index, group) in lines.withIndex()) {
        val x = index * 2
        val rows = group.lines()
        for ((y, row) in rows.withIndex()) {
            result.set(x, y, row)
        }
    }

    // Clean up spaces in between to one space
    for (y in 0 until result.height()) {
        for (x in 1 until result.width() step 2) {
            result.set(x, y, " ")
        }
    }

    return result.toString()
}

fun getCharacterCounts(hash: String, inOrder: Boolean = true, uniqueChars: Boolean = false): Map<Int, List<Char>> {
    val result = mutableMapOf<Int, MutableList<Char>>()
    var lastChar: Char? = null
    var sequence = 0
    val input = if (inOrder) hash else hash.toCharArray().sorted().joinToString("")
    for (char in input) {
        if (lastChar == null || lastChar == char) {
            sequence++
        } else {
            result.computeIfAbsent(sequence) { mutableListOf() }
            result[sequence]?.add(lastChar)
            sequence = 1
        }
        lastChar = char
    }
    if (lastChar != null) {
        result.computeIfAbsent(sequence) { mutableListOf() }
        result[sequence]?.add(lastChar)
    }

    if (uniqueChars) {
        for ((_, value) in result) {
            val unique = value.toSet().toMutableList()
            value.clear()
            value.addAll(unique)
        }
    }

    return result
}
