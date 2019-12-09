package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day6Test {

    @Test
    fun `single orbit`() {
        val day6 = Day6(listOf("COM)B"))
        assertThat(day6.checksum()).isEqualTo(1)
    }

    @Test
    fun `Example orbits`() {
        val day6 = Day6("""
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
        """.trimIndent().lines())
        assertThat(day6.checksum()).isEqualTo(42)
    }

    @Test
    fun `Part 2 - intersections`() {
        val day6 = Day6("""
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            K)YOU
            I)SAN
        """.trimIndent().lines())
        assertThat(day6.intersection()).isEqualTo(4)
    }
}