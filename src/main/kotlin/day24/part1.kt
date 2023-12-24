package day24

import lines

fun main() {
  val testAriaMin = "200000000000000".toCoordinateType()
  val testAriaMax = "400000000000000".toCoordinateType()

  val hailstones = lines().map { Hailstone(it) }.toList()

  sequence {
    for (i in hailstones.indices) {
      for (j in i + 1..<hailstones.size) {
        yield(intersection(hailstones[i], hailstones[j]))
      }
    }
  }
    .filterNotNull()
    .count { position ->
      (position.x - testAriaMin).compareToZero() >= 0 &&
        (position.x - testAriaMax).compareToZero() <= 0 &&
        (position.y - testAriaMin).compareToZero() >= 0 &&
        (position.y - testAriaMax).compareToZero() <= 0
    }
    .let { println(it) }
}

private fun isParallel(first: Velocity, second: Velocity): Boolean =
  second.x * first.y == first.x * second.y

private fun intersection(first: Hailstone, second: Hailstone): Position? {
  if (isParallel(first.velocity, second.velocity)) return null

  val result = second.position - first.position
  val beta =
    (result.x * first.velocity.y - result.y * first.velocity.x) / (second.velocity.x * first.velocity.y - first.velocity.x * second.velocity.y)
  val alpha = if (first.velocity.x.compareToZero() == 0) {
    (result.y - beta * second.velocity.y) / first.velocity.y
  } else {
    (result.x - beta * second.velocity.x) / first.velocity.x
  }
  if (alpha.compareToZero() < 0 || beta.compareToZero() > 0) return null

  return second.position - second.velocity * beta
}

data class Hailstone(
  val position: Position,
  val velocity: Velocity,
) {
  companion object {
    operator fun invoke(string: String): Hailstone {
      val (positionString, velocityString) = string.split(Regex("\\s*@\\s*"))
      val position = positionString.split(Regex(",\\s*")).map { it.toCoordinateType() }
      val velocity = velocityString.split(Regex(",\\s*")).map { it.toCoordinateType() }
      return Hailstone(
        Position(position[0], position[1], position[2]),
        Velocity(velocity[0], velocity[1], velocity[2]),
      )
    }
  }
}

data class Position(
  val x: CoordinateType,
  val y: CoordinateType,
  val z: CoordinateType,
) {
  operator fun minus(other: Position) = Velocity(x - other.x, y - other.y, z - other.z)

  operator fun plus(other: Velocity) = Position(x + other.x, y + other.y, z + other.z)
  operator fun minus(other: Velocity) = Position(x - other.x, y - other.y, z - other.z)
}

data class Velocity(
  val x: CoordinateType,
  val y: CoordinateType,
  val z: CoordinateType,
) {
  operator fun times(coefficient: CoordinateType) = Velocity(x * coefficient, y * coefficient, z * coefficient)
  operator fun plus(other: Velocity) = Velocity(x + other.x, y + other.y, z + other.z)
}

/*typealias CoordinateType = Double

fun String.toCoordinateType(): CoordinateType = toDouble()
fun CoordinateType.compareToZero(): Int = when {
  this > 0.0 -> 1
  this < 0.0 -> -1
  else -> 0
}*/
typealias CoordinateType = Long

fun String.toCoordinateType(): CoordinateType = toLong()
fun CoordinateType.compareToZero(): Int = when {
  this > 0L -> 1
  this < 0L -> -1
  else -> 0
}