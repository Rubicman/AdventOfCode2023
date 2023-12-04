package day04

import lines

fun main() {
  val lines = lines().toList()
  val counts = LongArray(lines.size) { 1L }
  lines
    .map { it.split(":")[1] }
    .map { line ->
      line.split(" | ").map { set ->
        set
          .split(" ")
          .filter(String::isNotEmpty)
          .toSet()
      }
    }
    .map { (first, second) ->
      second.count { it in first }
    }
    .forEachIndexed { index, number ->
      repeat(number) { i ->
        counts[index + i + 1] += counts[index]
      }
    }

  println(counts.sum())
}