package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


class Day1Test {

    @ParameterizedTest
    @MethodSource(value = ["provideWeights"])
    fun `finding fuel`(weight: Int, fuel: Int) {
        val day1 = Day1()
        assertThat(day1.findFuel(weight)).isEqualTo(fuel)
    }

    @ParameterizedTest
    @MethodSource(value = ["provideWeights2"])
    fun `include weight of fuel in calculation for fuel`(weight:Int, fuel: Int) {
        val day1 = Day1()
        assertThat(day1.findFuel2(weight)).isEqualTo(fuel)

    }
    companion object {
        @JvmStatic
        fun provideWeights(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(12, 2),
                Arguments.of(14, 2),
                Arguments.of(1969, 654),
                Arguments.of(100756, 33583)
            )
        }
        @JvmStatic
        fun provideWeights2(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(14, 2),
                Arguments.of(1969, 966),
                Arguments.of(100756, 50346)
            )
        }

    }
}