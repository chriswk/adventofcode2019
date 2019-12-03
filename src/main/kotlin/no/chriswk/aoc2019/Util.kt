package no.chriswk.aoc2019

import java.io.InputStream
import java.nio.charset.Charset

class Util {}
val utf8 = Charset.forName("UTF-8")
fun String.toInputStream() = Util::class.java.classLoader.getResourceAsStream(this)
fun InputStream.lines() = this.readBytes().toString(utf8).lines()