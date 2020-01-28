package e.pmart.project

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import kotlinx.android.synthetic.main.fragment_resh1.*
import org.mariuszgromada.math.mxparser.Expression
import java.lang.Double.NaN
import java.math.BigInteger


class Resh1Fragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh1, container, false)
    }

    override fun onStart() {
        val resh1_num_type_adapter: SpinnerAdapter = ArrayAdapter<String>(
                context,
                R.layout.simple_scalable_list_item_1,
                resources.getStringArray(R.array.resh1_types))

        resh1_num_type.adapter = resh1_num_type_adapter

        resh1_evaluate.setOnClickListener{
            try {
                if (resh1_enter.text.isNotEmpty()) {
                    textView6!!.visibility = View.VISIBLE
                    textView7!!.visibility = View.VISIBLE

                    val reg = Regex("(?<=[ *+-])|(?=[ *+-])")
                    val text = resh1_enter!!.text.split(reg)
                    val new_text = ArrayList<String>()
                    textView7!!.text = ""

                    for (i in text) {
                        if ("." in i) {
                            val temp = i.split(".")
                            new_text.add(BigInteger(temp[0], Integer.valueOf(temp[1])).toString())
                            textView7!!.text = "${textView7!!.text}$i = ${new_text.last()}.10\n"
                        } else
                            new_text.add(i)
                    }
                    val e = Expression()
                    e.expressionString = new_text.joinToString("")
                    if (e.calculate() == NaN)
                        throw Exception("bad expression")

                    val temp = e.calculate().toInt()

                    textView7!!.text = "${textView7!!.text}${new_text.joinToString("")} = $temp"

                    resh1_answerLayout!!.visibility = View.VISIBLE

                    when (resh1_num_type!!.selectedItemPosition) {
                        0 -> textView10!!.text = temp.toString(16)
                        1 -> textView10!!.text = temp.toString(10)
                        2 -> textView10!!.text = temp.toString(8)
                        3 -> textView10!!.text = temp.toString(2)
                        4 -> textView10!!.text = (temp.toString(2).split("1").size-1).toString()
                        5 -> textView10!!.text = (temp.toString(2).split("0").size-1).toString()
                    }
                }
                else {
                    Snackbar.make(it, "Введите, пожалуйста, число или выражение из условия задачи", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show()
                }
            } catch (e: Exception) {
                Snackbar.make(it, "Проверьте, пожалуйста, ведённые данные", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
            }

        }

        super.onStart()
    }
}
