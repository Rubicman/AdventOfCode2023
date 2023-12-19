package day19

import lines

fun main() {
  val rules = lines()
    .takeWhile { it.isNotEmpty() }
    .map { it.dropLast(1).split("{") }
    .map { (name, rules) -> name to rules.split(",").map { Rule(it) } }
    .toMap()

  lines()
    .map { it.drop(1).dropLast(1).split(",") }
    .map { bag -> bag.map { it.drop(2).toInt() } }
    .map { (x, m, a, s) -> PartBag(x, m, a, s) }
    .filter { bag ->
      var label = "in"
      while (true) {
        label = rules.getValue(label).firstNotNullOf { it(bag) }
        if (label == "A") return@filter true
        if (label == "R") return@filter false
      }
      false
    }
    .sumOf { it.sum }
    .let { println(it) }
}

data class PartBag(
  val x: Int,
  val m: Int,
  val a: Int,
  val s: Int,
) {
  val sum
    get() = x + m + a + s
}

sealed interface Rule {
  operator fun invoke(partBag: PartBag): String?

  companion object {
    operator fun invoke(string: String): Rule {
      if (!string.contains(":")) return FinalRule(string)
      val extractor = when (string.first()) {
        'x' -> PartBag::x
        'm' -> PartBag::m
        'a' -> PartBag::a
        's' -> PartBag::s
        else -> throw IllegalArgumentException()
      }
      val (value, label) = string.drop(2).split(":")
      val predicate: (Int) -> Boolean = if (string[1] == '>') { x -> x > value.toInt() } else { x -> x < value.toInt() }
      return PredicateRule(extractor, predicate, label)
    }
  }
}

class PredicateRule(
  private val extractor: (PartBag) -> Int,
  private val predicate: (Int) -> Boolean,
  private val nextLabel: String
) : Rule {
  override fun invoke(partBag: PartBag) = if (predicate(extractor(partBag))) nextLabel else null
}

class FinalRule(
  private val nextLabel: String
) : Rule {
  override fun invoke(partBag: PartBag): String = nextLabel
}
