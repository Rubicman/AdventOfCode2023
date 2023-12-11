package day10

import day10.State.*
import lines


fun main() {
  val map = lines().toList()
  val cells = Array(map.size) { Array(map[0].length) { Cell() } }

  fun dfs(current: Pair<Int, Int>, parent: Pair<Int, Int>): Boolean {
    if (current !in cells) return false
    if (cells[current.first][current.second].state != EMPTY) return false
    cells[current.first][current.second].distance = cells[parent.first][parent.second].distance + 1
    cells[current.first][current.second].state = OUTSIDE

    when (map[current.first][current.second]) {
      '|' -> listOf(current.first - 1 to current.second, current.first + 1 to current.second)
      '-' -> listOf(current.first to current.second - 1, current.first to current.second + 1)
      'L' -> listOf(current.first - 1 to current.second, current.first to current.second + 1)
      'J' -> listOf(current.first - 1 to current.second, current.first to current.second - 1)
      '7' -> listOf(current.first to current.second - 1, current.first + 1 to current.second)
      'F' -> listOf(current.first to current.second + 1, current.first + 1 to current.second)
      '.' -> emptyList()
      'S' -> buildList {
        (current.first - 1 to current.second).takeIf { it in cells && map[it.first][it.second] in setOf('|', '7', 'F') }
          ?.let { add(it) }
        (current.first + 1 to current.second).takeIf { it in cells && map[it.first][it.second] in setOf('|', 'L', 'J') }
          ?.let { add(it) }
        (current.first to current.second - 1).takeIf { it in cells && map[it.first][it.second] in setOf('-', 'L', 'F') }
          ?.let { add(it) }
        (current.first to current.second + 1).takeIf { it in cells && map[it.first][it.second] in setOf('-', '7', 'J') }
          ?.let { add(it) }
      }

      else -> throw IllegalArgumentException()
    }
      .filter { it != parent }
      .forEach { to ->
        if (map[to.first][to.second] == 'S' || dfs(to, current)) {
          cells[current.first][current.second].state = LOOP
          cells[current.first][current.second].distance = minOf(
            cells[current.first][current.second].distance,
            cells[to.first][to.second].distance + 1
          )
          return true
        }
      }

    return false
  }

  val group = ThreadGroup("threadGroup")
  for (i in map.indices) {
    for (j in map[0].indices) {
      if (map[i][j] == 'S') {
        cells[i][j].distance = -1
        Thread(group, { dfs(i to j, i to j) }, "Calculation Thread", 200000000)
          .apply {
            start()
            join()
          }
      }
    }
  }

  cells.flatten().filter { it.state == LOOP }.maxOf { it.distance }.let(::println)
}

enum class State {
  EMPTY, LOOP, OUTSIDE
}

class Cell {
  var distance: Int = 0
  var state: State = EMPTY
}

operator fun Array<Array<Cell>>.contains(point: Pair<Int, Int>) =
  point.first in indices && point.second in first().indices