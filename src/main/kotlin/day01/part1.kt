package day01

import lines

fun main() {
  println(lines().sumOf { line ->
    (line.first { it.isDigit() }.code - '0'.code) * 10 +
      (line.last { it.isDigit() }.code - '0'.code)
  })
}