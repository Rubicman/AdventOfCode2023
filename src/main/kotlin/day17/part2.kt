package day17

import day17.Direction.*
import lines
import java.util.*
import java.util.Comparator.comparingLong

fun main() {
  val map = lines().map { it.map { it.code - '0'.code } }.toList()

  val states = mutableMapOf<State2, Long>()
  val queue = PriorityQueue<Pair<State2, Long>>(comparingLong { it.second })
  queue.add(State2(0, 0, DOWN, 0) to 0)
  queue.add(State2(0, 0, RIGHT, 0) to 0)

  while (queue.isNotEmpty()) {
    val (state, way) = queue.remove()

    if (state in states) continue
    states[state] = way

    queue.addAll(state.next(map).map { it to way + map[it.i][it.j] })
  }

  states
    .entries
    .filter { (key, _) -> key.i == map.size - 1 && key.j == map[0].size - 1 && key.stepsInOneDirection >= 4 }
    .minOf { (_, value) -> value }
    .let(::println)
}

private data class State2(
  val i: Int,
  val j: Int,
  val direction: Direction,
  val stepsInOneDirection: Int
) {

  init {
    if (stepsInOneDirection > 10) throw IllegalArgumentException()
  }

  fun next(map: List<Collection<*>>) = when (direction) {
    UP -> listOf(up(), left(), right())
    DOWN -> listOf(down(), left(), right())
    LEFT -> listOf(up(), down(), left())
    RIGHT -> listOf(up(), down(), right())
  }
    .filterNotNull()
    .filter { it.isValid(map) }

  private fun up() = next(i - 1, j, UP)
  private fun down() = next(i + 1, j, DOWN)
  private fun left() = next(i, j - 1, LEFT)
  private fun right() = next(i, j + 1, RIGHT)

  private fun next(newI: Int, newJ: Int, newDirection: Direction): State2? {
    if (direction == newDirection) {
      if (stepsInOneDirection == 10) return null
      return State2(newI, newJ, newDirection, stepsInOneDirection + 1)
    } else {
      if (stepsInOneDirection < 4) return null
      return State2(newI, newJ, newDirection, 1)
    }
  }

  private fun isValid(map: List<Collection<*>>) =
    i in map.indices && j in map[0].indices
}
