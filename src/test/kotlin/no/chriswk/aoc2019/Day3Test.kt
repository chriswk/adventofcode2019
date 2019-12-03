package no.chriswk.aoc2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3Test {

    @Test
    fun `Calculates correct manhattan distance between two points`() {
        val a = Point(2,2)
        val b = Point(0, 0)
        assertThat(a.manhattan(b)).isEqualTo(4)
    }

    @Test
    fun `A Point can create a route`() {
        val c = Point(0, 0)
        val route = c.route("R3").toList()
        assertThat(route).containsExactly(Point(1, 0), Point(2, 0), Point(3, 0))
    }
    @Test
    fun `A more complicated route`() {
        val c = Point(0, 0)
        val route = c.route("R3,U3").toList()
        assertThat(route).isEqualTo(listOf(Point(1, 0), Point(2, 0), Point(3, 0), Point(3, 1), Point(3, 2), Point(3, 3)))
    }

    @Test
    fun `Finds intersection`() {
        val origo = Point(0, 0)
        val routeA = origo.route("R5,U5")
        val routeB = origo.route("U3,R6")
        val day3 = Day3()
        val intersections = day3.routeCollides(routeA, routeB)
        assertThat(intersections).isNotEmpty
        assertThat(intersections).containsExactly(Point(5, 3))
    }

    @Test
    fun `Finds intersection closest to central port`() {
        val origo = Point(0, 0)
        val routeA = origo.route("R8,U5,L5,D3")
        val routeB = origo.route("U7,R6,D4,L4")
        val day3 = Day3()
        val intersections = day3.routeCollides(routeA, routeB)
        assertThat(intersections).hasSize(2)
        assertThat(day3.closestToCentral(intersections)).isEqualTo(Point(3,3))
        assertThat(day3.manhattanForClosest(intersections)).isEqualTo(6)
    }

    @Test
    fun `Example case 1`() {
        val origo = Point(0, 0)
        val routeA = origo.route("R75,D30,R83,U83,L12,D49,R71,U7,L72")
        val routeB = origo.route("U62,R66,U55,R34,D71,R55,D58,R83")
        val day3 = Day3()
        val intersections = day3.routeCollides(routeA, routeB)
        assertThat(day3.manhattanForClosest(intersections)).isEqualTo(159)
    }
    @Test
    fun `Example case 2`() {
        val origo = Point(0, 0)
        val routeA = origo.route("R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51")
        val routeB = origo.route("U98,R91,D20,R16,D67,R40,U7,R15,U6,R7")
        val day3 = Day3()
        val intersections = day3.routeCollides(routeA, routeB)
        assertThat(day3.manhattanForClosest(intersections)).isEqualTo(135)
    }

    @Test
    fun `Steps used to reach`() {
        val origo = Point(0, 0)
        val routeA = origo.route("R8,U5,L5,D3")
        val day3 = Day3()
        assertThat(day3.steps(routeA, Point(3, 3))).isEqualTo(20)
    }

    @Test
    fun `Chooses correct collision point with correct amount of steps`() {
        val origo = Point(0, 0)
        val routeA = origo.route("R8,U5,L5,D3")
        val routeB = origo.route("U7,R6,D4,L4")
        val day3 = Day3()
        assertThat(day3.closestToCentralBySteps(routeA, routeB)).isEqualTo(30)

    }

}