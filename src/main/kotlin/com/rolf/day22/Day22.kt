package com.rolf.day22

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun realRun(): Boolean {
        return true
    }

    override fun solve1(lines: List<String>) {
        var total = 0L
        val initialSecrets = lines.map { it.toLong() }
        for (secret in initialSecrets) {
            var tmp = secret
            repeat(2000) {
                tmp = generateNextSecret(tmp)
            }
            total += tmp
        }
        println(total)
    }

    override fun solve2(lines: List<String>) {
        val initialSecrets = lines.map { it.toLong() }

        // So we are going to inspect all secrets and 2000 steps to keep track of each sequences of 4 integers
        // and the next value that it would gain. This should give us the best sequence with the highest result.

        // Build a map of sequences that occur for the first time for each secret and their next value
        val allSequences = mutableSetOf<List<Int>>()
        val allSecretSequences = mutableListOf<Map<List<Int>, Int>>()

        for (secret in initialSecrets) {
            // Make a list of last numbers of each secret
            val lastNumbers = mutableListOf<Int>("$secret".last().digitToInt())

            var tmp = secret
            repeat(2000) {
                tmp = generateNextSecret(tmp)
                lastNumbers.add("$tmp".last().digitToInt())
            }

            // Transform the list of numbers into a map of delta sequences of length 4 and their next value
            val secretSequences = mutableMapOf<List<Int>, Int>()
            for (i in 0 until lastNumbers.size - 5) {
                val sequence = lastNumbers.subList(i, i + 5)
                val deltas = sequence.zipWithNext { a, b -> b - a }
                val bananas = sequence.last()
                // Only remember this delta sequence if it occurs for the first time
                if (!secretSequences.containsKey(deltas)) {
                    secretSequences[deltas] = bananas
                }
                allSequences.add(deltas)
            }
            allSecretSequences.add(secretSequences)
        }
        var maxBananas = Int.MIN_VALUE
        for (sequence in allSequences) {
            val totalBananas = allSecretSequences.mapNotNull { it[sequence] }.sum()
            maxBananas = maxOf(maxBananas, totalBananas)
        }
        println(maxBananas)
    }

    fun generateNextSecret(secret: Long): Long {
        var updatedSecret = secret

        // Step 1: Multiply by 64 and mix
        updatedSecret = mixAndPrune(updatedSecret, updatedSecret * 64)

        // Step 2: Divide by 32 (rounded down) and mix
        updatedSecret = mixAndPrune(updatedSecret, updatedSecret / 32)

        // Step 3: Multiply by 2048 and mix
        updatedSecret = mixAndPrune(updatedSecret, updatedSecret * 2048)

        return updatedSecret
    }

    fun mixAndPrune(secret: Long, value: Long): Long {
        val mixed = secret xor value
        return mixed % 16777216
    }
}
