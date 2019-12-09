package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day7Test {

    @Test
    fun `Finds max thruster signal to be 43210`() {
        val day7 = Day7(parseInstructions("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"))
        assertThat(day7.part1()).isEqualTo(43210)
    }
}