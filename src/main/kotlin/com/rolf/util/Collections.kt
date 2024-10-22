package com.rolf.util

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun IntArray.swap(a: Int, b: Int): IntArray {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
    return this
}

fun IntArray.pushLeft(steps: Int): IntArray {
    return pushRight(size - (steps % size))
}

fun IntArray.pushRight(steps: Int): IntArray {
    val result = takeLast(steps % size) + dropLast(steps % size)
    for ((index, r) in result.withIndex()) {
        set(index, r)
    }
    return this
}

fun CharArray.swap(a: Int, b: Int): CharArray {
    val tmp = this[a]
    this[a] = this[b]
    this[b] = tmp
    return this
}

fun CharArray.swap(a: Char, b: Char): CharArray {
    return swap(indexOf(a), indexOf(b))
}

fun CharArray.pushLeft(steps: Int): CharArray {
    return pushRight(size - (steps % size))
}

fun CharArray.pushRight(steps: Int): CharArray {
    val result = takeLast(steps % size) + dropLast(steps % size)
    for ((index, r) in result.withIndex()) {
        set(index, r)
    }
    return this
}

fun IntRange.size(): Int {
    return this.last - this.first + 1
}

fun IntRange.hasOverlap(other: IntRange): Boolean {
    return !(other.first > last || other.last < first)
}

fun IntRange.overlap(other: IntRange): IntRange? {
    if (!hasOverlap(other)) return null
    return max(first, other.first)..min(last, other.last)
}

fun IntRange.contains(other: IntRange): Boolean {
    return other.first >= first && other.last <= last
}

fun IntRange.toLongRange(): LongRange {
    return LongRange(this.first.toLong(), this.last.toLong())
}

fun IntRange.remove(other: IntRange?): List<IntRange> {
    if (other == null) return listOf(this)

    val ranges = this.toLongRange().remove(other.toLongRange())
    return ranges.map { it.toIntRange() }
}

fun IntRange.split(other: IntRange): List<IntRange> {
    val ranges = this.toLongRange().split(other.toLongRange())
    return ranges.map { it.toIntRange() }
}

fun IntRange.add(value: Int): IntRange {
    return this.first + value..this.last + value
}

fun LongRange.size(): Long {
    return this.last - this.first + 1
}

fun LongRange.hasOverlap(other: LongRange): Boolean {
    return !(other.first > last || other.last < first)
}

fun LongRange.overlap(other: LongRange): LongRange? {
    if (!hasOverlap(other)) return null
    return max(first, other.first)..min(last, other.last)
}

fun LongRange.contains(other: LongRange): Boolean {
    return other.first >= first && other.last <= last
}

fun LongRange.toIntRange(): IntRange {
    return IntRange(this.first.toInt(), this.last.toInt())
}

fun LongRange.remove(other: LongRange?): List<LongRange> {
    if (other == null) return listOf(this)

    // If there is no overlap, return this
    val overlap = overlap(other) ?: return listOf(this)

    // When their overlap is equal to this, we remove everything
    if (overlap == this) return emptyList()

    // When they partly overlap at the beginning of the end, keep the remainder
    // They overlap from the beginning
    if (overlap.first == this.first) {
        return listOf(overlap.last + 1..this.last)
    }
    // They overlap at the end
    if (overlap.last == this.last) {
        return listOf(this.first..<overlap.first)
    }

    // Otherwise there will be an overlap somewhere in the middle
    return listOf(
        this.first..<overlap.first,
        overlap.last + 1..this.last
    )
}

fun LongRange.split(other: LongRange): List<LongRange> {
    // When there is no overlap, there is no need to split
    val overlap = overlap(other) ?: return listOf(this)

    // When the overlap is equal to this, no split is needed
    if (overlap == this) return listOf(this)

    // When the overlap is at the beginning
    if (overlap.first == this.first) {
        return listOf(
            (this.first..overlap.last),
            (overlap.last + 1..this.last),
        )
    }

    // When the overlap is at the end
    if (overlap.last == this.last) {
        return listOf(
            (this.first..<overlap.first),
            (overlap.first..this.last),
        )
    }

    // Otherwise there will be an overlap somewhere in the middle
    return listOf(
        this.first..<overlap.first,
        overlap.first..overlap.last,
        overlap.last + 1..this.last
    )
}

fun LongRange.add(value: Long): LongRange {
    return this.first + value..this.last + value
}

