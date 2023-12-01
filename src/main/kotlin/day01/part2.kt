package day01

import lines

val digits = mapOf(
  "one" to 1,
  "two" to 2,
  "three" to 3,
  "four" to 4,
  "five" to 5,
  "six" to 6,
  "seven" to 7,
  "eight" to 8,
  "nine" to 9,
)

fun firstDigit(line: String): Int {
  val wordIndex = digits
    .map { line.indexOf(it.key) to it.value }
    .filter { it.first != -1 }
    .minByOrNull { it.first }
  val digitIndex = line
    .indexOfFirst { it.isDigit() }
    .takeIf { it != -1 }
    ?.let { it to (line[it].code - '0'.code) }

  if (wordIndex == null) return digitIndex!!.second
  if (digitIndex == null) return wordIndex.second

  return if (wordIndex.first < digitIndex.first)
    wordIndex.second
  else
    digitIndex.second
}

fun lastDigit(line: String): Int {
  val reversedLine = line.reversed()
  val wordIndex = digits
    .mapKeys { it.key.reversed() }
    .map { line.length - 1 - reversedLine.indexOf(it.key) to it.value }
    .filter { it.first != line.length }
    .maxByOrNull { it.first }
  val digitIndex = line
    .indexOfLast { it.isDigit() }
    .takeIf { it != -1 }
    ?.let { it to (line[it].code - '0'.code) }

  if (wordIndex == null) return digitIndex!!.second
  if (digitIndex == null) return wordIndex.second

  return if (wordIndex.first > digitIndex.first)
    wordIndex.second
  else
    digitIndex.second
}

fun main() {
  println(lines().sumOf { line ->
    firstDigit(line) * 10 + lastDigit(line)
  })
}