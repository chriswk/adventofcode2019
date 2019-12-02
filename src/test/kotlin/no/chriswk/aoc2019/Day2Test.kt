package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Day2Test {

    @Test
    fun `handles sample input`() {
        val day = Day2()
        val instructions = day.runProgram(day.parseProgram("1,9,10,3,2,3,11,0,99,30,40,50"))
        assertThat(instructions).isEqualTo(arrayOf(3500,9,10,70,2,3,11,0,99,30,40,50))
    }

    @ParameterizedTest
    @MethodSource("smallPrograms")
    fun `runs programs as expected`(input: String, output: Array<Int>) {
        val day = Day2()
        val instructions = day.runProgram(day.parseProgram(input))
        assertThat(instructions).isEqualTo(output)
    }

    companion object {

        @JvmStatic
        fun smallPrograms(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("1,0,0,0,99", arrayOf(2,0,0,0,99)),
                Arguments.of("2,3,0,3,99", arrayOf(2,3,0,6,99)),
                Arguments.of("2,4,4,5,99,0", arrayOf(2,4,4,5,99,9801)),
                Arguments.of("1,1,1,4,99,5,6,0,99", arrayOf(30,1,1,4,2,5,6,0,99))
            )
        }
    }
}