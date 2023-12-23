package day23

import lines

fun main() {
  val map = lines().toList()
  val start = Point(0, map.first().indexOfFirst { it == '.' })
  val finish = Point(map.lastIndex, map.last().indexOfFirst { it == '.' })

  val visited = mutableSetOf<Point>()
  fun dfs(point: Point): Int? {
    if (point.i !in map.indices) return null
    if (point.j !in map.first().indices) return null
    if (point in visited) return null
    if (point == finish) return 0

    visited.add(point)
    val next = buildSet {
      if (map[point.i][point.j] in setOf('.', '^')) add(point.up())
      if (map[point.i][point.j] in setOf('.', 'v')) add(point.down())
      if (map[point.i][point.j] in setOf('.', '<')) add(point.left())
      if (map[point.i][point.j] in setOf('.', '>')) add(point.right())
    }

    val result = next.mapNotNull { dfs(it) }.maxOfOrNull { it + 1 }
    visited.remove(point)
    return result
  }

  println(dfs(start))
}

data class Point(
  val i: Int,
  val j: Int,
) {
  fun up() = Point(i - 1, j)
  fun down() = Point(i + 1, j)
  fun left() = Point(i, j - 1)
  fun right() = Point(i, j + 1)
}
