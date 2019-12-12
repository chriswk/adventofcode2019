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

    @Test
    fun `Finds expected target for shooting`() {
        val day10 = Day10()
        val asteroids = day10.parseInput(largeExample.lines())
        val station = day10.findStation(asteroids)
        assertThat(day10.shoot(station!!, asteroids).drop(199).first().part2()).isEqualTo(802)
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
                Arguments.of(largeExample, 210)
            )
        }
        val largeExample = """
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
            """.trimIndent()
    }
}