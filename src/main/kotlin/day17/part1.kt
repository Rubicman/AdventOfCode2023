package day17

import day17.Direction.*
import lines
import java.util.*
import java.util.Comparator.comparingLong

fun main() {
  val map = lines().map { it.map { it.code - '0'.code } }.toList()

  val states = mutableMapOf<State, Long>()
  val queue = PriorityQueue<Pair<State, Long>>(comparingLong { it.second })
  queue.add(State(0, 0, DOWN, 0) to 0)

  while (queue.isNotEmpty()) {
    val (state, way) = queue.remove()

    if (state in states) continue
    states[state] = way

    queue.addAll(state.next(map).map { it to way + map[it.i][it.j] })
  }

  states
    .entries
    .filter { (key, _) -> key.i == map.size - 1 && key.j == map[0].size - 1 }
    .minOf { (_, value) -> value }
    .let(::println)
}

data class State(
  val i: Int,
  val j: Int,
  val direction: Direction,
  val stepsInOneDirection: Int
) {

  init {
    if (stepsInOneDirection > 3) throw IllegalArgumentException()
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

  private fun next(newI: Int, newJ: Int, newDirection: Direction): State? {
    return when {
      direction != newDirection -> State(newI, newJ, newDirection, 1)
      stepsInOneDirection == 3 -> null
      else -> State(newI, newJ, newDirection, stepsInOneDirection + 1)
    }
  }

  private fun isValid(map: List<Collection<*>>) =
    i in map.indices && j in map[0].indices
}

enum class Direction {
  UP, DOWN, LEFT, RIGHT
}
