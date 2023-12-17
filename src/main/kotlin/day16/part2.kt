package day16

import day16.Direction.*
import lines

fun main() {
  val map = lines().toList()

  sequence {
    for (i in map.indices) {
      yield(State(i, 0, RIGHT))
      yield(State(i, map[0].lastIndex, LEFT))
    }
    for (j in map[0].indices) {
      yield(State(0, j, DOWN))
      yield(State(map.lastIndex, j, UP))
    }
  }
    .maxOf { calculate(map, it) }
    .let(::println)
}

fun calculate(map: List<String>, start: State): Int {
  val states = mutableSetOf<State>()
  val queue = ArrayDeque<State>()
  queue.add(start)

  while (queue.isNotEmpty()) {
    val state = queue.removeFirst()
    if (state in states) continue
    states.add(state)
    queue.addAll(state.next(map[state.i][state.j]).filter { it.isValid(map) })
  }

  return states
    .map { it.i to it.j }
    .distinct()
    .count()
}

