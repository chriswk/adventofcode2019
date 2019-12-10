package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class Day10Test {

    @Test
    fun `finds most visible asteroids to be 8`() {
        val day10 = Day10()
        val asteroids = day10.parseInput(
            """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent().lines()
        )
        assertThat(day10.visible(asteroids).max()).isEqualTo(8)
    }

    @ParameterizedTest
    @MethodSource("largerExamples")
    fun `handles larger examples`(input: String, expectedCount: Int) {
        val day10 = Day10()
        val asteroids = day10.parseInput(input.trimIndent().lines())
        assertThat(day10.visible(asteroids).max()).isEqualTo(expectedCount)
    }

    companion object {
        @JvmStatic
        fun largerExamples(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("""
                    ......#.#.
                    #..#.#....
                    ..#######.
                    .#.#.###..
                    .#..#.....
                    ..#....#.#
                    #..#....#.
                    .##.#..###
                    ##...#..#.
                    .#....####
                """.trimIndent(), 33
                ),
                Arguments.of(
                    """
                        #.#...#.#.
                        .###....#.
                        .#....#...
                        ##.#.#.#.#
                        ....#.#.#.
                        .##..###.#
                        ..#...##..
                        ..##....##
                        ......#...
                        .####.###.
                    """.trimIndent(), 35
                ),
                Arguments.of(
                    """
                .#..#..###
                ####.###.#
                ....###.#.
                ..###.##.#
                ##.##.#.#.
                ....###..#
                ..#.#..#.#
                #..#.#.###
                .##...##.#
                .....#.#..
            """.trimIndent(), 41
                ),
                Arguments.of(
                    """
                .#..##.###...#######
                ##.############..##.
                .#.######.########.#
                .###.#######.####.#.
                #####.##.#.##.###.##
                ..#####..#.#########
                ####################
                #.####....###.#.#.##
                ##.#################
                #####.##.###..####..
                ..######..##.#######
                ####.##.####...##..#
                .#####..#.######.###
                ##...#.##########...
                #.##########.#######
                .####.#.###.###.#.##
                ....##.##.###..#####
                .#.#.###########.###
                #.#.#.#####.####.###
                ###.##.####.##.#..##
            """.trimIndent(), 210
                )
            )
        }
    }
}