package day03

import lines
import java.lang.IndexOutOfBoundsException

fun main() {
  val engine = lines().toList()

  var globalSum = 0L
  for ((i, line) in engine.withIndex()) {
    var currentSum = 0L
    var hasNearSymbol = false
    for ((j, char) in line.withIndex()) {
      if (char.isDigit()) {
        currentSum = currentSum * 10 + char.code - '0'.code
        hasNearSymbol = hasNearSymbol || engine.nearSymbol(i, j)
      } else {
        if (hasNearSymbol)
          globalSum += currentSum
        currentSum = 0
        hasNearSymbol = false
      }
    }
    if (hasNearSymbol) globalSum += currentSum
  }
  println(globalSum)
}

fun List<String>.nearSymbol(i: Int, j: Int): Boolean {
  for (y in i - 1..i + 1)
    for (x in j - 1..j + 1)
      try {
        if (!get(y)[x].isDigit() && get(y)[x] != '.') return true
      } catch (_: IndexOutOfBoundsException) {}
  return false
}