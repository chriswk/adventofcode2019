package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day2Test {
    @Test
    fun `Expected answer from part 1 is 3706713`() {
        val day2 = Day2()
        assertThat(day2.part1()).isEqualTo(3706713L)
    }
    @Test
    fun `Expected answer from part 2 is 8609`() {
        val day2 = Day2()
        assertThat(day2.part2()).isEqualTo(8609L)
    }
}