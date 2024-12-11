package com.rolf.day11

import com.rolf.util.subLong

data class Stone(val value: Long) {
    fun evolve(): List<Stone> {
        if (value == 0L) {
            return listOf(Stone(1))
        }

        val valueString = value.toString()
        if (valueString.length % 2 == 0) {
            return listOf(
                Stone(value.subLong(0, valueString.length / 2)),
                Stone(value.subLong(valueString.length / 2))
            )
        }

        return listOf(Stone(value * 2024))
    }
}
