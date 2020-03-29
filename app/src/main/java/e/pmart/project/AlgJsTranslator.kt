package e.pmart.project


class AlgJsTranslator {  // translate algoritmic into JS
    private val divModAddition = "" +
            "function div(a, b) {\n" +
            "    return ~~(a / b)\n" +
            "}\n" +
            "\n" +
            "\n" +
            "function mod(a, b) {\n" +
            "    return a % b\n" +
            "}\n" +
            "\n"

    private var divModRequired = false

    var types = object : HashMap<String, String>() {
        val default = ""

        override fun get(key: String): String? {
            return if (contains(key)) super.get(key) else default
        }
    }

    private fun String.replaceAll(replaces: Map<String, String>): String {
        var temp = this
        var temp_temp = ""

        while (temp_temp != temp) {
            temp_temp = temp
            for ((k, v) in replaces) {
                temp = temp.replace(k, v)
            }
        }

        return temp
    }

    fun translate(alg_code: String): String {
        var js_code = ""
        for (line in alg_code.split("\n")) {
            checkLine(line).also{
                if (it != "") {
                    js_code += (if (it.endsWith("\n")) it.substring(0, it.length-1) else it) + "\n"
                }
            }
        }

        return (if (divModRequired) divModAddition else "") + beautify(js_code)
    }

    private fun checkLine(line_: String): String {
        val line = line_.trim()

        if (line.contains("div") or line.contains("mod"))
            divModRequired = true

        // these constructions are used in order not to check the line once again
        checkWhile(line).also{
            if (it.changed)
                return it.line
        }

        checkOther(line).also{
            if (it.changed)
                return it.line
        }

        checkVar(line).also {
            if (it.changed)
                return it.line
        }

        checkIO(line).also {
            if (it.changed)
                return it.line
        }

        checkIf(line).also {
            if (it.changed)
                return it.line
        }

        return line
    }

    private fun checkWhile(line_: String): Temp {
        var line = line_

        if (line.contains("пока"))
            line = line.replace("пока ", "while (") + ")"

        if (line.contains("нц"))
            line = line.replace("нц ", "") + " {"

        if (line.contains("кц"))
            line = line.replace("кц", "}")

        if (line != line_)
            line = checkLogic(line).line

        return Temp(line, line != line_)
    }

    private fun checkVar(line_: String): Temp {
        var line = line_

        if (line.contains("цел")) {
            for (variable in line.replace("цел ", "")
                                 .replace("    ", "")
                                 .split(", "))
                types[variable] = "Number"

            line = line.replace("цел", "var") + "  // Integer"
        }

        line = line.replace(":=", " = ")

        return Temp(line, line != line_)
    }

    private fun checkOther(line_: String): Temp {
        var line = line_

        if (line.startsWith("алг")) {
            line = line.replace("алг", "")
            if (line.isNotEmpty()) {
                line = "function " + line.replaceAll(hashMapOf(Pair("цел", ""), Pair("  ", " ")))
            }
        }

        if (line.startsWith("знач")) {
            line = line.replaceAll(hashMapOf(Pair("знач", "return"), Pair(":=", ""), Pair("  ", " ")))
        }

        line = line.replace("нач", "{")

        line = line.replace("кон", "}")

        return Temp(line, line != line_)
    }

    private fun checkIO(line_: String): Temp {
        var line = line_

        if (line.contains("вывод")) {
            var temp = ""
            for (variable in line.replace("    ", "")
                    .replace("вывод ", "")
                    .split(", "))
                temp += "alert($variable)\n"

            line = temp
        }

        if (line.contains("ввод")) {
            var temp = ""
            for (variable in line.replace("    ", "")
                          .replace("ввод ", "")
                          .split(", "))
                temp += "$variable = ${types[variable]}(prompt(\"Введите $variable\", \"\"))\n"

            line = temp
        }

        return Temp(line, line != line_)
    }

    private fun checkIf(line_: String): Temp {
        var line = line_

        if (line.contains("если")) {
            line = line.replace("если ", "if (") + ")"
            line = checkLogic(line).line
        }

        line = line.replace("то", "{")

        line = line.replace("все", "}")

        return Temp(line, line != line_)
    }

    private fun checkLogic(line_: String): Temp {
        (line_.replaceAll(hashMapOf(Pair("и", "&&"), Pair("или", "||"), Pair("не", "!")))).also {
            return Temp(it, it != line_)
        }
    }

    private fun beautify(code: String): String {
        var temp = ""
        var padding = 0

        for (line in code.split("\n")) {
            if (line.contains("}")) padding -= 1
            temp += line.padStart(padding*4 + line.length) + "\n"
            if (line.contains("{")) padding += 1
        }

        return temp
    }

    inner class Temp(line_: String, changed_: Boolean) {
        var line = line_
        var changed = changed_
    }
}
