package day15

import lines

fun main() {
  lines().first()
    .split(",")
    .sumOf(::hash)
    .let(::println)
}

fun hash(string: String): Int {
  var result = 0
  for (c in string) {
    result = (result + c.code) * 17 % 256
  }
  return result
}
