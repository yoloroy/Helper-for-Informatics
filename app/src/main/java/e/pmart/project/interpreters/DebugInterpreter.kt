package e.pmart.project.interpreters

interface DebugInterpreter {
    fun run(parentIndex: Int = 0): Iterable<DebugInfo>

    fun output(message: String)
}