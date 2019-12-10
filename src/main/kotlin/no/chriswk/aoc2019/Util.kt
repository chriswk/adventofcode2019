package no.chriswk.aoc2019

import kotlinx.coroutines.channels.Channel
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.system.measureTimeMillis

class Util {}
val utf8 = Charset.forName("UTF-8")
fun String.toInputStream() = Util::class.java.classLoader.getResourceAsStream(this)
fun InputStream.lines() = this.readBytes().toString(utf8).lines()
fun String.fileToLines(): List<String> {
    val path = "/$this"
    val res = object {}.javaClass.getResource(path)
    return File(res.toURI()).readLines()
}

fun String.fileToString(): String {
    val path = "/$this"
    return object {}.javaClass.getResource(path).readText().substringBeforeLast("\n")
}

fun <T> List<T>.permutations(): List<List<T>> {
    return if (this.size <= 1) {
        listOf(this)
    } else {
        val elementToInsert = first()
        drop(1).permutations().flatMap { permutation ->
            (0..permutation.size).map { i ->
                permutation.toMutableList().apply { add(i, elementToInsert) }
            }
        }
    }
}

suspend fun <T> Channel<T>.andSend(msg: T) : Channel<T> = this.also { send(msg) }

fun <T> List<T>.toChannel(capacity: Int = Channel.UNLIMITED): Channel<T> {
    return Channel<T>(capacity).also { this.forEach { e -> it.offer(e) }}
}

fun report(f: () -> Number) {
    var ans: Number? = null
    val timeTaken = measureTimeMillis { ans = f() }
    println("Answer [$ans] - took $timeTaken ms")
}

fun parseInstructions(instructions: String): IntArray {
    return instructions.split(",").map { it.toInt() }.toIntArray()
}

fun parseBigInstructions(instructions: String): LongArray {
    return instructions.split(",").map { it.toLong() }.toLongArray()
}
fun IntArray.toMutableMap(): MutableMap<Long, Long> = this.withIndex().associate { it.index.toLong() to it.value.toLong() }.toMutableMap()
fun LongArray.toMutableMap(): MutableMap<Long, Long> = this.withIndex().associate { it.index.toLong() to it.value }.toMutableMap()