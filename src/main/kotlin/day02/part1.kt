package day02

import lines

val maxes = mapOf(
  "red" to 12,
  "green" to 13,
  "blue" to 14,
)

fun main() {
  println(lines().sumOf { line ->
    val (game, results) = line.split(": ")

    return@sumOf if (results
      .split("; ")
      .flatMap { it.split(", ") }
      .map { it.split(" ") }
      .all { (count, color) -> maxes.getValue(color) >= count.toInt() }
      ) game.drop(5).toInt() else 0

  })
}