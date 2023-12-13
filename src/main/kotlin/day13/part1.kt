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

private fun reflectionY(map: List<String>): Int {
  mainLoop@ for (i in 1..<map.size) {
    for (j in 0..<min(i, map.size - i)) {
      if (map[i + j] != map[i - 1 - j]) continue@mainLoop
    }
    return i
  }
  return 0
}

private fun reflectionX(map: List<String>): Int {
  mainLoop@ for (j in 1..<map[0].length) {
    for (i in 0..<min(j, map[0].length - j)) {
      for (line in map) {
        if (line[j + i] != line[j - 1 - i]) continue@mainLoop
      }
    }
    return j
  }
  return 0
}
