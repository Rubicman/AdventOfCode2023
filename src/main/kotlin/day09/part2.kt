package day09

import lines

fun main() {
  lines()
    .map { it.split(" ").map(String::toLong) }
    .sumOf { it.history() }
    .let(::println)
}

fun List<Long>.history(): Long {
  if (all { it == 0L }) return 0L
  return first() - this.zipWithNext().map { (a, b) -> b - a }.history()
}