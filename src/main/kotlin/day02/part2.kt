package day02

import lines

fun main() {
  println(lines()
    .map { it.split(": ")[1] }
    .sumOf { cubes ->
      cubes
        .split("; ")
        .flatMap { it.split(", ") }
        .map { it.split(" ") }
        .groupBy { it[1] }
        .mapValues { (_, value) ->
          value.maxOf { (count, _) ->
            count.toInt()
          }
        }
        .values
        .reduce { acc, next -> acc * next }
    }
  )
}