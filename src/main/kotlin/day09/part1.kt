package day09

import lines

fun main() {
  lines()
    .map { it.split(" ").map(String::toLong) }
    .sumOf { it.predict() }
    .let(::println)
}

fun List<Long>.predict(): Long {
  if (all { it == 0L }) return 0L
  return last() + this.zipWithNext().map { (a, b) -> b - a }.predict()
}