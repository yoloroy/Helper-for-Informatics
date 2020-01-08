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
import kotlinx.android.synthetic.main.fragment_appendable_dicts_table.*


class AppendableDictsTableFragment : Fragment() {
    val NAMES = arrayOf("x", "y", "z", "w")
    var width = 1
    var height = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_appendable_dicts_table, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createGrid()
        createOnClicks()
    }

    private fun createGrid() {
        val columnView = LinearLayout(context)
        columnView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        columnView.orientation = LinearLayout.VERTICAL

        val cell = createEmptyCell()
        cell.setBackgroundResource(R.color.colorPrimaryLight)
        cell.setText(NAMES[width-1])
        cell.setTextColor(resources.getColor(R.color.colorBackContrast))

        cell.isLongClickable = false
        cell.isCursorVisible = false
        cell.isFocusable = false

        columnView.addView(cell)
        columnView.addView(createEmptyCell())

        grid.addView(columnView, 0)
    }

    private fun createOnClicks() {
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

    fun getMatrix(): ArrayList<ArrayList<String>> {
        val table = ArrayList<ArrayList<String>>()
        for (x in 0 until width) {
            table.add(getByX(x))
        }
        return table
    }

    fun getByX(x: Int): ArrayList<String> {
        val column = ArrayList<String>()

        Log.i(grid.childCount.toString(), width.toString())
        Log.i((grid.getChildAt(x) as LinearLayout).childCount.toString(), height.toString())
        for (y in 0 until height)
            column.add(get(x, y))
        return column
    }

    fun getByY(y: Int): ArrayList<String> {
        val row = ArrayList<String>()
        for (x in 0 until width)
            row.add(get(x, y))
        return row
    }

    fun get(x: Int, y: Int): String {
        return ((grid.getChildAt(x) as LinearLayout).getChildAt(y) as EditText).text.toString()
    }
}
