package com.rolf.day24

import com.rolf.Day
import com.rolf.util.Binary
import com.rolf.util.groupLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun testRun(): Boolean {
        return false
    }

    override fun solve1(lines: List<String>) {
        return
        val (wireValues, operations) = parse(lines)
        val (_, _, z) = calculate(wireValues.toMutableMap(), operations)
        println(z)
    }

    private fun parse(lines: List<String>): Pair<Map<String, Binary>, List<Operation>> {
        val (initialValues, gateDefinitions) = groupLines(lines, "")

        val wireValues = mutableMapOf<String, Binary>()
        for (initialValue in initialValues) {
            val (wire, value) = initialValue.split(": ")
            val binary = Binary(value, 1)
            wireValues[wire] = binary
        }

        val operations = mutableListOf<Operation>()
        for (gateDefinition in gateDefinitions) {
            val parts = gateDefinition.split(" ")
            val input1 = parts[0]
            val operation = parts[1]
            val input2 = parts[2]
            val output = parts[4]
            operations.add(Operation(input1, operation, input2, output))
        }
        return Pair(wireValues, operations)
    }

    private fun calculate(
        wireValues: MutableMap<String, Binary>,
        operations: List<Operation>,
    ): Triple<Long, Long, Long> {
        val toCalculate = operations.toMutableList()
        while (toCalculate.isNotEmpty()) {
            var found = false
            for (next in toCalculate) {
                if (wireValues.contains(next.input1) && wireValues.contains(next.input2)) {
                    val value = next.calculate(wireValues)
                    wireValues[next.output] = value
                    toCalculate.remove(next)
                    found = true
                    break
                }
            }
            if (!found) throw IllegalArgumentException("Cannot calculate")
        }

        val x = wireValues.filter { it.key.startsWith("x") }.toSortedMap()
        val y = wireValues.filter { it.key.startsWith("y") }.toSortedMap()
        val z = wireValues.filter { it.key.startsWith("z") }.toSortedMap()
        val xValue = Binary(x.values.reversed().joinToString("") { it.toString() }, 60).value
        val yValue = Binary(y.values.reversed().joinToString("") { it.toString() }, 60).value
        val zValue = Binary(z.values.reversed().joinToString("") { it.toString() }, 60).value
        return Triple(xValue, yValue, zValue)
    }

    override fun solve2(lines: List<String>) {
        val (wiresRaw, gatesRaw) = groupLines(lines, "")

        val wires = mutableMapOf<String, Int?>()
        for (line in wiresRaw) {
            val (name, value) = line.split(": ")
            wires[name] = value.toInt()
        }

        val inputBitCount = wiresRaw.size / 2

        val gates = mutableListOf<Map<String, String>>()
        for (line in gatesRaw) {
            val (inputs, output) = line.split(" -> ")
            val (a, op, b) = inputs.split(" ")
            gates.add(mapOf("a" to a, "op" to op, "b" to b, "output" to output))

            if (!wires.containsKey(a)) wires[a] = null
            if (!wires.containsKey(b)) wires[b] = null
            if (!wires.containsKey(output)) wires[output] = null
        }

        fun isDirect(gate: Map<String, String>): Boolean {
            return gate["a"]?.startsWith("x") == true || gate["b"]?.startsWith("x") == true
        }

        fun isOutput(gate: Map<String, String>): Boolean {
            return gate["output"]?.startsWith("z") == true
        }

        fun isGate(type: String): (Map<String, String>) -> Boolean {
            return { gate -> gate["op"] == type }
        }

        fun hasOutput(output: String): (Map<String, String>) -> Boolean {
            return { gate -> gate["output"] == output }
        }

        fun hasInput(input: String): (Map<String, String>) -> Boolean {
            return { gate -> gate["a"] == input || gate["b"] == input }
        }

        val flags = mutableSetOf<String>()

        val gate0s = gates.filter { isDirect(it) }.filter(isGate("XOR"))
        for (gate in gate0s) {
            val a = gate["a"]!!
            val b = gate["b"]!!
            val output = gate["output"]!!

            val isFirst = a == "x00" || b == "x00"
            if (isFirst) {
                if (output != "z00") {
                    flags.add(output)
                }
                continue
            } else if (output == "z00") {
                flags.add(output)
            }

            if (isOutput(gate)) {
                flags.add(output)
            }
        }

        val gate3s = gates.filter(isGate("XOR")).filter { !isDirect(it) }
        for (gate in gate3s) {
            if (!isOutput(gate)) {
                flags.add(gate["output"]!!)
            }
        }

        val outputGates = gates.filter { isOutput(it) }
        for (gate in outputGates) {
            val isLast = gate["output"] == "z${inputBitCount.toString().padStart(2, '0')}"
            if (isLast) {
                if (gate["op"] != "OR") {
                    flags.add(gate["output"]!!)
                }
                continue
            } else if (gate["op"] != "XOR") {
                flags.add(gate["output"]!!)
            }
        }

        val checkNext = mutableListOf<Map<String, String>>()
        for (gate in gate0s) {
            val output = gate["output"]!!

            if (flags.contains(output)) continue
            if (output == "z00") continue

            val matches = gate3s.filter(hasInput(output))
            if (matches.isEmpty()) {
                checkNext.add(gate)
                flags.add(output)
            }
        }

        for (gate in checkNext) {
            val a = gate["a"]!!

            val intendedResult = "z${a.substring(1)}"
            val matches = gate3s.filter(hasOutput(intendedResult))

            if (matches.size != 1) {
                throw Exception("Critical Error! Is your input correct?")
            }

            val match = matches[0]
            val toCheck = listOf(match["a"]!!, match["b"]!!)

            val orMatches = gates.filter(isGate("OR")).filter { toCheck.contains(it["output"]) }
            if (orMatches.size != 1) {
                throw Exception("Critical Error! This solver isn't complex enough to solve this")
            }

            val orMatchOutput = orMatches[0]["output"]!!
            val correctOutput = toCheck.find { it != orMatchOutput }!!
            flags.add(correctOutput)
        }

        if (flags.size != 8) {
            throw Exception("Critical Error! This solver isn't complex enough to solve this")
        }

        val flagsArr = flags.sorted()
        println(flagsArr.joinToString(","))
    }
}
