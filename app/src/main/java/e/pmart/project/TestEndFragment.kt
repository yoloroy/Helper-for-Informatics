package e.pmart.project


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_test_end.*


class TestEndFragment : Fragment() {
    private var nums = ArrayList<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_end, container, false)
    }

    override fun onStart() {
        setStats()
        end_test.setOnClickListener { onClickEndTest() }

        super.onStart()
    }

    fun addAnswered(num: Int) {
        if (!nums.contains(num)) {
            nums.add(num)
            setStats()
        }
    }

    fun delAnswered(num: Int) {
        if (nums.contains(num)) {
            nums.minusAssign(num)
            setStats()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setStats() {
        answered?.text = "№ ${nums.sorted().joinToString("\n№ ")}"
        non_answered?.text = "№ ${(( 1..23 ).toList() - nums).sorted().joinToString("\n№ ")}"
    }

    private fun onClickEndTest() {
        val correct = ArrayList<Int>()
        val incorrect = ArrayList<Int>()
        val not_resolved = ArrayList<Int>()

        (activity as MainActivity).mode_fragments["test"]!!.also {
            val temp = ArrayList<Fragment>()

            it.slice(0 until it.size-1).forEachIndexed { index, task ->
                (task as FactoryEducationFragment)
                if (task.checkAnswer()) {
                    task.delTask(1)
                            .newText("Верно")
                            .newTitle("Ответ: ${task.answer}")
                    correct += index+1
                } else if (task.enter.text.isBlank()) {
                    task.delTask(1)
                            .newText("Неверно")
                            .newTitle("Ответ: ${task.answer}")
                            .newTitle("Вы не дали ответа")
                    not_resolved += index + 1
                } else {
                    task.delTask(1)
                            .newText("Неверно")
                            .newTitle("Ответ: ${task.answer}")
                            .newTitle("Ваш ответ: \"${task.enter.text}\"")
                    incorrect += index+1
                }
                task.answer = null
                temp.add(task)
            }
            (activity as MainActivity).mode_fragments["test_end"] = listOf(FactoryEducationFragment()
                    .newTitle("Решено верно: ${correct.size}")
                    .newText("№ ${correct.joinToString("\n№ ")}")
                    .newTitle("Решено неверно: ${incorrect.size}")
                    .newText("№ ${incorrect.joinToString("\n№ ")}")
                    .newTitle("Не решено: ${not_resolved.size}")
                    .newText("№ ${not_resolved.joinToString("\n№ ")}")
            ) + temp
            (activity as MainActivity).actionBarNames["test"]?.also { names ->
                (activity as MainActivity).actionBarNames["test_end"] =
                        listOf("Итоги") + names.slice(0 until names.size-1)
            }

            (activity as MainActivity).changeMode("test_end")
        }
    }
}
