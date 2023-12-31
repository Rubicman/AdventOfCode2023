package day08

import lcm
import lines

fun main() {
  val instruction = lines().first()
  lines().first()

  val steps = {
    sequence {
      while (true) {
        instruction.forEach { yield(it) }
      }
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


  graph.keys
    .filter { it.last() == 'A' }
    .map {
      var vertex = it
      var count = 0L
      for (step in steps()) {
        if (vertex.last() == 'Z') break
        count++
        vertex = graph.getValue(vertex).run { if (step == 'L') left else right }
      }
      count
    }
    .reduce(::lcm)
    .let(::println)
}