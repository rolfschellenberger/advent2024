package com.rolf.util

import org.junit.Assert.assertEquals
import org.junit.Test

class AlgorithmsTest {

    @Test
    fun testMd5() {
        assertEquals("912EC803B2CE49E4A541068D495AB570", md5("asdf"))
        assertEquals("AF1AD8C76FDA2E48EA9AED2937E972EA", md5("bla bla bla"))
        assertEquals("91E19391E839E5E98306843F87443A85", md5("235346656"))
        assertEquals("91e19391e839e5e98306843f87443a85", md5("235346656", uppercase = false))
        assertEquals("D41D8CD98F00B204E9800998ECF8427E", md5(""))
    }
}
