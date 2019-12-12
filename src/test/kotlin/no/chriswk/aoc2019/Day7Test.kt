package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day7Test {

    @Test
    fun part1() {
        val day7 = Day7()
        assertThat(day7.part1()).isEqualTo(95757)
    }
    @Test
    fun part2() {
        val day7 = Day7()
        assertThat(day7.part2()).isEqualTo(4275738)
    }
    @Test
    fun `Finds max thruster signal to be 43210`() {
        val day7 = Day7(parseInstructions("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0"))
        assertThat(day7.part1()).isEqualTo(43210)
    }
    @Test
    fun `amplified loop is read correctly`() {
        val computer = Day7(parseInstructions("""3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"""))
        assertThat(computer.part2()).isEqualTo(139629729)
    }
}