package day18

import lines
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import kotlin.math.abs

fun main() {
  val points = buildList {
    lines()
      .map { it.split(" ")[2].drop(2).dropLast(1) }
      .map { it.last() to it.dropLast(1).toLong(16).toBigDecimal() }
      .fold(ZERO to ZERO) { (x, y), (direction, distance) ->
        add(x to y)
        when (direction) {
          '3' -> x to y + distance
          '1' -> x to y - distance
          '2' -> x - distance to y
          '0' -> x + distance to y
          else -> throw IllegalArgumentException()
        }
      }
      .let { add(it) }
    add(ZERO to ZERO)
  }
    .zipWithNext()
  val s = points
    .sumOf { (p1, p2) -> (p2.first - p1.first) * (p2.second + p1.second) }.abs() / BigDecimal(2)
  val p = points
    .sumOf { (p1, p2) -> (p2.first - p1.first).abs() + (p2.second - p1.second).abs() }

  println(s + p / BigDecimal(2) + ONE)
}
