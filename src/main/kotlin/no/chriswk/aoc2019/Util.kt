package no.chriswk.aoc2019

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

fun report(f: () -> Int) {
    var ans: Int? = null
    val timeTaken = measureTimeMillis { ans = f() }
    println("Answer [$ans] - took $timeTaken ms")
}