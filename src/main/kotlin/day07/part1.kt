package day07

import lines

fun main() {
  Comparator.comparing(::combinationPower).thenComparing(::orderPower)
  lines()
    .map { it.split(" ") }
    .map { (hand, value) -> hand to value.toLong() }
    .sortedWith(Comparator
      .comparing<Pair<String, Any>, Long> { (hand, _) -> combinationPower(hand) }
      .thenComparing { (hand, _) -> orderPower(hand) }
    )
    .mapIndexed { index, (_, value) -> (index + 1) * value }
    .sum()
    .let(::println)
}

private fun combinationPower(hand: String): Long {
  val counts = hand
    .groupBy { it }
    .map { (_, list) -> list.size }

  return when {
    counts.any { it == 5 } -> 7L
    counts.any { it == 4 } -> 6L
    counts.any { it == 3 } && counts.any { it == 2 } -> 5L
    counts.any { it == 3 } -> 4L
    counts.count { it == 2 } == 2 -> 3L
    counts.any { it == 2 } -> 2L
    else -> 1L
  }
}

private fun cardPower(card: Char): Long = when (card) {
  'A' -> 13L
  'K' -> 12L
  'Q' -> 11L
  'J' -> 10L
  'T' -> 9L
  '9' -> 8L
  '8' -> 7L
  '7' -> 6L
  '6' -> 5L
  '5' -> 4L
  '4' -> 3L
  '3' -> 2L
  '2' -> 1L
  else -> throw IllegalArgumentException()
}

private fun orderPower(hand: String): Long =
  hand
    .map(::cardPower)
    .fold(0L) { acc, next -> acc * 14L + next }