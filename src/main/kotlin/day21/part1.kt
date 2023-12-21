package day21

import lines

const val MAX_STEPS = 64

fun main() {
  val lines = lines().toList()
  val steps = mutableMapOf<Pair<Int, Int>, Int>()

  val queue = ArrayDeque<Triple<Int, Int, Int>>()

  for (i in lines.indices) {
    for (j in lines[0].indices) {
      if (lines[i][j] == 'S') queue.add(Triple(i, j, 0))
    }
  }

  while(queue.isNotEmpty()) {
    val (i, j, dist) = queue.removeFirst()
    if (i to j in steps) continue
    if (i !in lines.indices) continue
    if (j !in lines[0].indices) continue
    if (lines[i][j] == '#') continue

    steps[i to j] = dist
    queue.add(Triple(i + 1, j, dist + 1))
    queue.add(Triple(i - 1, j, dist + 1))
    queue.add(Triple(i, j + 1, dist + 1))
    queue.add(Triple(i, j - 1, dist + 1))
  }

  println(steps.values.count {it % 2 == MAX_STEPS % 2 && it <= MAX_STEPS})
}