package day20

import day20.Pulse.HIGH
import day20.Pulse.LOW
import lines
import java.math.BigDecimal.ZERO

fun main() {
  val preModules = lines().map { Module(it) }.toList()
  val modules = preModules
    .map { module -> if (module is PreConjunctionModule) module.finish(preModules) else module }
    .associateBy { it.name }
    .toMutableMap()

  val queue = ArrayDeque<State>()

  var lows = ZERO
  var highs = ZERO

  repeat(1_000) {
    queue.add(State("", LOW,"broadcaster"))
    while (queue.isNotEmpty()) {
      val (from, pulse, to) = queue.removeFirst()
      if (pulse == LOW) lows++
      if (pulse == HIGH) highs++

      val module = modules[to] ?: continue
      val result = module.activate(from, pulse) ?: continue

      modules[module.name] = result.newModule
      result.nextModules.forEach { next ->
        queue.add(State(module.name, result.sentPulse, next))
      }
    }
  }

  println(lows * highs)
}

enum class Pulse {
  LOW, HIGH
}

data class Result(
  val newModule: Module,
  val sentPulse: Pulse,
  val nextModules: List<String>
)

data class State(
  val from: String,
  val pulse: Pulse,
  val to: String,
)

interface Module {
  val name: String
  val nextModules: List<String>
  fun activate(from: String, pulse: Pulse): Result?

  companion object {
    operator fun invoke(string: String): Module {
      val (module, next) = string.split(" -> ")
      val nextModules = next.split(", ")

      if (module == "broadcaster") return BroadcastModule(nextModules)
      val name = module.drop(1)
      if (module.first() == '%') return FlipFlopModule(name, nextModules)
      if (module.first() == '&') return PreConjunctionModule(name, nextModules)
      throw IllegalStateException()
    }
  }
}

data class FlipFlopModule(
  override val name: String,
  override val nextModules: List<String>,
  val on: Boolean = false,
) : Module {
  override fun activate(from: String, pulse: Pulse): Result? =
    if (pulse == LOW) {
      Result(
        copy(on = !on),
        if (on) LOW else HIGH,
        nextModules
      )
    } else {
      null
    }
}

data class PreConjunctionModule(
  override val name: String,
  override val nextModules: List<String>
) : Module {
  override fun activate(from: String, pulse: Pulse): Result? {
    throw NotImplementedError()
  }

  fun finish(modules: List<Module>): ConjunctionModule =
    ConjunctionModule(name, nextModules, modules.filter { it.nextModules.contains(name) }.map { it.name })
}

data class ConjunctionModule(
  override val name: String,
  override val nextModules: List<String>,
  val inputs: Map<String, Pulse>
) : Module {
  constructor(name: String, nextModules: List<String>, inputs: List<String>) :
    this(name, nextModules, inputs.associateWith { LOW })

  override fun activate(from: String, pulse: Pulse): Result {
    val newInputs = inputs + (from to pulse)
    return if (newInputs.values.all { it == HIGH }) {
      Result(copy(inputs = newInputs), LOW, nextModules)
    } else {
      Result(copy(inputs = newInputs), HIGH, nextModules)
    }
  }
}

data class BroadcastModule(
  override val nextModules: List<String>,
) : Module {
  override val name: String = "broadcaster"
  override fun activate(from: String, pulse: Pulse): Result =
    Result(this, pulse, nextModules)
}
