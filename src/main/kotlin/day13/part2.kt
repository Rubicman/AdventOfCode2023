package day13

import lines
import kotlin.math.min

fun main() {
  sequence {
    lines().fold(mutableListOf<String>()) { acc, next ->
      if (next.isEmpty()) {
        yield(acc)
        mutableListOf()
      } else {
        acc.add(next)
        acc
      }
    }.takeIf { it.isNotEmpty() }?.let { yield(it) }
  }
    .sumOf { reflectionX(it) + reflectionY(it) * 100 }
    .let(::println)
}

private fun reflectionY(map: List<String>): Int = sequence {
  mainLoop@ for (i in 1..<map.size) {
    var errors = 0
    for (j in 0..<min(i, map.size - i)) {
      for (k in map[0].indices) {
        if (map[i + j][k] != map[i - 1 - j][k]) errors++
      }
    }
    if (errors == 1) yield(i)
  }
}
  .sum()

private fun reflectionX(map: List<String>): Int = sequence {
  mainLoop@ for (j in 1..<map[0].length) {
    var errors = 0
    for (i in 0..<min(j, map[0].length - j)) {
      for (line in map) {
        if (line[j + i] != line[j - 1 - i]) errors++
      }
    }
    if (errors == 1) yield(j)
  }
}.sum()
