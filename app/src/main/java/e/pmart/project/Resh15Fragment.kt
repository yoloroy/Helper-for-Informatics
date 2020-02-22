package e.pmart.project


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.otaliastudios.zoom.ZoomApi
import de.blox.graphview.*
import de.blox.graphview.layered.SugiyamaAlgorithm
import kotlinx.android.synthetic.main.fragment_resh15.*


class Resh15Fragment : Fragment() {
    private var nodeCount = 2
    private var clicked_nodes: ArrayList<Any> = ArrayList()
    private var clicked_views: ArrayList<ViewHolder> = ArrayList()

    private val nodesData = arrayListOf(1, 1)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh15, container, false)
    }

    override fun onStart() {
        graphView.adapter = SimpleGraphAdapter()

        graphView.adapter.graph = Graph()
        val temp = Node(1)
        graphView.adapter.graph.addEdge(Node(0), temp)
        graphView.adapter.algorithm = SugiyamaAlgorithm()

        graphView.setMaxZoom(1f, ZoomApi.MAX_ZOOM_DEFAULT_TYPE)
        graphView.setMinZoom(1f, ZoomApi.MIN_ZOOM_DEFAULT_TYPE)

        graphView.refreshDrawableState()
        graphView.adapter.notifySizeChanged()

        createOnClicks()

        super.onStart()
    }

    private fun createOnClicks() {
        add.setOnClickListener{
            val graph = graphView.adapter.graph
            val node = Node(nodeCount)
            if (clicked_nodes.size == 1) {
                graph.addEdge(searchNodeByData(clicked_nodes[0]), node)
                clicked_nodes.clear()
            } else {
                try {
                    graph.addEdge(searchNodeByData(0), node)
                } catch (e: ArrayIndexOutOfBoundsException) {
                    // певрой ноды нет?
                    graph.addEdge(Node(0), node)
                }
            }
            nodeCount ++
            nodesData.add(0)
            graphView.adapter.graph = graph
            graphView.adapter.notifyInvalidated()
        }
    }

    fun searchNodeByData(data: Any, nodes: List<Node>): Node {
        return nodes[nodes.binarySearchBy(data.toString()) { it.data.toString() }]
    }

    fun searchNodeByData(data: Any): Node {
        return graphView.adapter.graph.nodes[graphView.adapter.graph.nodes.binarySearchBy(data.toString()) { it.data.toString() }]
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
            (viewHolder as SimpleViewHolder)
            // соединяем/разъединяем
            viewHolder.textView.setOnClickListener {
                clicked_nodes.add(data)
                clicked_views.add(viewHolder)
                viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimaryLight))
                viewHolder.textView.refreshDrawableState()

                if (clicked_nodes.size == 2) {
                    add.background.setTint(resources.getColor(android.R.color.darker_gray))
                    add.isClickable = false

                    if (clicked_nodes[0] == clicked_nodes[1]) {
                        clicked_nodes.clear()
                        viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimary))
                        return@setOnClickListener
                    }

                    try {
                        val edge = Edge(searchNodeByData(clicked_nodes[0]),
                                searchNodeByData(clicked_nodes[1]))

                        if (searchNodeByData(clicked_nodes[1]) in graph.predecessorsOf(searchNodeByData(clicked_nodes[0])))
                            graph.removeEdge(searchNodeByData(clicked_nodes[1]),
                                    searchNodeByData(clicked_nodes[0]))
                        else if (searchNodeByData(clicked_nodes[0]) in graph.predecessorsOf(searchNodeByData(clicked_nodes[1])))
                            graph.removeEdge(searchNodeByData(clicked_nodes[0]),
                                    searchNodeByData(clicked_nodes[1]))
                        else
                            graph.addEdge(edge)

                        viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimary))
                        (graphView.adapter.getItem(clicked_nodes[0] as Int) as Resh3GraphFragment.SimpleViewHolder).textView.background.setTint(resources.getColor(R.color.colorPrimary))
                    } catch (aioobe: Exception) {}
                    clicked_nodes.clear()
                    clicked_views.clear()
                    notifyInvalidated()
                } else {
                    add.background.setTint(resources.getColor(R.color.colorCalcMain))
                    add.isClickable = true
                }
            }
            // удаляем
            viewHolder.textView.setOnLongClickListener {
                if (data as Int != 0)
                    graph.removeNode(searchNodeByData(data.toString(), graph.nodes))
                true
            }

            notifyInvalidated()
            viewHolder.textView.text = getItem(position).toString()
        }

        override fun getItem(position: Int): Any {
            return nodesData[position]
        }

        // обновление выводимых чисел
        override fun notifyInvalidated() {
            for (i in 1 until nodeCount) {
                findNum(i)
            }

            super.notifyInvalidated()
        }

        // поиск числа
        private fun findNum(position: Int) {
            nodesData[position] = 0
            for (i in graph.predecessorsOf(Node(position))) {
                if (nodesData[i.data as Int] == 0)
                    findNum(i.data as Int)

                nodesData[position] += nodesData[i.data as Int]
            }
        }
    }
}
