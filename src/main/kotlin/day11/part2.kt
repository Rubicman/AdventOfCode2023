package day11

import lines
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import kotlin.math.abs

val EMPTY_LINE_SIZE = BigDecimal(1_000_000)

fun main() {
  val galaxies = buildList {
    lines().forEachIndexed { i, sting ->
      sting.forEachIndexed { j, char ->
        if (char == '#') add(i to j)
      }
    }
  }
  val lines = galaxies.map { it.first }.toSet()
  val columns = galaxies.map { it.second }.toSet()

  galaxies.sumOf { g1 ->
    galaxies.sumOf { g2 ->
      BigDecimal(abs(g1.first - g2.first)) * EMPTY_LINE_SIZE +
        BigDecimal(abs(g1.second - g2.second)) * EMPTY_LINE_SIZE -
        BigDecimal((g1.first increasingRange g2.first).count { it in lines }) * (EMPTY_LINE_SIZE - ONE) -
        BigDecimal((g1.second increasingRange g2.second).count { it in columns }) * (EMPTY_LINE_SIZE - ONE)
    }
  }.let { println(it / BigDecimal(2)) }
}
