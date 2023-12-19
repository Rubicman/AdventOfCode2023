package day19

import lines
import java.math.BigDecimal

fun main() {
  val rulesWithLabels = lines()
    .takeWhile { it.isNotEmpty() }
    .map { it.dropLast(1).split("{") }
    .map { (name, rules) -> name to rules.split(",").map { SplitRule(it) } }
    .toMap()

  val queue = ArrayDeque<Pair<String, PartGroups>>()
  queue.add("in" to PartGroups(1..4000, 1..4000, 1..4000, 1..4000))

  sequence {
    while (queue.isNotEmpty()) {
      val (label, partGroup) = queue.removeFirst()
      if (partGroup.isEmpty()) continue
      if (label == "A") {
        yield(partGroup)
        continue
      }
      if (label == "R") continue
      val rules = rulesWithLabels.getValue(label)

      rules.fold(partGroup) { group, rule ->
        if (rule is FinalSplitRule) {
          queue.add(rule.nextLabel to group)
          return@fold group
        }
        if (rule is PredicateSplitRule) {
          if (rule.compare == '>') {
            val (left, right) = when (rule.part) {
              'x' -> group.copy(x = group.x.first..rule.value) to group.copy(x = rule.value + 1..group.x.last)
              'm' -> group.copy(m = group.m.first..rule.value) to group.copy(m = rule.value + 1..group.m.last)
              'a' -> group.copy(a = group.a.first..rule.value) to group.copy(a = rule.value + 1..group.a.last)
              's' -> group.copy(s = group.s.first..rule.value) to group.copy(s = rule.value + 1..group.s.last)
              else -> throw IllegalArgumentException()
            }
            queue.add(rule.nextLabel to right)
            return@fold left
          } else {
            val (left, right) = when (rule.part) {
              'x' -> group.copy(x = group.x.first..<rule.value) to group.copy(x = rule.value..group.x.last)
              'm' -> group.copy(m = group.m.first..<rule.value) to group.copy(m = rule.value..group.m.last)
              'a' -> group.copy(a = group.a.first..<rule.value) to group.copy(a = rule.value..group.a.last)
              's' -> group.copy(s = group.s.first..<rule.value) to group.copy(s = rule.value..group.s.last)
              else -> throw IllegalArgumentException()
            }
            queue.add(rule.nextLabel to left)
            return@fold right
          }
        }
        return@fold group
      }
    }
  }.sumOf { it.count }
    .let { println(it) }

}

data class PartGroups(
  val x: IntRange,
  val m: IntRange,
  val a: IntRange,
  val s: IntRange,
) {
  val count
    get() = BigDecimal(x.last - x.first + 1) *
      BigDecimal(m.last - m.first + 1) *
      BigDecimal(a.last - a.first + 1) *
      BigDecimal(s.last - s.first + 1)

  fun isEmpty() = x.isEmpty() || m.isEmpty() || a.isEmpty() || s.isEmpty()
}

sealed interface SplitRule {
  companion object {
    operator fun invoke(string: String): SplitRule {
      if (!string.contains(":")) return FinalSplitRule(string)
      val (value, label) = string.drop(2).split(":")
      return PredicateSplitRule(string[0], string[1], value.toInt(), label)
    }
  }
}

class PredicateSplitRule(
  val part: Char,
  val compare: Char,
  val value: Int,
  val nextLabel: String
) : SplitRule

class FinalSplitRule(
  val nextLabel: String
) : SplitRule
