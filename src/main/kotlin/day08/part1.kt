package day08

import lines

fun main() {
  val instruction = lines().first()
  lines().first()

  val steps = sequence {
    while (true) {
      instruction.forEach { yield(it) }
    }
  }

  val graph = lines()
    .map { line ->
      Vertex(
        line.substring(0, 3),
        line.substring(7, 10),
        line.substring(12, 15)
      )
    }
    .associateBy { it.root }


  var vertex = "AAA"
  var count = 0
  for (step in steps) {
    if (vertex == "ZZZ") {
      println(count)
      break
    }
    count++
    vertex = graph.getValue(vertex).run { if (step == 'L') left else right }
  }
}

class Vertex(
  val root: String,
  val left: String,
  val right: String,
)