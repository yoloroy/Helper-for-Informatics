package e.pmart.project


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_appendable_dicts_table.*
import kotlinx.android.synthetic.main.fragment_resh2.*
import org.mariuszgromada.math.mxparser.Expression
import java.lang.Math.pow


class Resh2Fragment : Fragment() {
    // TODO: перекодить всё это заново + надо AppendableDictsTableFragment переделать в нормальный view или fragment для создания библиотеки создания таблиц
    // TODO: сделать решение с пустыми клетками
    private val NAMES = arrayOf("x", "y", "z", "w")
    private var resultList: ArrayList<String> = ArrayList()
    var width = 1
    var height = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resh2, container, false)
    }

    override fun onStart() {
        createOnClicks()

        super.onStart()
    }

    private fun createOnClicks() {
        resh2_evaluate.setOnClickListener {onClickRun()}
        info_resh2.setOnClickListener {
            viewHint()
            openInfo_resh2.scaleX = -openInfo_resh2.scaleX
        }
        openInfo_resh2.setOnClickListener {
            viewHint()
            it.scaleX = -it.scaleX
        }
        createOnClicksPOS()
    }

    private fun viewHint() {
        if (resh2_hint.visibility == TextView.VISIBLE)
            resh2_hint.visibility = TextView.GONE
        else
            resh2_hint.visibility = TextView.VISIBLE
    }

    private fun onClickRun() {
        // code for tasks without empty cells
        val matrix = getMatrix()
        val full_matrix: ArrayList<ArrayList<String>> = ArrayList()
        val max_char_index = width-1

        for (i in 0 until pow(2.toDouble(), max_char_index.toDouble()+1).toInt()) {
            full_matrix.add(ArrayList(toStringArray(i.toString(2).toCharArray()).toList()))
            for (j in 0 .. max_char_index-full_matrix.last().size) {
                full_matrix[i] = (ArrayList(listOf("0")) + full_matrix.last()) as ArrayList<String>
            }
            full_matrix[i].add(calculate(full_matrix[i]))
        }

        computeResult(toCharArray(toHorizontal(matrix)[0]), 0, "")

        var temp_matrix: ArrayList<ArrayList<String>> = ArrayList()
        for (i in resultList) {
            val mask = toMask(toHorizontal(matrix)[0], toStringArray(i.replace("F", "").toCharArray()).toList() as ArrayList<String>)
            temp_matrix = ArrayList()
            temp_matrix = shuffleByMask(matrix, mask) as ArrayList<ArrayList<String>>

            for (j in 0 until temp_matrix.size)
                temp_matrix[j].add(matrix[j].last())

            temp_matrix = toHorizontal(temp_matrix)
            Log.i("resh2", temp_matrix.toString())
            Log.i("resh2", mask.toString())

            if (full_matrix.containsAll(temp_matrix.slice(1 until temp_matrix.size) as ArrayList<ArrayList<String>>)) {
                viewAnswer(i, full_matrix)
                break
            }
        }
        Log.i("resh2", full_matrix.toString())
    }

    private fun viewAnswer(answer: String, matrix: ArrayList<ArrayList<String>>) {
        resh2_answer.setText(answer.slice(0..answer.length-2) + ".")

        resh2_answerLayout.visibility = LinearLayout.VISIBLE
    }

    private fun computeResult(s: CharArray, pos: Int, resultString: String) {
        if (pos == s.size) {
            resultList.add(resultString)
            return
        }
        for (i in 0 until s.size) {
            if (!resultString.contains(s[i].toString())) {
                computeResult(s, pos + 1, resultString + s[i])
            }
        }
    }

    private fun calculate(params: ArrayList<String>): String {
        var temp = resh2_enter.text.toString().toLowerCase()

        for (i in 0 until params.size) {
            temp = temp.replace(NAMES[i], params[i])
        }

        val e = Expression()
        e.expressionString = temp
        return e.calculate().toInt().toString()
    }

    private fun toStringArray(charArray: CharArray): Array<String> {
        var temp = emptyArray<String>()

        for (i in charArray)
            temp += i.toString()

        return temp
    }

    private fun toCharArray(stringArray: Collection<String>): CharArray {
        var temp = CharArray(0)

        for (i in stringArray)
            temp += i.toCharArray()[0]

        return temp
    }

    private fun shuffleByMask(array: Collection<Any>, mask: Collection<Int>): Collection<Any> {
        val temp = ArrayList<Any>()
        for (i in 0 until mask.size) {
            temp.add(ArrayList<Any>())
            temp[i] = array.elementAt(mask.elementAt(i))
        }
        temp.add(array.last())

        return temp
    }

    private fun toMask(item: ArrayList<String>, key: ArrayList<String>): ArrayList<Int> {
        val temp = ArrayList<Int>()
        for (i in item) {
            if (i != "F")
                temp.add(key.indexOf(i))
        }
        return temp
    }

    private fun toHorizontal(verticalArray: Collection<Collection<String>>): ArrayList<ArrayList<String>> {
        val temp = ArrayList<ArrayList<String>>()

        for (x in 0 until verticalArray.elementAt(0).size) {
            temp.add(ArrayList())
            for (y in 0 until verticalArray.size)
                temp[x].add(verticalArray.elementAt(y).elementAt(x))
        }

        return temp
    }

    private fun getMatrix(): ArrayList<ArrayList<String>> {
        val table = ArrayList<ArrayList<String>>()
        for (x in 0 .. width) {
            table.add(getByX(x))
        }
        return table
    }

    private fun getByX(x: Int): ArrayList<String> {
        val column = ArrayList<String>()
        for (y in 0 until height) {
            try {
                column.add(get(x, y))
            } catch (e: Exception) {
                // pass
            }
        }
        return column
    }

    private fun getByY(y: Int): ArrayList<String> {
        val row = ArrayList<String>()
        for (x in 0 .. width) {
            try {
                row.add(get(x, y))
            } catch (e: Exception) {}
        }
        return row
    }

    private fun get(x: Int, y: Int): String {
        return ((grid.getChildAt(x) as LinearLayout).getChildAt(y) as EditText).text.toString()
    }

    private fun createOnClicksPOS() {
        addCol.setOnClickListener {
            if (width < 4)
                onClickAddColumn()
            else
                Snackbar.make(it, "Зачем вам ещё больше столбцов?", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
        }
        delCol.setOnClickListener {if (width > 1) onClickDelColumn()}
        addRow.setOnClickListener {onClickAddRow()}
        delRow.setOnClickListener {if (height > 2) onClickDelRow()}

        rotateMatrix.setOnClickListener {rotate90Degrees()}
    }

    private fun createEmptyCell(): EditText {
        val cell = EditText(context)
        cell.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        cell.setText("")
        cell.setEms(1)
        cell.textAlignment = EditText.TEXT_ALIGNMENT_CENTER
        cell.setPadding(2, 0, 2, 0)
        cell.setBackgroundResource(R.color.colorBackContrast)
        (cell.layoutParams as LinearLayout.LayoutParams).marginEnd = 1
        (cell.layoutParams as LinearLayout.LayoutParams).bottomMargin = 1

        return cell
    }

    private fun onClickAddColumn() {
        width++

        val columnView = LinearLayout(context)
        columnView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        if (grid.orientation == LinearLayout.VERTICAL)
            columnView.orientation = LinearLayout.HORIZONTAL
        else
            columnView.orientation = LinearLayout.VERTICAL

        val cell = createEmptyCell()
        cell.setBackgroundResource(R.color.colorPrimaryLight)
        cell.setText(NAMES[width-1])
        cell.setTextColor(resources.getColor(R.color.colorBackContrast))

        cell.isLongClickable = false
        cell.isCursorVisible = false
        cell.isFocusable = false

        columnView.addView(cell)

        for (i in 1 until height) {
            columnView.addView(createEmptyCell())
        }

        grid.addView(columnView, width-1)
    }

    private fun onClickDelColumn() {
        grid.removeViewAt(grid.childCount-2)
        width--
    }

    private fun onClickAddRow() {
        for (i in 0 until grid.childCount) {
            (grid.getChildAt(i) as LinearLayout).addView(createEmptyCell())
        }
        height++
    }

    private fun onClickDelRow() {
        for (i in 0 until grid.childCount) {
            (grid.getChildAt(i) as LinearLayout).removeViewAt((grid.getChildAt(i) as LinearLayout).childCount - 1)
        }
        height--
    }

    private fun rotate90Degrees() {
        if (grid.orientation == LinearLayout.VERTICAL) {
            grid.layoutParams.width = 0
            grid.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            grid.orientation = LinearLayout.HORIZONTAL
            for (i in 0 until width)
                (grid.getChildAt(i) as LinearLayout).orientation = LinearLayout.VERTICAL
        } else {
            grid.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            grid.layoutParams.height = 0
            grid.orientation = LinearLayout.VERTICAL
            for (i in 0 until width)
                (grid.getChildAt(i) as LinearLayout).orientation = LinearLayout.HORIZONTAL
        }
    }
}
