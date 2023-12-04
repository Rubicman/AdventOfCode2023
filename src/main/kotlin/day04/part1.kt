package day04

import lines

fun main() {
  lines()
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
    .sumOf { 1L shl (it - 1) }
    .let(::println)
}