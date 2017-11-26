package com.nilhcem.blenamebadge

import org.amshove.kluent.`should be equal to`
import org.junit.Test

class SampleTest {

    @Test
    fun `should be true`() {
        val test = true
        test `should be equal to` true
    }
}
