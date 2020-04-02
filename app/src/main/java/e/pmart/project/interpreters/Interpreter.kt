package e.pmart.project.interpreters

interface Interpreter {
    fun run()

    fun runDebug(): Iterable<DebugInfo>

    fun output(message: String)
}