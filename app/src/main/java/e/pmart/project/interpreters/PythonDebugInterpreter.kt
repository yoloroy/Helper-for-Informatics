package e.pmart.project.interpreters

import android.util.Log
import org.mariuszgromada.math.mxparser.Expression


private const val CONTINUE = -1

open class PythonDebugInterpreter(open var code: String): DebugInterpreter {
    var vars: LinkedHashMap<String, Double> = LinkedHashMap()  // значения всех известных переменных
    private var funcs: LinkedHashMap<String, Int> = LinkedHashMap()  // номера строк объявления функций

    constructor(code: String,
                _vars: LinkedHashMap<String, Double>,
                _funcs: LinkedHashMap<String, Int>) : this(code) {
        vars = _vars
        funcs = _funcs
    }

    var debugInfo = ArrayList<DebugInfo>()
    var debugInfoTemp = ArrayList<DebugInfo>()

    init {
        vars["True"] = 1.0
        vars["False"] = 0.0
    }

    fun run() = run(0)

    override fun run(parentIndex: Int): Iterable<DebugInfo> {
        debugInfo = ArrayList()
        debugInfoTemp = ArrayList()
        var pass = CONTINUE
        code.trimEnd().split("\n").forEachIndexed { index, s ->
            if (index > pass)
                pass = CONTINUE
            if (pass == CONTINUE)
                debugExecLine(s, index + parentIndex).also {
                    pass = it.first
                    if (s.startsWith("print"))
                        if ('"' in s)
                            it.second.print = s.substring(7, s.length-2)
                        else
                            Expression().also { e ->
                                e.mySetExpressionString(fillExpression(s.substring(6, s.length-1)))
                                it.second.print = e.calculate().toString()
                            }
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
        if (line.count("=") % 2 == 1)
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
        return Pair(execLine(line, index), DebugInfo(line.replace("\n", "\\n"), index, vars))
    }

    private fun execSet(line: String): Int {
        Expression().also { e ->
            e.mySetExpressionString(fillExpression(line.split("=").subList(1).joinToString("")))
            vars[line.split("=")[0].trim()] = e.calculate()
        }
        vars = vars.sortedByKeyLength()
        return CONTINUE
    }

    private fun execIf(line: String, index: Int): Int {
        var temp = ""
        Expression().also {
            it.mySetExpressionString(fillExpression(line.substring(3 until line.length-1)))
            if (it.calculate() == 1.0) {
                temp = ""
                for (i in code.split("\n").subList(index+1)) {
                    if (i.startsWith("    "))
                        temp += i.substring(4) + "\n"
                    else {
                        PythonDebugInterpreter(temp, vars, funcs).also { inner ->
                            inner.run(index+1).forEach(
                                    action = {
                                        info -> debugInfoTemp.add(info)
                                    }
                            )
                            vars = inner.vars
                            funcs = inner.funcs
                        }
                    }
                }
            }
        }
        return index + temp.split("\n").size -1
    }

    private fun execWhile(line: String, index: Int): Int {
        var temp = ""
        while (true) {
            Expression().also {
                it.mySetExpressionString(fillExpression(line.substring(6 until line.length - 1)))
                if (it.calculate() == 1.0) {
                    temp = ""

                    // нахождение всех строк, принадлежащих while
                    for (i in code.split("\n").subList(index + 1)) {
                        if (i.startsWith("    "))
                            temp += i.substring(4) + "\n"
                        else
                            break
                    }

                    if (temp != "")
                        PythonDebugInterpreter(temp, vars, funcs).also { inner ->
                            inner.run(index+1).forEach(
                                    action = {
                                        info -> debugInfoTemp.add(info)
                                    }
                            )
                            vars = inner.vars
                            funcs = inner.funcs
                        }
                } else return index + temp.split("\n").size -1
            }
        }
    }

    private fun execPrint(line: String): Int {
        if ('"' in line)
            output(line.substring(7, line.length-2))
        else
            Expression().also {
                it.mySetExpressionString(fillExpression(line.substring(6, line.length-1)))
                output(it.calculate().toString())
            }
        return CONTINUE
    }

    /* Нужно переопределять при инициализации */
    override fun output(message: String) {
        Log.i("output", message)
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