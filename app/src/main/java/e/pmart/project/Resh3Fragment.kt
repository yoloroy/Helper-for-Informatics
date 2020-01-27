package e.pmart.project


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import de.blox.graphview.Graph
import de.blox.graphview.Node
import kotlinx.android.synthetic.main.fragment_resh3.*
import kotlinx.android.synthetic.main.fragment_symmetry_matrix.*
import kotlinx.android.synthetic.main.fragment_test.*


class Resh3Fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh3, container, false)
    }

    override fun onStart() {
        super.onStart()

        createOnClicks()
    }

    private fun createOnClicks() {
        resh3_evaluate.setOnClickListener { onClickRun() }
    }

    private fun onClickRun() {
        val runnable = Runnable {
            val matrix1 = getMatrix()
            val matrix2 = getTreeMatrix()


        }

        Thread(runnable).start()
    }

    private fun getTreeMatrix(): ArrayList<ArrayList<Boolean>> {
        val graph = graphView.adapter.graph
        val matrix: ArrayList<ArrayList<Boolean>> = ArrayList()

        for (i in graph.nodes.sortedBy { it.data.toString() }) {
            matrix.add(ArrayList())
            for (j in graph.nodes.sortedBy { it.data.toString() }) {
                matrix.last().add(hasConnection(i, j, graph))
            }
        }

        return matrix
    }

    private fun hasConnection(node1: Node, node2: Node, graph: Graph): Boolean {
        return (node1 in graph.predecessorsOf(node2)) or (node2 in graph.predecessorsOf(node1))
    }

    private fun getMatrix(): ArrayList<ArrayList<String>> {
        val table = ArrayList<ArrayList<String>>()
        for (x in 1 until grid.childCount) {
            table.add(getByX(x))
        }
        return table
    }

    private fun getByX(x: Int): ArrayList<String> {
        val column = ArrayList<String>()
        for (y in 1 until (grid.getChildAt(0) as LinearLayout).childCount) {
            try {
                column.add(get(x, y))
            } catch (e: Exception) {
                // pass
            }
        }
        return column
    }

    private fun get(x: Int, y: Int): String {
        return ((grid.getChildAt(x) as LinearLayout).getChildAt(y) as EditText).text.toString()
    }
}
