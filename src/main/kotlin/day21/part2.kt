package day21

import lines
import java.math.BigDecimal

const val MAX_INFINITE_STEPS = 26501365
const val STABlE_SERIES = 5

fun main() {
  val lines = lines().toList()
  val steps = mutableMapOf<Pair<Int, Int>, Int>()
  val series = Array(lines.size) { ArrayDeque<Int>(STABlE_SERIES).apply { repeat(STABlE_SERIES) { add(0) } } }
  val prevValues = Array(lines.size) { 0 }
  val prevDist = Array(lines.size) { 0 }
  var lastWatchedLayer = -1

  val queue = ArrayDeque<Triple<Int, Int, Int>>()

  for (i in lines.indices) {
    for (j in lines[0].indices) {
      if (lines[i][j] == 'S') queue.add(Triple(i, j, 0))
    }
  }

  var count = 0

  while (true) {
    val (i, j, dist) = queue.removeFirst()

    if (dist - 1 > lastWatchedLayer) {
      lastWatchedLayer = dist - 1
      val k = (dist - 1) % lines.size
      if ((MAX_INFINITE_STEPS - dist) % 2 == 1) {
        series[k].apply {
          removeFirst()
          addLast(count - prevValues[k])
        }
        prevValues[k] = count
        prevDist[k] = lastWatchedLayer
        if (series.all { it.distinct().size == 1 }) break
      }
      count = 0

    }

    if (dist > MAX_INFINITE_STEPS) break
    if (i to j in steps) continue
    if (lines.getInfinite(i, j) == '#') continue
    count++

    steps[i to j] = dist
    queue.add(Triple(i + 1, j, dist + 1))
    queue.add(Triple(i - 1, j, dist + 1))
    queue.add(Triple(i, j + 1, dist + 1))
    queue.add(Triple(i, j - 1, dist + 1))
  }

  var answer = BigDecimal(steps.values.count { (MAX_INFINITE_STEPS - it) % 2 == 0 })
  for (i in lines.indices) {
    val b = series[i].last()
    val a = prevValues[i] + b
    val n = (MAX_INFINITE_STEPS - prevDist[i] + 1) / (2 * lines.size)
    answer += progressionSum(a, b, n)
  }

  println(answer)
}

fun inBorder(a: Int, n: Int): Int {
  if (a % n == 0) return 0
  if (a < 0) return n - inBorder(-a, n)
  return a % n
}

fun List<String>.getInfinite(i: Int, j: Int) = get(inBorder(i, size))[inBorder(j, get(0).length)]

fun progressionSum(a: Int, b: Int, n: Int) =
  (BigDecimal(a) * BigDecimal(2) + BigDecimal(b) * BigDecimal(n - 1)) * BigDecimal(n) / BigDecimal(2)