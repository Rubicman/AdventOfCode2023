package day24

import lines

fun main() {
  val hailstones = lines().map { Hailstone(it) }.toList()
  val equations = StringBuilder()
  for (i in 0..2) {
    val t = "t$i"
    equations.append(t).append(" >= 0, ").append(hailstones[i].position.x).append(" + ").append(hailstones[i].velocity.x).append(t)
      .append(" == x + vx ").append(t).append(", ")
    equations.append(hailstones[i].position.y).append(" + ").append(hailstones[i].velocity.y).append(t).append(" == y + vy ").append(t)
      .append(", ")
    equations.append(hailstones[i].position.z).append(" + ").append(hailstones[i].velocity.z).append(t).append(" == z + vz ").append(t)
      .append(", ")
  }
  val sendToMathematica = "Solve[{" + equations.substring(0, equations.length - 2) + "}, {x,y,z,vx,vy,vz,t0,t1,t2}]"
  println(sendToMathematica)

  println(192863257090212L + 406543399029824L + 181983899642349L)
}
