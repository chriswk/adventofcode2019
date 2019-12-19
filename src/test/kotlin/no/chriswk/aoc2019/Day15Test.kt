package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day15Test {

    @Test
    fun part1() {
        val day15 = Day15()
        assertThat(day15.part1(parseBigInstructions("day15.txt".fileToString()).toMutableMap())).isEqualTo(240)

    }

    @Test
    fun part2() {
        val day15 = Day15()
        assertThat(day15.part2(parseBigInstructions("day15.txt".fileToString()).toMutableMap())).isEqualTo(322)
    }
}
