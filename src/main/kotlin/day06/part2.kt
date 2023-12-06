package day06

import lines

fun main() {
  val (time, distance) = lines()
    .map { it.split(Regex(":\\s*"))[1] }
    .map { it.replace(" ", "").toLong() }
    .toList()

  var l = 0L
  var r = time / 2

  while (r - l > 1) {
    val m = (r + l) / 2
    if (m * (time - m) > distance)
      r = m
    else
      l = m
  }

  println(time - l * 2 - 1)
}