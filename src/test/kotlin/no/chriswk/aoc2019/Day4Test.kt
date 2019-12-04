package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day4Test {

    @Test
    fun `is able to digitize number`() {
        val day4 = Day4()
        assertThat(day4.digits(173413)).isEqualTo(listOf(1,7,3,4,1,3))
    }

    @Test
    fun `validates 111111`() {
        val day4 = Day4()
        assertThat(day4.isValid(111111)).isTrue()
    }

    @Test
    fun `invalidates 223450`() {
        val day4 = Day4()
        assertThat(day4.isValid(223450)).isFalse()
    }
    @Test
    fun `invalidates 123789`() {
        val day4 = Day4()
        assertThat(day4.isValid(123789)).isFalse()
    }
    @Test
    fun `part2 invalidates 123444`() {
        val day4 = Day4()
        assertThat(day4.isValidByPart2(123444)).isFalse()
    }
    @Test
    fun `part2 keeps 111122 as valid`() {
        val day4 = Day4()
        assertThat(day4.isValidByPart2(111122)).isTrue()
    }

}