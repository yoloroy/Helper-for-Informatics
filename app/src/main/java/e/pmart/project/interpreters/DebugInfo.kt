package e.pmart.project.interpreters

class DebugInfo(
        val line: String,
        val index: Int,
        val vars: LinkedHashMap<String, Double> = LinkedHashMap(),
        var print: String = ""
)