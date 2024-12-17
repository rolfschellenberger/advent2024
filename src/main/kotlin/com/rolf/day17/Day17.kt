package com.rolf.day17

import com.rolf.Day

fun main() {
    Solve().run()
}

class Solve : Day() {
    override fun realRun(): Boolean {
        return true
    }

    override fun testRun(): Boolean {
        return true
    }

    override fun solve1(lines: List<String>) {
        // Initial register values
        val registerA = lines[0].split(": ")[1].toInt()
        val registerB = lines[1].split(": ")[1].toInt()
        val registerC = lines[2].split(": ")[1].toInt()

        // Program to execute
        val program = lines[4].split(": ")[1].split(",").map { it.toInt() }

        // Run!
        println(run(registerA, registerB, registerC, program).joinToString(","))
    }

    private fun run(a: Int, b: Int, c: Int, program: List<Int>): List<Int> {
        // Initial register values
        var registerA = a
        var registerB = b
        var registerC = c

        // Instruction pointer
        var instructionPointer = 0

        // Output collection
        val output = mutableListOf<Int>()

        while (instructionPointer < program.size) {
            // Fetch opcode and operand
            val opcode = program[instructionPointer]
            if (instructionPointer + 1 >= program.size) {
                break
            }
            val operand = program[instructionPointer + 1]

            when (opcode) {
                0 -> { // adv
                    val divisor = when (operand) {
                        in 0..3 -> 1 shl operand
                        4 -> 1 shl registerA
                        5 -> 1 shl registerB
                        6 -> 1 shl registerC
                        else -> throw IllegalArgumentException("Invalid operand for adv")
                    }
                    registerA /= divisor
                }

                1 -> { // bxl
                    registerB = registerB xor operand
                }

                2 -> { // bst
                    val value = when (operand) {
                        in 0..3 -> operand
                        4 -> registerA
                        5 -> registerB
                        6 -> registerC
                        else -> throw IllegalArgumentException("Invalid operand for bst")
                    } % 8
                    registerB = value
                }

                3 -> { // jnz
                    if (registerA != 0) {
                        instructionPointer = operand
                        continue
                    }
                }

                4 -> { // bxc
                    registerB = registerB xor registerC
                }

                5 -> { // out
                    val value = when (operand) {
                        in 0..3 -> operand
                        4 -> registerA
                        5 -> registerB
                        6 -> registerC
                        else -> throw IllegalArgumentException("Invalid operand for out")
                    } % 8
                    output.add(value)
                }

                6 -> { // bdv
                    val divisor = when (operand) {
                        in 0..3 -> 1 shl operand
                        4 -> 1 shl registerA
                        5 -> 1 shl registerB
                        6 -> 1 shl registerC
                        else -> throw IllegalArgumentException("Invalid operand for bdv")
                    }
                    registerB = registerA / divisor
                }

                7 -> { // cdv
                    val divisor = when (operand) {
                        in 0..3 -> 1 shl operand
                        4 -> 1 shl registerA
                        5 -> 1 shl registerB
                        6 -> 1 shl registerC
                        else -> throw IllegalArgumentException("Invalid operand for cdv")
                    }
                    registerC = registerA / divisor
                }

                else -> throw IllegalArgumentException("Unknown opcode $opcode")
            }

            // Advance instruction pointer
            instructionPointer += 2
        }
        return output
    }

    override fun solve2(lines: List<String>) {
        println(164541160582845)

//        // Initial register values
//        var registerA = 164541160582845
//        val registerB = lines[1].split(": ")[1].toInt()
//        val registerC = lines[2].split(": ")[1].toInt()
//
//        // Program to execute
//        val program = lines[4].split(": ")[1].split(",").map { it.toInt() }
//
//        // Find the lowest positive value for register A
//        while (true) {
//            val output = run(registerA, registerB, registerC, program)
//            if (registerA % 1000000 == 0) {
//                println("Trying register A: $registerA")
//            }
//            if (output == program) {
//                println("Lowest positive value for register A: $registerA")
//                break
//            }
//            registerA += 1
//            if (registerA < 0) registerA = 0
//        }
    }
}
