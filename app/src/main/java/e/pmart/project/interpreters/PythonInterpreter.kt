package e.pmart.project.interpreters

import org.mariuszgromada.math.mxparser.Expression


private const val CONTINUE = -1

open class PythonInterpreter(val code: String): Interpreter {
    private var vars: LinkedHashMap<String, Double> = LinkedHashMap()  // значения всех известных переменных
    private var funcs: LinkedHashMap<String, Int> = LinkedHashMap()  // номера строк объявления функций

    constructor(code: String,
                _vars: LinkedHashMap<String, Double>,
                _funcs: LinkedHashMap<String, Int>) : this(code) {
        vars = _vars
        funcs = _funcs
    }

    private var debugInfo = ArrayList<DebugInfo>()
    private var debugInfoTemp = ArrayList<DebugInfo>()

    init {
        vars["True"] = 1.0
        vars["False"] = 0.0
    }

    override fun run() {
        var pass = CONTINUE
        code.split("\n").forEachIndexed { index, s ->
            if (index > pass)
                pass = CONTINUE
            if (pass == CONTINUE)
                pass = execLine(s, index)
        }
    }

    override fun runDebug(): List<DebugInfo> {
        return runDebug(0)
    }

    private fun runDebug(parentIndex: Int): List<DebugInfo> {
        debugInfo = ArrayList()
        debugInfoTemp = ArrayList()
        var pass = CONTINUE
        "${code.trimEnd()}\n".split("\n").forEachIndexed { index, s ->if (index > pass)
                pass = CONTINUE
            if ((pass == CONTINUE) and s.isNotBlank())
                debugExecLine(s, index + parentIndex).also {
                    pass = it.first
                    if (s.startsWith("print"))
                        if ('"' in s)
                            it.second.print = s.substring(7, s.length-2)
                        else
                            it.second.print = eval(s.substring(6, s.length-1))
                    debugInfo.add(it.second)
                    debugInfo.addAll(debugInfoTemp)
                    debugInfoTemp = ArrayList()
                }
        }
        return debugInfo
    }

    private fun execLine(line: String, index: Int): Int {
        if (line.startsWith("def")) {
            funcs[line.substring(4, line.indexOf("("))] = index
            return CONTINUE
        }
        if ((line.count("=")
                        - line.count("==")*2
                        - line.count("!=")
                        - line.count("<=")
                        - line.count(">=")) == 1)
            return execSet(line)

        if (line.startsWith("if"))
            return execIf(line, index)

        if (line.startsWith("while"))
            return execWhile(line, index)

        if (line.startsWith("print"))
            return execPrint(line)

        return CONTINUE
    }

    private fun debugExecLine(line: String, index: Int): Pair<Int, DebugInfo> {
        vars.also {temp ->
            temp.remove("True")
            temp.remove("False")

            // этот костыль нужен чтоб переопределения порядка выполнения
            // + с ним return выглядит проще
            DebugInfo(line.replace("\n", "\\n"), index, vars).also {info ->
                return Pair(execLine(line, index), info)
            }
        }
    }

    private fun execSet(line: String): Int {
        vars[line.split("=")[0].trim()] = eval(line.split("=").subList(1).joinToString("=")).toDouble()
        vars = vars.sortedByKeyLength()
        return CONTINUE
    }

    private fun execIf(line: String, index: Int): Int {
        var temp = ""
        if (eval(line.substring(3 until line.length-1)) == "1.0") {
            for (i in code.split("\n").subList(index+1)) {
                if (i.startsWith("    "))
                    temp += i.substring(4) + "\n"
                else
                    break
            }
            PythonInterpreter(temp, vars, funcs).also { inner ->
                inner.runDebug(index + 1).forEach { info ->
                    debugInfoTemp.add(info)
                }
                vars = inner.vars
                funcs = inner.funcs
            }
        }
        return index + temp.split("\n").size - 1
    }

    private fun execWhile(line: String, index: Int): Int {
        var temp = ""
        // нахождение всех строк, принадлежащих while
        for (i in code.split("\n").subList(index + 1)) {
            if (i.startsWith("    "))
                temp += i.substring(4) + "\n"
            else
                break
        }
        while (eval("(${line.substring(6 until line.length - 1)})") == "1.0") {
            if (temp != "")
                PythonInterpreter(temp, vars, funcs).also { inner ->
                    inner.runDebug(index+1).forEach { info ->
                        debugInfoTemp.add(info)
                    }
                    vars = inner.vars
                    funcs = inner.funcs
                }
        }
        return index + temp.split("\n").size -1
    }

    private fun execPrint(line: String): Int {
        if ('"' in line)
            output(line.substring(7, line.length-2))
        else
            output(eval(line.substring(6, line.length-1)))
        return CONTINUE
    }

    /* Нужно переопределять при инициализации */
    override fun output(message: String) {
        // do something
    }

    // TODO: create own expression evaluator
    private fun eval(expression: String): String {
        Expression().also {
            it.mySetExpressionString(fillExpression(expression))
            return it.calculate().toString()
        }
    }

    private fun Expression.mySetExpressionString(string: String) {
        expressionString = string.replace("%", "#")
    }

    private fun String.count(substring: String): Int = (length - replace(substring, "").length) / substring.length

    private fun <T> List<T>.subList(fromIndex: Int) = this.subList(fromIndex, this.size)

    private fun fillExpression(expression: String) = expression.replaceAll(vars)

    private fun String.replaceAll(replaces: Map<String, Any>): String {
        var temp = this
        var temp_temp = ""

        while (temp_temp != temp) {
            temp_temp = temp
            for ((k, v) in replaces) {
                temp = temp.replace(k, v.toString())
            }
        }

        return temp
    }

    private fun <T> Map<String, T>.sortedByKeyLength() =
            LinkedHashMap<String, T>().also { sortedMap ->
                (this.toList().groupBy { it.first.length }).also {
                    it.keys.sorted().forEach { i ->
                        sortedMap.putAll(it[i]!!.toSet())
                    }
                }
            }
}