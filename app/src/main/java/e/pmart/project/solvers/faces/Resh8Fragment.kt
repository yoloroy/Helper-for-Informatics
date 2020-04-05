package e.pmart.project.solvers.faces


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView

import e.pmart.project.R
import e.pmart.project.interpreters.DebugInfo
import e.pmart.project.interpreters.PythonInterpreter

import kotlinx.android.synthetic.main.fragment_resh8.*


private const val EDIT = 0
private const val RUN = 1

class Resh8Fragment : Fragment() {
    private var mode = EDIT
    private var debugInfo: List<DebugInfo> = ArrayList()
    private var step = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh8, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        resh8_language.adapter = ArrayAdapter<String>(
                context,
                R.layout.simple_scalable_list_item_1,
                resources.getStringArray(R.array.code_languages))

        setOnClicks()

        super.onStart()
    }

    private fun setOnClicks() {
        resh8_evaluate.setOnClickListener {
            if (mode != EDIT)
                return@setOnClickListener

            highlight.loadDataWithBaseURL(null,
                    """
                    <html>
                    <head>
                    <style type="text/css">
                    .stroke { color: #777}
                    .number { color: #1057c3 }
                    .spec_symb { color: #800800}
                    .spec_word { color: #1262de; font-weight:bold }
                    .ioput { color: #001272; text-decoration: underline }
                    .main { font-size: 14pt; font-family: Courier New; }
                    </style>
                    </head>
                    <body><div class="main">${getCode().toHighlightHTML()}</div></body>
                    </html>""",
                    "text/html", "en_US", null)

            runCode()
            step = 1
            viewDebugInfo(debugInfo.elementAt(0))

            mode = RUN
            code_enter.visibility = GONE
            highlight.visibility = VISIBLE

            debug_shadow.visibility = VISIBLE
            resh8_step.visibility = VISIBLE
            debug_content.visibility = VISIBLE
        }

        resh8_edit.setOnClickListener {
            if (mode != RUN)
                return@setOnClickListener

            debugInfo = ArrayList()

            mode = EDIT
            code_enter.visibility = VISIBLE
            highlight.visibility = GONE

            debug_shadow.visibility = INVISIBLE
            resh8_step.visibility = INVISIBLE
            debug_content.visibility = INVISIBLE
        }

        resh8_step.setOnClickListener {
            if (step < debugInfo.size) {
                viewDebugInfo(debugInfo.elementAt(step))
                step += 1
            } else
                Snackbar.make(it, "Произведение завершено", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
        }
    }

    // code part start{
    private fun getCode() = code_enter.text.toString()

    private fun runCode() {
        when (getLanguage()) {
            "Python" -> debugInfo = PythonInterpreter(getCode()).runDebug()
        }
    }

    private fun getLanguage() = resh8_language.adapter.getItem(resh8_language.selectedItemPosition)

    private fun String.toHighlightHTML(): String {
        when (getLanguage()) {
            "Python" -> return addNumeration(
                        Regex("[\\d]").replace(
                            Regex("(print|input)").replace(
                                Regex("(if|while|for|return)").replace(
                                    Regex("(=|&lt;|&gt;|==|[+\\-*/])").replace(
                                            this.replaceAll(mapOf(Pair("<", "&lt;"),
                                                                  Pair(">", "&gt;")))
                                                    .replace(" ", "&nbsp;")
                                                    .replace("\n", "<br>")
                                    ) {s -> "<span class=\"spec_symb\">${s.value}</span>"}
                                ) {s -> "<span class=\"spec_word\">${s.value}</span>"}
                            ) {s -> "<span class=\"ioput\">${s.value}</span>"}
                        ) { s -> "<span class=\"number\">${s.value}</span>" })
        }
        return "<div>$this</div>"
    }

    private fun addNumeration(highlightCode: String): String {
        var temp = highlightCode
        for (i in 1 until highlightCode.split("<br>").size)
            temp = temp.replaceFirst("<br>", "<span class=\"stroke\"><bruh>$i|</span> ")

        return "<span class=\"stroke\">0|</span> ${temp.replaceAll(mapOf(Pair("<bruh>", "<br>")))}"
    }
    //} code part end

    // debug part start{
    private fun viewDebugInfo(debugInfo: DebugInfo) {
        resh8_step_info.text = step.toString()
        resh8_line.text = debugInfo.line
        resh8_index.text = debugInfo.index.toString()

        // чистим прошлые переменные
        vars.removeAllViews()
        // заполняем переменными
        debugInfo.vars.forEach { variable ->
            LinearLayout(context).also { layout ->
                layout.layoutParams = ViewGroup.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                layout.orientation = LinearLayout.HORIZONTAL

                TextView(ContextThemeWrapper(context, R.style.CodeDebugValueName)).also { name ->
                    name.text = variable.key
                    layout.addView(name)
                }
                TextView(ContextThemeWrapper(context, R.style.CodeDebugValueValue)).also { value ->
                    value.text = variable.value.toString()
                    layout.addView(value)
                }

                vars.addView(layout)
            }
        }

        // выводим, если есть что выводить
        if (debugInfo.print != "") print(debugInfo.print)
    }

    private fun print(message: String) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle("Python пишет:")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("ОК"
                ) { dialog, _ -> dialog.cancel() }
                .create()
                .show()
    }
    //} debug part end

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
}
