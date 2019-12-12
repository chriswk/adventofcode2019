package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day12Test {

    @Test
    fun part1() {
        val day12 = Day12()
        assertThat(day12.part1()).isEqualTo(8742)
    }

    @Test
    fun part2() {
        val day12 = Day12()
        assertThat(day12.part2()).isEqualTo(325433763467176)
    }
    @Test
    fun `Parses body format`() {
        val location = Point3D.fromString("<x=-2, y=5, z=7>")
        assertThat(location).isEqualTo(Point3D(-2, 5, 7))
    }

    @Test
    fun `Example handled`() {
        val example = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()
        val day12 = Day12()
        val bodies = day12.parseLocations(example.lines())
        val simulator = Simulator(bodies)
        simulator.step(2)
        assertThat(simulator.moons).containsExactly(
            Body(Point3D(5, -3, -1), Point3D(3, -2, -2)),
            Body(Point3D(1,-2, 2), Point3D(-2,5,6)),
            Body(Point3D(1, -4, -1), Point3D(0, 3, -6)),
            Body(Point3D(1, -4, 2), Point3D(-1, -6, 2))
        )
    }
}