package day14

import lines

const val TOTAL_LOOPS = 1_000_000_000

fun main() {
  val map = lines().map(String::toMutableList).toList()
  var dropPlace: Array<Int>

  val answers = mutableMapOf<String, Int>()
  answers[map.joinToString("$") { it.joinToString() }] = 0

  repeat(TOTAL_LOOPS) { index ->
    dropPlace = Array(map[0].size) { -1 }
    for (i in map.indices) {
      for (j in map[0].indices) {
        when (map[i][j]) {
          '#' -> dropPlace[j] = i
          'O' -> {
            map[i][j] = '.'
            map[++dropPlace[j]][j] = 'O'
          }
        }
      }
    }

    dropPlace = Array(map.size) { -1 }
    for (j in map[0].indices) {
      for (i in map.indices) {
        when (map[i][j]) {
          '#' -> dropPlace[i] = j
          'O' -> {
            map[i][j] = '.'
            map[i][++dropPlace[i]] = 'O'
          }
        }
      }
    }

    dropPlace = Array(map[0].size) { map.size }
    for (i in map.indices.reversed()) {
      for (j in map[0].indices) {
        when (map[i][j]) {
          '#' -> dropPlace[j] = i
          'O' -> {
            map[i][j] = '.'
            map[--dropPlace[j]][j] = 'O'
          }
        }
      }
    }

    dropPlace = Array(map.size) { map[0].size }
    for (j in map[0].indices.reversed()) {
      for (i in map.indices) {
        when (map[i][j]) {
          '#' -> dropPlace[i] = j
          'O' -> {
            map[i][j] = '.'
            map[i][--dropPlace[i]] = 'O'
          }
        }
      }
    }

    val key = map.joinToString("$") { it.joinToString() }
    if (key in answers) {
      val phase = (TOTAL_LOOPS - index - 1) % (index + 1 - answers.getValue(key))

      answers.entries.first { it.value == answers.getValue(key) + phase }.let { println(weight(it.key.split("$"))) }
      return
    }

    answers[key] = index + 1
  }

  answers.entries.sortedBy { it.value }.forEach {
    print(weight(it.key.split("$")))
    print(" ")
    println(it.value)
  }
}

fun weight(map: List<String>): Long {
  var sum = 0L
  for (i in map.indices) {
    for (j in map[0].indices) {
      if (map[i][j] == 'O') sum += map.size - i
    }
  }
  return sum
}