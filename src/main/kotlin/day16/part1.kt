package day16

import day16.Direction.*
import lines

fun main() {
  val map = lines().toList()
  val states = mutableSetOf<State>()
  val queue = ArrayDeque<State>()
  queue.add(State(0, 0, RIGHT))

  while (queue.isNotEmpty()) {
    val state = queue.removeFirst()
    if (state in states) continue
    states.add(state)
    queue.addAll(state.next(map[state.i][state.j]).filter { it.isValid(map) })
  }

  states
    .map { it.i to it.j }
    .distinct()
    .count()
    .let(::println)
}

data class State(
  val i: Int,
  val j: Int,
  val direction: Direction,
) {
  fun isValid(map: List<String>) =
    i in map.indices && j in map[0].indices

  fun next(element: Char): List<State> = when (direction) {
    UP -> nextUp(element)
    DOWN -> nextDown(element)
    LEFT -> nextLeft(element)
    RIGHT -> nextRight(element)
  }

  private fun nextUp(element: Char): List<State> = when (element) {
    '.' -> listOf(up())
    '-' -> listOf(left(), right())
    '|' -> listOf(up())
    '/' -> listOf(right())
    '\\' -> listOf(left())
    else -> throw IllegalArgumentException()
  }

  private fun nextDown(element: Char): List<State> = when (element) {
    '.' -> listOf(down())
    '-' -> listOf(left(), right())
    '|' -> listOf(down())
    '/' -> listOf(left())
    '\\' -> listOf(right())
    else -> throw IllegalArgumentException()
  }

  private fun nextLeft(element: Char): List<State> = when (element) {
    '.' -> listOf(left())
    '-' -> listOf(left())
    '|' -> listOf(up(), down())
    '/' -> listOf(down())
    '\\' -> listOf(up())
    else -> throw IllegalArgumentException()
  }

  private fun nextRight(element: Char): List<State> = when (element) {
    '.' -> listOf(right())
    '-' -> listOf(right())
    '|' -> listOf(up(), down())
    '/' -> listOf(up())
    '\\' -> listOf(down())
    else -> throw IllegalArgumentException()
  }

  private fun up() = State(i - 1, j, UP)
  private fun down() = State(i + 1, j, DOWN)
  private fun left() = State(i, j - 1, LEFT)
  private fun right() = State(i, j + 1, RIGHT)
}

enum class Direction {
  UP, DOWN, LEFT, RIGHT
}