package com.rolf.day07

data class Equation(val value: Long, val numbers: List<Long>) {
    fun isValid(operators: List<String>): Boolean {
        return isValid(value, numbers, operators)
    }

    private fun isValid(value: Long, numbers: List<Long>, operators: List<String>): Boolean {
        if (numbers.isEmpty()) {
            throw IllegalArgumentException("No numbers found")
        }
        if (numbers[0] > value) {
            return false
        }
        if (numbers.size == 1) {
            return value == numbers[0]
        }

        val left = numbers[0]
        val right = numbers[1]

        val remainingNumbers = numbers.subList(2, numbers.size)
        val results = getResults(left, right, operators)
        for (result in results) {
            val newNumbers = listOf(result) + remainingNumbers
            if (isValid(value, newNumbers, operators)) {
                return true
            }
        }
        return false
    }

    private fun getResults(left: Long, right: Long, operators: List<String>): List<Long> {
        return operators.map { operator ->
            when (operator) {
                "*" -> left * right
                "+" -> left + right
                "||" -> (left.toString() + right.toString()).toLong()
                else -> throw IllegalArgumentException("Unknown operator: $operator")
            }
        }
    }
}
