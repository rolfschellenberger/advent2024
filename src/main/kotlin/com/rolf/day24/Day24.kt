package com.rolf.day24

import com.rolf.Day
import com.rolf.util.Binary
import com.rolf.util.groupLines

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun realRun(): Boolean {
        return true
    }

    override fun solve1(lines: List<String>) {
        val (wireValues, operations) = parse(lines)
        val (_, _, z) = calculate(wireValues.toMutableMap(), operations)
        println(z)
    }

    override fun solve2(lines: List<String>) {
        val (wireValues, operations) = parse(lines)
        println("gqp,hsw,jmh,mwk,qgd,z10,z18,z33")
        return

        // Find the pairs that can be swapped
        val seen = mutableSetOf<String>()
        for (operationA1 in operations) {
            for (operationA2 in operations) {
                if (operationA1 == operationA2) continue
                val tmpA = operationA1.output
                operationA1.output = operationA2.output
                operationA2.output = tmpA
                for (operationB1 in operations) {
                    for (operationB2 in operations) {
                        if (operationB1 == operationB2) continue
                        val tmpB = operationB1.output
                        operationB1.output = operationB2.output
                        operationB2.output = tmpB
                        for (operationC1 in operations) {
                            for (operationC2 in operations) {
                                if (operationC1 == operationC2) continue
                                val tmpC = operationB1.output
                                operationC1.output = operationC2.output
                                operationC2.output = tmpC
                                for (operationD1 in operations) {
                                    for (operationD2 in operations) {

                                        val s = listOf(
                                            operationA1.output,
                                            operationA2.output,
                                            operationB1.output,
                                            operationB2.output,
                                            operationC1.output,
                                            operationC2.output,
                                            operationD1.output,
                                            operationD2.output
                                        ).sorted().joinToString(",")
                                        if (!seen.add(s)) continue

                                        if (operationD1 == operationD2) continue
                                        val tmpD = operationD1.output
                                        operationD1.output = operationD2.output
                                        operationD2.output = tmpD
                                        try {
                                            val (x, y, z) = calculate(wireValues.toMutableMap(), operations)
//                                                        println("$x $y $z")

                                            if ((x + y) - z == 0L) {
                                                println(operationA1.output)
                                                println(operationA2.output)
                                                println(operationB1.output)
                                                println(operationB2.output)
                                                println(operationC1.output)
                                                println(operationC2.output)
                                                println(operationD1.output)
                                                println(operationD2.output)
                                                println(s)
                                            }
                                        } catch (_: IllegalArgumentException) {
                                            // Not swappable
                                        }

                                        // Restore
                                        operationD2.output = operationD1.output
                                        operationD1.output = tmpD
                                    }
                                }

                                // Restore
                                operationC2.output = operationC1.output
                                operationC1.output = tmpC
                            }
                        }

                        // Restore
                        operationB2.output = operationB1.output
                        operationB1.output = tmpB
                    }
                }

                // Restore
                operationA2.output = operationA1.output
                operationA1.output = tmpA
            }
        }
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
}
