package day12

import lines
import java.util.Queue

fun main() {
  lines()
    .map { it.split(" ") }
    .map { (springs, numbers) -> springs to numbers.split(",").map(String::toInt) }
    .sumOf { (springs, numbers) -> process(springs, numbers) }
    .let(::println)
}

private fun process(springs: String, numbers: List<Int>): Long {
  val queue = ArrayDeque<State>()
  queue.add(State(0, 0, 0))
  var sum = 0L
  while (queue.isNotEmpty()) {
    val state = queue.removeFirst()
    if (state.springIndex == springs.length) {
      if (state.streak == 0) {
        if (state.numbersIndex == numbers.size) sum++
      } else {
        if (
          state.numbersIndex == numbers.size - 1 &&
          state.streak == numbers.last()
        ) sum++
      }
      continue
    }

    if (springs[state.springIndex] == '.' || springs[state.springIndex] == '?') {
      when {
        state.streak == 0 -> queue.add(state.empty())
        state.numbersIndex < numbers.size && state.streak == numbers[state.numbersIndex] -> queue.add(state.finishGroup())
      }
    }
    if (springs[state.springIndex] == '#' || springs[state.springIndex] == '?') {
      queue.addLast(state.onGroup())
    }
  }
  return sum
}

data class State(
  val springIndex: Int,
  val streak: Int,
  val numbersIndex: Int,
) {
  fun finishGroup() = State(springIndex + 1, 0, numbersIndex + 1)
  fun empty() = State(springIndex + 1, 0, numbersIndex)
  fun onGroup() = State(springIndex + 1, streak + 1, numbersIndex)
}
