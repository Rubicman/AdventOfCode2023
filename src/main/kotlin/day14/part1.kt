package day14

import lines

fun main() {
  val map = lines().toList()
  var result = 0L
  val dropPlace = Array(map[0].length) { -1 }

  for (i in map.indices) {
    for (j in map[0].indices) {
      when (map[i][j]) {
        '#' -> dropPlace[j] = i
        'O' -> {
          result += map.size - ++dropPlace[j]
        }
      }
    }
  }

  println(result)
}
