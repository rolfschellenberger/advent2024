package com.rolf.util

import java.util.regex.Pattern

fun readLines(fileName: String): List<String> {
    return {}.javaClass.getResource(fileName)?.readText()?.split("\n") ?: throw Exception("File '$fileName' was not found")
}

fun splitLine(line: String, delimiter: String = "", pattern: Pattern? = null, chunkSize: Int = 1): List<String> {
    if (pattern != null) {
        return splitLine(line, pattern)
    }
    if (delimiter.isEmpty()) {
        return splitLine(line, maxOf(1, chunkSize))
    }
    return splitLine(line, delimiter)
}

private fun splitLine(line: String, chunkSize: Int): List<String> {
    return line.chunked(chunkSize)
}

private fun splitLine(line: String, delimiter: String): List<String> {
    return line.split(delimiter)
}

private fun splitLine(line: String, pattern: Pattern): List<String> {
    return line.split(pattern)
}

fun splitLines(
    lines: List<String>,
    delimiter: String = "", // Split by a delimiter
    pattern: Pattern? = null, // Or split by a pattern (regex)
    chunkSize: Int = 1 // Or split by a fixed length
): List<List<String>> {
    return lines.map { splitLine(it, delimiter, pattern, chunkSize) }
}

fun groupLines(lines: List<String>, match: String): List<List<String>> {
    val groups = mutableListOf<MutableList<String>>()
    var group = mutableListOf<String>()

    for (line in lines) {
        if (line == match) {
            groups.add(group)
            group = mutableListOf()
        } else {
            group.add(line)
        }
    }
    groups.add(group)
    return groups
}

fun removeLastEmptyLine(lines: List<String>): List<String> {
    if (lines.isNotEmpty() && lines.last().isEmpty()) {
        return lines.subList(0, lines.size - 1)
    }
    return lines
}
