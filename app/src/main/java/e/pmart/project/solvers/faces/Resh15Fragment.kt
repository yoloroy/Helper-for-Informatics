package e.pmart.project.solvers.faces


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
import e.pmart.project.R
import e.pmart.project.solvers.Resh3GraphFragment
import kotlinx.android.synthetic.main.fragment_resh15.*


class Resh15Fragment : Fragment() {
    private var ALPHABET_RU = "RU"
    private var ALPHABET_EN = "EN"
    private var ALPHABET_UA = "UA"
    private var ALPHABET_KZ = "KZ"
    private val LOWER_NUMS = "₀₁₂₃₄₅₆₇₈₉"

    /*
    * 0 - русский
    * 1 - украинский
    * 2 - казахский
    * 3 - английский
    * */
    var curr_lang = 0

    private var nodeCount = 2
    private var clickedNodes: ArrayList<Any> = ArrayList()
    private var clickedViews: ArrayList<ViewHolder> = ArrayList()

    private val nodesData = arrayListOf(1, 1)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resh15, container, false)
    }

    override fun onStart() {
        createAlphabets()
        createLangController()
        createGraphView()
        createOnClicks()

        super.onStart()
    }

    fun createAlphabets() {
        ALPHABET_RU = getString(R.string.lang_ru)
        ALPHABET_EN = getString(R.string.lang_en)
        ALPHABET_UA = getString(R.string.lang_ua)
        ALPHABET_KZ = getString(R.string.lang_kz)
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
                curr_lang = position
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
            if (clickedNodes.size == 1) {
                graph.addEdge(searchNodeByData(clickedNodes[0]), node)
                clickedNodes.clear()
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
            "Русский" -> return ALPHABET_RU[pos]
            "Український" -> return ALPHABET_UA[pos]
            "Қазақ" -> return ALPHABET_KZ[pos]
            "English" -> return ALPHABET_EN[pos]
        }
        throw IndexOutOfBoundsException()
    }

    fun getNumber(pos: Int): String {
        val alphabet_len = arrayOf(ALPHABET_RU, ALPHABET_UA, ALPHABET_KZ, ALPHABET_EN)[curr_lang].length
        if (alphabet_len <= pos)
            return String.format("%s%s", getChar(pos % alphabet_len),
                                                    LOWER_NUMS[pos / alphabet_len])
        else
            return getChar(pos).toString()
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.node_line, parent, false)
            return SimpleViewHolder(view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, data: Any, position: Int) {
            (viewHolder as SimpleViewHolder)
            // соединяем/разъединяем
            viewHolder.textView.setOnClickListener {
                clickedNodes.add(data)
                clickedViews.add(viewHolder)
                viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimaryLight))
                viewHolder.textView.refreshDrawableState()

                if (clickedNodes.size == 2) {
                    add.background.setTint(resources.getColor(android.R.color.darker_gray))
                    add.isClickable = false

                    if (clickedNodes[0] == clickedNodes[1]) {
                        clickedNodes.clear()
                        viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimary))
                        return@setOnClickListener
                    }

                    try {
                        val edge = Edge(searchNodeByData(clickedNodes[0]),
                                searchNodeByData(clickedNodes[1]))

                        if (searchNodeByData(clickedNodes[1]) in graph.predecessorsOf(searchNodeByData(clickedNodes[0])))
                            graph.removeEdge(searchNodeByData(clickedNodes[1]),
                                    searchNodeByData(clickedNodes[0]))
                        else if (searchNodeByData(clickedNodes[0]) in graph.predecessorsOf(searchNodeByData(clickedNodes[1])))
                            graph.removeEdge(searchNodeByData(clickedNodes[0]),
                                    searchNodeByData(clickedNodes[1]))
                        else
                            graph.addEdge(edge)

                        viewHolder.textView.background.setTint(resources.getColor(R.color.colorPrimary))
                        (graphView.adapter.getItem(clickedNodes[0] as Int) as Resh3GraphFragment.SimpleViewHolder).textView.background.setTint(resources.getColor(R.color.colorPrimary))
                    } catch (aioobe: Exception) {}
                    clickedNodes.clear()
                    clickedViews.clear()
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
            viewHolder.textView.text = String.format("%s, %s", getNumber(data as Int), getItem(data).toString())
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
