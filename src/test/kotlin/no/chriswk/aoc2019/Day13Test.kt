package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13Test {

    @Test
    fun part1() {
        val day13 = Day13()
        assertThat(day13.part1()).isEqualTo(239)
    }

    @Test
    fun part2() {
        val day13 = Day13()
        assertThat(day13.part2()).isEqualTo(12099)
    }
}