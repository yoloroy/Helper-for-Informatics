package e.pmart.project.interpreters

interface Interpreter {
    fun run()

    fun output(message: String)
}