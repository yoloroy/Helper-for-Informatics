package e.pmart.project


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_symmetry_matrix.*


class SymmetryMatrixFragment : Fragment() {
    var width = 2
    var height = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_symmetry_matrix, container, false)
    }

    override fun onStart() {
        super.onStart()

        createOnClicks()
    }

    private fun createOnClicks() {
        add.setOnClickListener {
            onClickAddColumn()
            onClickAddRow()
        }
        del.setOnClickListener {
            if (width > 2) onClickDelColumn()
            if (height > 2) onClickDelRow()
        }
    }

    private fun createEmptyCell(): EditText {
        val cell = EditText(context)
        cell.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        cell.inputType = InputType.TYPE_CLASS_NUMBER
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
        val columnView = LinearLayout(context)
        columnView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        if (grid.orientation == LinearLayout.VERTICAL)
            columnView.orientation = LinearLayout.HORIZONTAL
        else
            columnView.orientation = LinearLayout.VERTICAL

        val cell = createEmptyCell()
        cell.setBackgroundResource(R.color.colorPrimaryLight)
        cell.setText(width.toString())
        cell.setTextColor(resources.getColor(R.color.colorBackContrast))

        cell.isLongClickable = false
        cell.isCursorVisible = false
        cell.isFocusable = false

        columnView.addView(cell)

        for (i in 1 until height) {
            columnView.addView(createEmptyCell())
        }

        grid.addView(columnView)

        width++
    }

    private fun onClickDelColumn() {
        grid.removeViewAt(grid.childCount-1)
        width--
    }

    private fun onClickAddRow() {
        var cell = createEmptyCell()
        cell.setBackgroundResource(R.color.colorPrimaryLight)
        cell.setText(height.toString())
        cell.setTextColor(resources.getColor(R.color.colorBackContrast))

        cell.isLongClickable = false
        cell.isCursorVisible = false
        cell.isFocusable = false

        (grid.getChildAt(0) as LinearLayout).addView(cell)

        for (i in 1 until grid.childCount-1) {
            (grid.getChildAt(i) as LinearLayout).addView(createEmptyCell())
        }

        cell = createEmptyCell()

        cell.setBackgroundColor(resources.getColor(R.color.colorBackground))
        cell.setText("")

        cell.isLongClickable = false
        cell.isCursorVisible = false
        cell.isFocusable = false

        (grid.getChildAt(grid.childCount-1) as LinearLayout).addView(cell)
        height++
    }

    private fun onClickDelRow() {
        for (i in 0 until grid.childCount) {
            (grid.getChildAt(i) as LinearLayout).removeViewAt((grid.getChildAt(i) as LinearLayout).childCount - 1)
        }
        height--
    }
}
