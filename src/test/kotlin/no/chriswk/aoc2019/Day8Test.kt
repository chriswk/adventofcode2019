package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day8Test {

    @Test
    fun `parses image`() {
        val day8 = Day8()
        val layers = day8.layers("123452789012", wide = 3, height = 2)
        assertThat(layers).hasSize(2)
        assertThat(layers[0]).containsEntry('2', 2)
    }
}