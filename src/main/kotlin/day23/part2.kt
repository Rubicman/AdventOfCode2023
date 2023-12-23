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
    if (map[point.i][point.j] == '#') return null
    if (point == finish) return 0

    visited.add(point)

    val result = listOf(point.up(), point.down(), point.left(), point.right())
      .mapNotNull { dfs(it) }
      .maxOfOrNull { it + 1 }
    visited.remove(point)
    return result
  }

  var answer = 0
  val group = ThreadGroup("threadGroup")
  Thread(group, { answer = dfs(start)!! }, "Calculate thread", 2_000_000_000)
    .apply {
      start()
      join()
    }
  println(answer)
}

