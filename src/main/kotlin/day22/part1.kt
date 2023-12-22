package day22

import lines
import kotlin.math.max
import kotlin.math.min

fun main() {
  val fallingBricks = lines().mapIndexed { index, line -> Brick(index, line) }.sortedBy { it.z.first }
  val lyingBricks = mutableListOf<Brick>()

  for (brick in fallingBricks) {
    val brickBelow = lyingBricks.filter { brick.inStack(it) }.maxByOrNull { it.z.last }
    if (brickBelow == null) {
      lyingBricks.add(brick.copy(z = 0..(brick.z.last - brick.z.first)))
    } else {
      lyingBricks.add(brick.copy(z = (brickBelow.z.last + 1)..(brickBelow.z.last + 1 + brick.z.last - brick.z.first)))
    }
  }
  val removableBricks = Array(lyingBricks.size) { true }

  lyingBricks.forEach { brick ->
    val bricksUnder = lyingBricks.filter { brick.inStack(it) }.filter { brick.z.first - 1 == it.z.last }
    if (bricksUnder.size == 1) removableBricks[bricksUnder.first().index] = false
  }
  println(removableBricks.count { it })
}

data class Brick(
  val index: Int,
  val x: IntRange,
  val y: IntRange,
  val z: IntRange,
) {
  fun inStack(other: Brick): Boolean =
    !(x.last < other.x.first || x.first > other.x.last) && !(y.last < other.y.first || y.first > other.y.last)

  companion object {
    operator fun invoke(index: Int, string: String): Brick {
      val (first, second) = string.split("~")
      val (x1, y1, z1) = first.split(",").map { it.toInt() }
      val (x2, y2, z2) = second.split(",").map { it.toInt() }
      return Brick(index, correctRange(x1, x2), correctRange(y1, y2), correctRange(z1, z2))
    }
  }
}

fun correctRange(a: Int, b: Int) = min(a, b)..max(a, b)
