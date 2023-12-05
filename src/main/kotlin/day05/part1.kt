package day05

import lines

fun main() {
  val lineIterator = lines().iterator()
  val seeds = lineIterator.next().drop(7).split(" ").map(String::toLong)
  lineIterator.next()

  val mappers = mappers(lineIterator)

  var source = "seed"
  var values = seeds
  while (source != "location") {
    val mapper = mappers.first { it.from == source }
    values = values.map(mapper::map)
    source = mapper.to
  }

  println(values.min())
}

fun mappers(lineIterator: Iterator<String>): MutableList<Mapper> {
  val mappers = mutableListOf<Mapper>()
  while (lineIterator.hasNext()) {
    val (from, to) = lineIterator.next().dropLast(5).split("-to-")
    val mapper = Mapper(from, to)
    while (lineIterator.hasNext()) {
      val line = lineIterator.next()
      if (line.isBlank()) break
      val (d, s, l) = line.split(" ").map(String::toLong)
      mapper.ranges.add(MapRange(d, s, l))
    }
    mappers.add(mapper)
  }
  return mappers
}

class Mapper(
  val from: String,
  val to: String,
) {
  val ranges = mutableListOf<MapRange>()

  fun map(value: Long) =
    ranges.find { value in it.sourceRange }?.let { value - it.sourceRange.first + it.destinationStart } ?: value
}

class MapRange(
  val destinationStart: Long,
  sourceStart: Long,
  length: Long,
) {
  val sourceRange = sourceStart..<sourceStart + length
}