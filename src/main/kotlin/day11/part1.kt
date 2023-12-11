package day11

import lines
import kotlin.math.abs

fun main() {
  val galaxies = buildList {
    lines().forEachIndexed { i, sting ->
      sting.forEachIndexed { j, char ->
        if (char == '#') add(i to j)
      }
    }
  }
  val lines = galaxies.map { it.first }.toSet()
  val columns = galaxies.map { it.second }.toSet()

  galaxies.sumOf { g1 ->
    galaxies.sumOf { g2 ->
      abs(g1.first - g2.first) * 2 + abs(g1.second - g2.second) * 2 -
        (g1.first increasingRange g2.first).count { it in lines } -
        (g1.second increasingRange g2.second).count { it in columns }
    }
  }.let { println(it / 2) }
}

infix fun Int.increasingRange(other: Int) =
  if (this < other) this..<other else other..<this