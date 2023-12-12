package day12

import lines

fun main() {
  var done = 0
  lines()
    .map { it.split(" ") }
    .map { (springs, numbers) -> springs to numbers.split(",").map(String::toInt) }
    .map { (springs, numbers) -> List(5) { springs }.joinToString("?") to List(5) { numbers }.flatten() }
    .sumOf { (springs, numbers) -> process(springs, numbers) }
    .let(::println)
}

private fun process(springs: String, numbers: List<Int>): Long {
  val memorizedAnswers = mutableMapOf<State, Long>()

  fun dfs(state: State): Long {
    if (state in memorizedAnswers) return memorizedAnswers.getValue(state)

    var sum = 0L

    if (state.numbersIndex < numbers.size) {
      val subNumbers = numbers.subList(state.numbersIndex, numbers.size)
      if (
        subNumbers.sum() + subNumbers.size - 1 - state.streak >
        springs.length - state.springIndex
      ) {
        memorizedAnswers[state] = 0L
        return 0L
      }
    }

    if (state.streak > 0) {
      if (state.numbersIndex == numbers.size || state.streak > numbers[state.numbersIndex]) {
        memorizedAnswers[state] = 0L
        return 0L
      }
    }

    if (state.springIndex == springs.length) {
      if (state.streak == 0) {
        if (state.numbersIndex == numbers.size) sum++
      } else {
        if (
          state.numbersIndex == numbers.size - 1 &&
          state.streak == numbers.last()
        ) sum++
      }
      memorizedAnswers[state] = sum
      return sum
    }

    if (springs[state.springIndex] == '.' || springs[state.springIndex] == '?') {
      when {
        state.streak == 0 -> sum += dfs(state.empty())
        state.numbersIndex < numbers.size && state.streak == numbers[state.numbersIndex] -> sum += dfs(state.finishGroup())
      }
    }
    if (springs[state.springIndex] == '#' || springs[state.springIndex] == '?') {
      sum += dfs(state.onGroup())
    }

    memorizedAnswers[state] = sum
    return sum
  }

  return dfs(State(0, 0, 0))
}