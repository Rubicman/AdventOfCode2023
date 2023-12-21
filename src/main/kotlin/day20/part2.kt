package day20

import day20.Pulse.HIGH
import day20.Pulse.LOW
import lcm
import lines

fun main() {
  val preModules = lines().map { Module(it) }.toList()
  val modules = preModules
    .map { module -> if (module is PreConjunctionModule) module.finish(preModules) else module }
    .associateBy { it.name }
    .toMutableMap()

  val queue = ArrayDeque<State>()
  var pushes = 0L
  val searchNames = (modules.values
    .first { it.nextModules.contains(FINAL_NAME) } as ConjunctionModule)
    .inputs.keys
    .toMutableSet()
  var answer = 1L

  mainLoop@ while (pushes < 10_000) {
    pushes++
    queue.add(State("", LOW, "broadcaster"))
    while (queue.isNotEmpty()) {
      val (from, pulse, to) = queue.removeFirst()
      val module = modules[to] ?: continue

      val result = module.activate(from, pulse) ?: continue

      if (to in searchNames && result.sentPulse == HIGH) {
        searchNames.remove(to)
        answer = lcm(answer, pushes)
      }

      modules[module.name] = result.newModule
      for (next in result.nextModules) {
          queue.add(State(module.name, result.sentPulse, next))
      }
    }
  }

  println(answer)
}



const val FINAL_NAME = "rx"