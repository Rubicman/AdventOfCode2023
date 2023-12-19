package day18

import lines
import kotlin.math.abs

fun main() {
  val points = buildList {
    lines()
      .map { it.split(" ") }
      .map { (direction, distance) -> direction to distance.toInt() }
      .fold(0 to 0) { (x, y), (direction, distance) ->
        add(x to y)
        when (direction) {
          "U" -> x to y + distance
          "D" -> x to y - distance
          "L" -> x - distance to y
          "R" -> x + distance to y
          else -> throw IllegalArgumentException()
        }
      }
      .let { add(it) }
    add(0 to 0)
  }
    .zipWithNext()
  val s = points
    .sumOf { (p1, p2) -> (p2.first - p1.first) * (p2.second + p1.second) / 2 }
  val p = points
    .sumOf { (p1, p2) -> abs(p2.first - p1.first) + abs(p2.second - p1.second) }

  println(s + p / 2 + 1)
}
