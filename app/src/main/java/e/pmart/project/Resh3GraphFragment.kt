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
import kotlinx.android.synthetic.main.fragment_resh3_graph.*


class Resh3GraphFragment : Fragment() {
    private val NAMES = arrayOf("А", "Б", "В", "Г", "Д", "Е", "Ж", "З", "И", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ъ", "Ы", "Ь", "Э", "Ю", "Я")
    private var nodeCount = 0
    private var clicked_nodes: ArrayList<Any> = ArrayList()
    private var clicked_views: ArrayList<ViewHolder> = ArrayList()
    private var start_data = ""
    private var start_view = Any()
    private var end_data = ""
    private var end_view = Any()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resh3_graph, container, false)
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
        graph_add.setOnClickListener {
            graphView.adapter.graph.addNode(Node(NAMES[nodeCount]))
            graphView.adapter.notifyInvalidated()
            graphView.destroyDrawingCache()
            graphView.refreshDrawableState()
            nodeCount++
        }

        graph_refresh.setOnClickListener {
            graphView.adapter.notifyNodeAdded(Node("refresh"))
            graphView.adapter.notifyNodeRemoved(Node("refresh"))
        }

        graph_start.setOnClickListener {
            if (clicked_views.size == 0)
                return@setOnClickListener
            if (start_data != "")
                (start_view as SimpleViewHolder).textView.text = start_data
            start_data = clicked_nodes[0].toString()
            start_view = clicked_views[0]
            (start_view as SimpleViewHolder).textView.text = "S"
            (start_view as SimpleViewHolder).textView.background.setTint(resources.getColor(R.color.colorPrimary))
            clicked_nodes.clear()
            clicked_views.clear()
        }

        graph_end.setOnClickListener {
            if (clicked_views.size == 0)
                return@setOnClickListener
            if (end_data != "")
                (end_view as SimpleViewHolder).textView.text = end_data
            end_data = clicked_nodes[0].toString()
            end_view = clicked_views[0]
            (end_view as SimpleViewHolder).textView.text = "Ɛ"
            (end_view as SimpleViewHolder).textView.background.setTint(resources.getColor(R.color.colorPrimary))
            clicked_nodes.clear()
            clicked_views.clear()
        }
    }

    fun Boolean.toInt() = if (this) 1 else 0

    private fun nodesConnected(node1: Node, node2: Node, graph: Graph): Boolean {
        return (node1 in graph.predecessorsOf(node2)) or
               (node2 in graph.predecessorsOf(node1))
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
            textView.background.setTint(resources.getColor(R.color.colorPrimary))
            textView.setTextColor(resources.getColor(android.R.color.white))
        }
    }

    internal inner class SimpleGraphAdapter : BaseGraphAdapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.node_char, parent, false)
            return SimpleViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, data: Any, position: Int) {
            (viewHolder as SimpleViewHolder).textView.text = if (data.toString() == start_data) "S" else if (data.toString() == end_data) "Ɛ" else data.toString()
            viewHolder.textView.setOnClickListener {
                clicked_nodes.add(data.toString())
                clicked_views.add(viewHolder)
                viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimaryLight))
                viewHolder.textView.refreshDrawableState()

                if (clicked_nodes.size == 2) {
                    graph_end.setColorFilter(resources.getColor(android.R.color.darker_gray))
                    graph_start.setColorFilter(resources.getColor(android.R.color.darker_gray))

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
                    clicked_views.clear()
                    notifyInvalidated()
                } else {
                    graph_end.setColorFilter(resources.getColor(R.color.colorCalcMain))
                    graph_start.setColorFilter(resources.getColor(R.color.colorCalcMain))
                }
            }
            viewHolder.textView.setOnLongClickListener {
                graph.removeNode(searchNodeByData(data.toString(), graph.nodes))
                true
            }

            graphView.engine.setTransformation(ZoomApi.TRANSFORMATION_CENTER_INSIDE, Gravity.CENTER);
        }

        override fun getItem(position: Int): Any {
            return if (super.getItem(position) == start_data) "S" else if (super.getItem(position) == end_data) "Ɛ" else super.getItem(position)
        }
    }
}
