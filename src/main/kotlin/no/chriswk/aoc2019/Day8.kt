package no.chriswk.aoc2019

import java.lang.IllegalStateException

class Day8 {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val day8 = Day8()
            report { day8.part1(25, 6) }
            report { day8.part2(25, 6); 2}
        }
    }

    fun layers(image: String, wide: Int, height: Int): List<Map<Char, Int>> {
        return image.chunked(wide * height).map { layer -> layer.groupingBy { it }.eachCount() }
    }

    fun part1(width: Int, height: Int): Int {
        val input = "day8.txt".fileToString()
        val layers = layers(input, width, height)
        return layers.minBy { it.getOrDefault('0', 0) }?.let {
            it.getOrDefault('1', 0) * it.getOrDefault('2', 0)
        } ?: throw IllegalStateException("Corrupt image")
    }

    fun List<String>.pixelAt(at: Int): Char =
            if (map { it[at] }.firstOrNull { it != '2' } == '1') {
                '#'
            } else {
                ' '
            }

    fun part2(width: Int, height: Int) {
        val layers = "day8.txt".fileToString().chunked(width * height)
        (0 until width * height)
                .map { layers.pixelAt(it) }
                .chunked(width)
                .forEach { println(it.joinToString(separator = ""))}
    }
}