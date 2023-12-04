package day03

import lines
import java.lang.IndexOutOfBoundsException

fun main() {
  val engine = lines().toList()
  val gears = mutableMapOf<Pair<Int, Int>, Long>()

  var globalSum = 0L
  for ((i, line) in engine.withIndex()) {
    var currentSum = 0L
    val nearGears = mutableSetOf<Pair<Int, Int>>()
    for ((j, char) in line.withIndex()) {
      if (char.isDigit()) {
        currentSum = currentSum * 10 + char.code - '0'.code
        engine.nearGear(i, j)?.let(nearGears::add)
      } else {
        nearGears.forEach { gear ->
          val value = gears[gear]
          when {
            value == null -> gears[gear] = currentSum
            value <= 0 -> {
              globalSum += value
              gears[gear] = 0
            }
            else -> {
              globalSum += value * currentSum
              gears[gear] = -value * currentSum
            }
          }
        }
        currentSum = 0
        nearGears.clear()
      }
    }
    nearGears.forEach { gear ->
      val value = gears[gear]
      when {
        value == null -> gears[gear] = currentSum
        value <= 0 -> {
          globalSum += value
          gears[gear] = 0
        }
        else -> {
          globalSum += value * currentSum
          gears[gear] = -value * currentSum
        }
      }
    }
  }
  println(globalSum)
}

fun List<String>.nearGear(i: Int, j: Int): Pair<Int, Int>? {
  for (y in i - 1..i + 1)
    for (x in j - 1..j + 1)
      try {
        if (get(y)[x] == '*') return y to x
      } catch (_: IndexOutOfBoundsException) {}
  return null
}