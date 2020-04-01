package e.pmart.project.solvers.faces


import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import de.blox.graphview.Graph
import de.blox.graphview.Node
import e.pmart.project.Deikstra
import e.pmart.project.R
import e.pmart.project.solvers.Resh3t1
import kotlinx.android.synthetic.main.fragment_resh3.*
import kotlinx.android.synthetic.main.fragment_resh3_graph.*
import kotlinx.android.synthetic.main.fragment_symmetry_matrix.*


class Resh3Fragment : Fragment() {
    private val ALPHABET = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh3, container, false)
    }

    override fun onStart() {
        super.onStart()

        resh3_num_type.adapter = ArrayAdapter<String>(context,
                R.layout.simple_scalable_list_item_1,
                listOf("Однозначное соотнесение таблицы и графа"))

        resh3_ans_type.adapter = ArrayAdapter<String>(context,
                R.layout.simple_scalable_list_item_1,
                listOf("Найти кратчайший путь"))

        createOnClicks()
    }

    private fun createOnClicks() {
        resh3_evaluate.setOnClickListener { onClickRun() }
    }

    private fun onClickRun() {
        val matrix1 = getMatrix()
        val matrix2 = getTreeMatrix()
        if (matrix1.size != matrix2.size) {
            Snackbar.make(view!!, "Проверьте правильность таблицы или графа", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            return
        }

        val names = graphView.adapter.graph.nodes.map {it.data.toString()}
        val fromto: ArrayList<Int>;
        try {
            fromto = getFromTo()
        } catch (e: IndexOutOfBoundsException) {
            if (e.message == "start")
                Snackbar.make(view!!, "Пожалуйста, выберите точку старта", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
            if (e.message == "end")
                Snackbar.make(view!!, "Пожалуйста, выберите точку конца", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
            return
        }


        try {
            val answer = Resh3t1().run(matrix2, matrix1)

            resh3_matrix.removeAllViews()

            var rowView = LinearLayout(context)
            rowView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            rowView.orientation = LinearLayout.HORIZONTAL

            resh3_matrix.addView(rowView)

            var cell = createEmptyCell()
            cell.setBackgroundColor(resources.getColor(android.R.color.widget_edittext_dark))
            (resh3_matrix.getChildAt(0) as LinearLayout).addView(cell)

            var temp = -1
            for (i in answer) {
                temp++
                cell = createEmptyCell()
                cell.text = names[temp]
                cell.setBackgroundColor(resources.getColor(android.R.color.widget_edittext_dark))
                cell.setTextColor(resources.getColor(android.R.color.secondary_text_dark))
                (resh3_matrix.getChildAt(0) as LinearLayout).addView(cell)
            }

            temp = -1
            val ribs = ArrayList<ArrayList<Int>>()
            for (i in answer) {
                temp++
                rowView = LinearLayout(context)
                rowView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
                rowView.orientation = LinearLayout.HORIZONTAL

                cell = createEmptyCell()
                cell.setBackgroundColor(resources.getColor(android.R.color.widget_edittext_dark))
                cell.setTextColor(resources.getColor(android.R.color.secondary_text_dark))
                cell.text = names[temp]
                rowView.addView(cell)
                ribs.add(ArrayList())
                for (j in answer) {
                    cell = createEmptyCell()

                    if (j == i) cell.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                    else cell.text = matrix1[i][j].toString()
                    ribs.last().add(matrix1[i][j])
                    rowView.addView(cell)
                }
                resh3_matrix.addView(rowView)
            }

            resh3_answer.text = Deikstra().initialize(graphView.adapter.graph.nodeCount,
                                                      graphView.adapter.graph.edges.size,
                                                      ribs, fromto[0], fromto[1]).main().toString()
        /*} catch (e: IndexOutOfBoundsException) {
            Snackbar.make(view!!, "Количества элментов таблицы и графа не совпадают", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()*/
        } catch (e: Exception) {
            throw e
            Log.e("e", e.stackTrace.toString())
            Snackbar.make(view!!, e.message!!, Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
        }
    }

    private fun getFromTo(): ArrayList<Int> {
        for (i in 0 until graphView.adapter.count) {
            if (graphView.adapter.getItem(i) == "S") {
                for (j in 0 until graphView.adapter.count) {
                    if (graphView.adapter.getItem(j) == "Ɛ")
                        return arrayListOf(i, j)
                }
                throw IndexOutOfBoundsException("end")
            }
        }
        throw IndexOutOfBoundsException("start")
    }

    private fun getTreeMatrix(): ArrayList<ArrayList<Int>> {
        val graph = graphView.adapter.graph
        val matrix: ArrayList<ArrayList<Int>> = ArrayList()

        for (i in graph.nodes.sortedBy { it.data.toString() }) {
            Log.i("ggg", i.data.toString())
            matrix.add(ArrayList())
            for (j in graph.nodes.sortedBy { it.data.toString() }) {
                matrix.last().add(hasConnection(i, j, graph).toInt())
            }
        }

        return matrix
    }

    fun Boolean.toInt() = if (this) 1 else 0

    private fun hasConnection(node1: Node, node2: Node, graph: Graph): Boolean {
        return (node1 in graph.predecessorsOf(node2)) or
               (node2 in graph.predecessorsOf(node1))
    }

    private fun createEmptyCell(): TextView {
        val cell = TextView(context)
        cell.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        cell.setTextColor(resources.getColor(R.color.colorCalcMain))
        cell.setText("")
        cell.setEms(1)
        cell.textAlignment = EditText.TEXT_ALIGNMENT_CENTER
        cell.setPadding(2, 0, 2, 0)
        cell.setBackgroundResource(R.color.colorBackContrast)
        (cell.layoutParams as LinearLayout.LayoutParams).marginEnd = 1
        (cell.layoutParams as LinearLayout.LayoutParams).bottomMargin = 1

        return cell
    }

    private fun getMatrix(): ArrayList<ArrayList<Int>> {
        val table = ArrayList<ArrayList<Int>>()
        for (x in 1 until grid.childCount) {
            table.add(getByX(x))
        }
        return table
    }

    private fun getByX(x: Int): ArrayList<Int> {
        val column = ArrayList<Int>()
        for (y in 1 until (grid.getChildAt(0) as LinearLayout).childCount) {
            try {
                column.add(get(x, y))
            } catch (e: Exception) {
                column.add(0)
            }
        }
        return column
    }

    private fun get(x: Int, y: Int): Int {
        return ((grid.getChildAt(x) as LinearLayout).getChildAt(y) as EditText).text.toString().toInt()
    }
}
