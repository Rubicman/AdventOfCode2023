package day05

import lines

fun main() {
  val lineIterator = lines().iterator()
  val seeds = lineIterator.next()
    .drop(7)
    .split(" ")
    .map(String::toLong)
    .chunked(2)
    .map { (start, length) -> start..<start + length }
  lineIterator.next()

  val mappers = mappers(lineIterator)

  var source = "seed"
  var ranges = seeds
  while (source != "location") {
    val mapper = mappers.first { it.from == source }
    ranges = ranges.flatMap(mapper::mapRanges).concat()

    source = mapper.to
  }

  println(ranges.first().first)
}

fun List<LongRange>.concat(): List<LongRange> =
  sequence {
    sortedBy { it.first }.reduce { acc, next ->
      if (acc.last < next.first) {
        yield(acc)
        return@reduce next
      } else {
        return@reduce acc.first..next.last
      }
    }.let { yield(it) }
  }.toList()

fun Mapper.mapRanges(range: LongRange): List<LongRange> {
  val mapRange = ranges.find { range intersect it.sourceRange } ?: return listOf(range)

  val leftRanges = if (range.first < mapRange.sourceRange.first)
    mapRanges(range.first..<mapRange.sourceRange.first)
  else
    emptyList()

  val middleRange = ((maxOf(range.first, mapRange.sourceRange.first)..minOf(range.last, mapRange.sourceRange.last)))
    .let { (it.first - mapRange.sourceRange.first + mapRange.destinationStart)..(it.last - mapRange.sourceRange.first + mapRange.destinationStart) }

  val rightRanges = if (range.last > mapRange.sourceRange.last)
    mapRanges(mapRange.sourceRange.last + 1..range.last)
  else
    emptyList()

  return buildList {
    addAll(leftRanges)
    add(middleRange)
    addAll(rightRanges)
  }
}

infix fun LongRange.intersect(other: LongRange): Boolean =
  first <= other.last && last >= other.first