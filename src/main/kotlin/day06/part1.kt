package day06

import lines

fun main() {
  val (times, distances) = lines()
    .map { it.split(Regex(":\\s*"))[1] }
    .map { it.split(Regex("\\s+")).map(String::toLong) }
    .toList()

  times.zip(distances)
    .map { (time, distance) -> (0..time).count { it * (time - it) > distance } }
    .reduce { acc, next -> acc * next }
    .let(::println)
}