fun <T> ArrayDeque<T>.shift(n: Int) {
    when {
        n < 0 -> repeat(n.absoluteValue) {
            addLast(removeFirst())
        }

        else -> repeat(n) {
            addFirst(removeLast())
        }
    }
}

fun List<*>.endsWith(other: List<*>): Boolean {
    return if (this.size < other.size) false
    else this.slice(this.size - other.size until this.size) == other
}

/**
 * options: [a, b, c]
 * return: [[a, b, c], [a, c, b], [b, a, c], [b, c, a], [c, a, b], [c, b, a]]
 */
fun <T> getPermutations(
    options: List<T>,
    size: Int = options.size,
    position: Int = 0,
    cache: MutableMap<String, List<List<T>>> = mutableMapOf()
): List<List<T>> {
    if (position >= size || options.isEmpty()) {
        return listOf(emptyList())
    }
    if (options.size == 1) {
        return listOf(listOf(options[0]))
    }

    val key = options.joinToString { "-" }
    if (cache.containsKey(key)) {
        return cache[key]!!
    }

    val result = mutableListOf<List<T>>()
    for (option in options) {
        val newOptions = options.toMutableList()
        newOptions.remove(option)
        for (permutation in getPermutations(newOptions, size, position + 1)) {
            result.add(listOf(option) + permutation)
        }
    }
    cache[key] = result
    return result
}

fun <T> getPermutations(options: List<T>, onNextPermutation: (List<T>) -> Unit, prefix: List<T> = emptyList()) {
    if (options.isEmpty()) {
        onNextPermutation(prefix)
    }
    for (option in options) {
        val newOptions = options.toMutableList()
        newOptions.remove(option)
        getPermutations(newOptions, onNextPermutation, prefix + option)
    }
}

/**
 * options: [a, b, c]
 * return: ```[[a, b, c], [a, b], [a, c], [a], [b, c], [b], [c]]```
 */
fun <T> getCombinations(options: List<T>, cache: MutableMap<String, List<List<T>>> = mutableMapOf()): List<List<T>> {
    if (options.isEmpty()) {
        return emptyList()
    }
    if (options.size == 1) {
        return listOf(listOf(options.first()))
    }

    val key = options.joinToString { "-" }
    if (cache.containsKey(key)) {
        return cache[key]!!
    }

    val result = mutableListOf<List<T>>()
    for (index in 1..options.size) {
        val current = options[index - 1]
        val subList = options.subList(index, options.size)
        for (p in getCombinations(subList, cache)) {
            result.add(listOf(current) + p)
        }
        result.add(listOf(current))
    }
    cache[key] = result
    return result
}

fun <T> getCombinations(
    options: List<T>,
    onNextCombination: (List<T>) -> Unit,
    earlyTermination: (List<T>) -> Boolean = { _ -> false },
    prefix: List<T> = emptyList()
) {
    if (earlyTermination(prefix)) {
        return
    }
    for (index in 1..options.size) {
        val current = options[index - 1]
        val newOptions = options.subList(index, options.size)
        getCombinations(newOptions, onNextCombination, earlyTermination, prefix + current)
    }
    if (prefix.isNotEmpty()) {
        onNextCombination(prefix)
    }
}

fun <T, U> findPairs(input: Map<T, Set<U>>): Map<T, U> {
    // Create mutable options to distribute
    val options = input.map { it.key to it.value.toMutableList() }.toMap().toMutableMap()
    val result = mutableMapOf<T, U>()

    var lastSize = -1
    while (result.size != lastSize) {
        lastSize = result.size
        options.filter { it.value.size == 1 }.forEach { result[it.key] = it.value.first() }

        // Any unique values found?
        val valueCounts = options.values.flatten().groupingBy { it }.eachCount()
        val singleValues = valueCounts.filter { it.value == 1 }.map { it.key }
        for (option in options) {
            for (singleValue in singleValues) {
                if (option.value.contains(singleValue)) {
                    result[option.key] = singleValue
                }
            }
        }

        // Remove all found options
        for (element in result) {
            options.remove(element.key)
            for (option in options) {
                option.value.remove(element.value)
            }
        }

        // Last option remaining?
        if (options.size == 1) {
            val key = options.keys.first()
            if (options[key]!!.isEmpty()) {
                val value = (input.values.flatten().toSet() - result.values).first()
                result[key] = value
            }
        }
    }
    if (result.size != input.size) throw Exception("No matching was possible")
    return result
}
