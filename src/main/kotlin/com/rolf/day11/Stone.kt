package com.rolf.day11

data class Stone(val value: Long) {
    fun evolve(): List<Stone> {
        if (value == 0L) {
            return listOf(Stone(1))
        }

        val valueString = value.toString()
        if (valueString.length % 2 == 0) {
            return listOf(
                Stone(valueString.substring(0, valueString.length / 2).toLong()),
                Stone(valueString.substring(valueString.length / 2).toLong())
            )
        }

        return listOf(Stone(value * 2024))
    }
}
