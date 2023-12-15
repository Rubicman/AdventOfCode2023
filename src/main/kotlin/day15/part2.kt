package day15

import lines

fun main() {
  val boxes = Array(256) { mutableMapOf<String, Int>() }

  lines().first()
    .split(",")
    .forEach { command ->
      val (label, value) = command.split(Regex("[\\-=]"))
      if (value.isEmpty()) {
        boxes[hash(label)].remove(label)
      } else {
        boxes[hash(label)][label] = value.toInt()
      }
    }

  boxes.mapIndexed { i, box ->
    box.values.mapIndexed { j, value -> (j + 1L) * value }.sum() * (i + 1)
  }
    .sum()
    .let(::println)
}
