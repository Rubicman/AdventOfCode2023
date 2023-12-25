package day25

import lines

fun main() {
  val edges = mutableSetOf<Pair<String, String>>()
  lines()
    .map {it.split(": ")}
    .forEach { (head, tail) -> tail.split(" ").forEach {
      edges.add(head to it)
    } }
  val graph = edges
    .flatMap { listOf(it.first, it.second) }
    .distinct()
    .associateWith { mutableListOf<String>() }
  edges.forEach {
    graph.getValue(it.first).add(it.second)
    graph.getValue(it.second).add(it.first)
  }

  val visited = mutableSetOf(graph.entries.first().key)
  val nextEdges = graph.entries
    .first()
    .value
    .map { graph.entries.first().key to it }
    .toMutableSet()

  while (nextEdges.size > 3) {
    val vertex = nextEdges
      .flatMap { listOf(it.first, it.second) }
      .filter { it !in visited }
      .groupBy { it }
      .maxBy { it.value.size }
      .key
    visited.add(vertex)
    nextEdges.removeIf { it.first in visited && it.second in visited }
    graph.getValue(vertex).filter { it !in visited }.forEach { nextEdges.add(vertex to it) }
  }

  println(visited.size * (graph.size - visited.size))
}
