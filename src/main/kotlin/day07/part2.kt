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
  val map = hand
    .groupBy { it }
    .mapValues { (_, list) -> list.size }
  val jokers = map['J'] ?: 0
  val counts = map.filterKeys { it != 'J' }.values

  if (counts == listOf(2, 2)) println("!!!")

  return when {
    counts.any { it + jokers == 5 } || jokers == 5 -> 7L
    counts.any { it + jokers == 4 } -> 6L
    counts.any { it == 3 } && counts.any { it == 2 } -> 5L
    counts.count { it == 2 } == 2 && jokers == 1 -> 5L
    counts.any { it + jokers == 3 } -> 4L
    counts.count { it == 2 } == 2 || (counts.any { it == 2 } && jokers > 0) -> 3L
    counts.any { it + jokers == 2 } -> 2L
    else -> 1L
  }
}

private fun cardPower(card: Char): Long = when (card) {
  'A' -> 13L
  'K' -> 12L
  'Q' -> 11L
  'T' -> 10L
  '9' -> 9L
  '8' -> 8L
  '7' -> 7L
  '6' -> 6L
  '5' -> 5L
  '4' -> 4L
  '3' -> 3L
  '2' -> 2L
  'J' -> 1L
  else -> throw IllegalArgumentException()
}

private fun orderPower(hand: String): Long =
  hand
    .map(::cardPower)
    .fold(0L) { acc, next -> acc * 14L + next }