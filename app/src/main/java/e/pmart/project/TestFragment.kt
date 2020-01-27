package e.pmart.project


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.otaliastudios.zoom.ZoomApi
import de.blox.graphview.*
import de.blox.graphview.energy.FruchtermanReingoldAlgorithm
import kotlinx.android.synthetic.main.fragment_test.*


class TestFragment : Fragment() {
    private val NAMES = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я")
    private var nodeCount = 0
    private var clicked_nodes: ArrayList<Any> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onStart() {
        super.onStart()

        graphView.adapter = SimpleGraphAdapter()

        graphView.adapter.algorithm = FruchtermanReingoldAlgorithm()
        graphView.adapter.algorithm.setEdgeRenderer(NoArrowEdgeRenderer())
        graphView.adapter.graph = Graph()

        createOnClicks()
    }

    private fun createOnClicks() {
        test_graph_add.setOnClickListener {
            graphView.adapter.graph.addNode(Node(NAMES[nodeCount]))
            graphView.adapter.notifyInvalidated()
            graphView.destroyDrawingCache()
            graphView.refreshDrawableState()
            nodeCount++
        }
    }

    fun searchNodeByData(data: Any, nodes: List<Node>): Node {
        return nodes[nodes.binarySearchBy(data.toString()) { it.data.toString() }]
    }

    internal inner class SimpleViewHolder(var view: View) : ViewHolder(view) {
        val textView: TextView
        var pos = -1

        init {
            pos = nodeCount
            textView = view.findViewById(R.id.textView)
        }
    }

    internal inner class SimpleGraphAdapter : BaseGraphAdapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.node_char, parent, false)
            return SimpleViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, data: Any, position: Int) {
            (viewHolder as SimpleViewHolder).textView.text = data.toString()
            viewHolder.textView.setOnClickListener {
                clicked_nodes.add(data.toString())
                viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimaryLight))
                viewHolder.textView.refreshDrawableState()

                if (clicked_nodes.size == 2) {
                    test_graph_end.visibility = View.GONE
                    test_graph_start.visibility = View.GONE

                    test_graph_end.scaleX = 0f
                    test_graph_end.scaleY = 0f
                    test_graph_start.scaleX = 0f
                    test_graph_start.scaleY = 0f

                    textGraphEnd.visibility = View.GONE
                    textGraphStart.visibility = View.GONE

                    textGraphEnd.scaleX = 0f
                    textGraphEnd.scaleY = 0f
                    textGraphStart.scaleX = 0f
                    textGraphStart.scaleY = 0f

                    if (clicked_nodes[0] == clicked_nodes[1]) {
                        clicked_nodes.clear()
                        viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimary))
                        return@setOnClickListener
                    }

                    try {
                        val edge = Edge(searchNodeByData(clicked_nodes[0], graph.nodes),
                                searchNodeByData(clicked_nodes[1], graph.nodes))

                        if (searchNodeByData(clicked_nodes[1], graph.nodes) in graph.predecessorsOf(searchNodeByData(clicked_nodes[0], graph.nodes)))
                            graph.removeEdge(searchNodeByData(clicked_nodes[1], graph.nodes),
                                    searchNodeByData(clicked_nodes[0], graph.nodes))
                        else if (searchNodeByData(clicked_nodes[0], graph.nodes) in graph.predecessorsOf(searchNodeByData(clicked_nodes[1], graph.nodes)))
                            graph.removeEdge(searchNodeByData(clicked_nodes[0], graph.nodes),
                                    searchNodeByData(clicked_nodes[1], graph.nodes))
                        else
                            graph.addEdge(edge)

                        viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimary))
                        (graphView.adapter.getItem(clicked_nodes[0] as Int) as SimpleViewHolder).textView.background.setTint(resources.getColor(R.color.colorPrimary))
                    } catch (aioobe: Exception) {}
                    clicked_nodes.clear()
                    notifyInvalidated()
                } else {
                    test_graph_end.visibility = View.VISIBLE
                    test_graph_start.visibility = View.VISIBLE
                    test_graph_end.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                    test_graph_start.animate().scaleX(1f).scaleY(1f).setDuration(100).start()

                    textGraphEnd.visibility = View.VISIBLE
                    textGraphStart.visibility = View.VISIBLE
                    textGraphEnd.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                    textGraphStart.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                }
            }
            viewHolder.textView.setOnLongClickListener {
                graph.removeNode(searchNodeByData(data.toString(), graph.nodes))
                //notifyNodeRemoved(searchNodeByData(data.toString(), graph.nodes))
                true
            }

            graphView.engine.setTransformation(ZoomApi.TRANSFORMATION_CENTER_INSIDE, Gravity.CENTER);
        }
    }
}
