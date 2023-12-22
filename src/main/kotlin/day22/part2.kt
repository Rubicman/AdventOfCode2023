package day22

import lines

fun main() {
  val fallingBricks = lines().mapIndexed { index, line -> Brick(index, line) }.sortedBy { it.z.first }.toList()
  val lyingBricks = dropBricks(fallingBricks)

  var answer = 0
  for (i in fallingBricks.indices) {
    val newBricks = lyingBricks.subList(0, i) + lyingBricks.subList(i + 1, lyingBricks.size)
    val newLyingBricks = dropBricks(newBricks)

    for (j in newBricks.indices) {
      if (newBricks[j] != newLyingBricks[j]) answer++
    }
  }
  println(answer)
}

fun dropBricks(fallingBricks: List<Brick>): List<Brick> {
  val lyingBricks = mutableListOf<Brick>()

  for (brick in fallingBricks) {
    val brickBelow = lyingBricks.filter { brick.inStack(it) }.maxByOrNull { it.z.last }
    if (brickBelow == null) {
      lyingBricks.add(brick.copy(z = 0..(brick.z.last - brick.z.first)))
    } else {
      lyingBricks.add(brick.copy(z = (brickBelow.z.last + 1)..(brickBelow.z.last + 1 + brick.z.last - brick.z.first)))
    }
  }
  return lyingBricks
}