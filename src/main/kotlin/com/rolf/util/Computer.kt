package com.rolf.util

abstract class Computer(val instructions: List<Instruction>, var pointer: Int = 0, val defaultValue: Long = 0L) {
    val registers = mutableMapOf<String, Long>()
    var shouldStop = false

    fun getValue(key: String): Long {
        if (key.isNumeric()) {
            return key.toLong()
        }

        registers.computeIfAbsent(key) { defaultValue }
        return registers[key]!!
    }

    fun setValue(key: String, value: String) {
        registers[key] = getValue(value)
    }

    fun setValue(key: String, value: Long) {
        registers[key] = value
    }

    fun execute() {
        while (!isDone()) {
            executeNext()
            if (shouldStop) {
                return
            }
        }
    }

    fun executeNext() {
        execute(instructions[pointer])
    }

    fun isDone(): Boolean {
        return pointer >= instructions.size
    }

    abstract fun execute(instruction: Instruction)
}

class Instruction(val input: String) {
    val operator: String
    val parts: List<String>

    init {
        val split = input.split(" ")
        operator = split[0]
        parts = if (split.size > 1) split.subList(1, split.size) else emptyList()
    }

    override fun toString(): String {
        return "Instruction(operator='$operator', parts=$parts)"
    }

    operator fun get(i: Int): String {
        return parts[i]
    }
}
