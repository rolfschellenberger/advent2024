package com.rolf.util

import java.math.BigInteger

open class Binary(var value: Long, val bits: Int = 16) {

    constructor(value: String, bits: Int = 16) : this(toLong(value), bits)

    val length = bits
    private val maskValue: Long = (1L shl bits) - 1

    init {
        value = mask(value)
    }

    private fun mask(value: Long): Long {
        return value and maskValue
    }

    fun and(other: Long): Binary {
        value = mask(value and other)
        return this
    }

    fun and(other: String): Binary {
        return and(toLong(other))
    }

    fun or(other: Long): Binary {
        value = mask(value or other)
        return this
    }

    fun or(other: String): Binary {
        return or(toLong(other))
    }

    fun xor(other: Long): Binary {
        value = mask(value xor other)
        return this
    }

    fun xor(other: String): Binary {
        return xor(toLong(other))
    }

    fun not(): Binary {
        value = mask(value.inv())
        return this
    }

    fun shiftLeft(bits: Int): Binary {
        value = mask(value shl bits)
        return this
    }

    fun shiftRight(bits: Int): Binary {
        value = mask(value shr bits)
        return this
    }

    fun toString(leadingZeros: Boolean = false): String {
        val string = BigInteger.valueOf(value).toString(2)
        if (leadingZeros) {
            return string.padStart(bits, '0')
        }
        return string
    }

    fun copy(newBits: Int = bits): Binary {
        return Binary(value, newBits)
    }

    override fun toString(): String {
        return toString(false)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Binary

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    operator fun get(index: Int): Boolean {
        return value and (1L shl index) != 0L
    }

    operator fun set(index: Int, newValue: Boolean) {
        val v = if (newValue) {
            value or (1L shl index)
        } else {
            value and (1L shl index).inv()
        }
        value = mask(v)
    }

    operator fun plus(increment: Binary): Binary {
        return Binary(value + increment.value, bits)
    }

    operator fun minus(decrement: Binary): Binary {
        return Binary(value - decrement.value, bits)
    }

    operator fun times(factor: Binary): Binary {
        return Binary(value * factor.value, bits)
    }

    operator fun div(factor: Binary): Binary {
        return Binary(value / factor.value, bits)
    }

    companion object {
        private fun toLong(binaryNumber: String): Long {
            var sum = 0L
            binaryNumber.reversed().forEachIndexed { k, v ->
                sum += (v.toString().toInt() * (1L shl k))
            }
            return sum
        }
    }
}
