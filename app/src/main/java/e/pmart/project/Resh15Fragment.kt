package e.pmart.project


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
        createLangController()
        createGraphView()
        createOnClicks()

        super.onStart()
    }

    private fun createGraphView() {
        graphView.adapter = SimpleGraphAdapter()
        graphView.adapter.graph = Graph()

        val temp = Node(1)
        graphView.adapter.graph.addEdge(Node(0), temp)
        graphView.adapter.algorithm = SugiyamaAlgorithm()

        graphView.setMaxZoom(1f, ZoomApi.MAX_ZOOM_DEFAULT_TYPE)
        graphView.setMinZoom(1f, ZoomApi.MIN_ZOOM_DEFAULT_TYPE)

        graphView.refreshDrawableState()
        graphView.adapter.notifySizeChanged()
    }

    private fun createLangController() {
        ArrayAdapter.createFromResource(
                context,
                R.array.languages,
                android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            lang_controller.adapter = it
        }
        lang_controller.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                graphView.adapter.notifyInvalidated()
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
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

    fun searchNodeByData(data: Any): Node {
        for (x in graphView.adapter.graph.nodes)
            if (x.data.toString() == data.toString()) return x

        throw IndexOutOfBoundsException()

       // return graphView.adapter.graph.nodes[graphView.adapter.graph.nodes.binarySearchBy(data.toString()) { it.data.toString() }]
    }

    fun getChar(pos: Int): Char {
        when (lang_controller.selectedItem) {
            "Русский" -> return getString(R.string.lang_ru)[pos]
            "Український" -> return getString(R.string.lang_ua)[pos]
            "Қазақ" -> return getString(R.string.lang_kz)[pos]
            "English" -> return getString(R.string.lang_en)[pos]
        }
        throw IndexOutOfBoundsException()
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
                if (data as Int != 0) {
                    graph.removeNode(searchNodeByData(data.toString()))
                }

                true
            }

            notifyInvalidated()
            viewHolder.textView.text = String.format("%s, %s", getChar(data as Int), getItem(data as Int).toString())
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
                if (nodesData[i.data as Int] == 0) {
                    try {
                        findNum(i.data as Int)
                    } catch (e: Exception) {}
                }

                nodesData[position] += nodesData[i.data as Int]
            }
        }
    }
}
