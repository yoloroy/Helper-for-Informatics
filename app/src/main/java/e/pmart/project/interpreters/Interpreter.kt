package e.pmart.project.interpreters

interface Interpreter {
    /*
    * Main interface for all Interpreters
    */

    fun run()

    fun runDebug(): Iterable<DebugInfo>

    fun output(message: String)
}