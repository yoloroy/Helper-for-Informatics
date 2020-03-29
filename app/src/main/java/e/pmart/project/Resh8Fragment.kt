package e.pmart.project


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_resh8.*


class Resh8Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh8, container, false)
    }
    /*
    * web_view.loadUrl("file:///android_asset/blockly/webview.html")

        resh8_evaluate.setOnClickListener {
            web_view.loadUrl("javascript:runCode()")
        }
        */

    @SuppressLint("SetJavaScriptEnabled")
    override fun onStart() {
        web_view.webChromeClient = object : WebChromeClient() {
            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                AlertDialog.Builder(context!!)
                        .setTitle("Решатор №8 говорит:")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok) { _, _ -> result?.confirm() }
                        .setCancelable(false)
                        .create()
                        .show()

                return true
            }
        }
        web_view.settings.javaScriptEnabled = true

        resh8_language.adapter = ArrayAdapter<String>(
                context,
                R.layout.simple_scalable_list_item_1,
                resources.getStringArray(R.array.code_languages))
        resh8_language.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hide()
                resh8_language.also {
                    when (it.adapter.getItem(position)) {
                        "Blockly" -> {
                            card_view.layoutParams.height = MATCH_PARENT
                            web_view.visibility = VISIBLE
                            web_view.loadUrl("file:///android_asset/blockly/webview.html")
                        }
                        "Алгоритмический" -> code_enter.visibility = VISIBLE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        resh8_evaluate.setOnClickListener {
            when (resh8_language.adapter.getItem(resh8_language.selectedItemPosition)) {
                "Blockly" -> web_view.loadUrl("javascript:runCode()")
                "Алгоритмический" -> web_view.loadDataWithBaseURL(null, "<html><body><script>\n" +
                        "${AlgJsTranslator().translate(code_enter.text.toString())}\n" +
                        "</script></body></html>", "text/html", "en_US", null)
            }
        }

        super.onStart()
    }

    fun hide() {
        card_view.layoutParams.height = WRAP_CONTENT
        web_view.visibility = GONE
        code_enter.visibility = GONE
    }
}
