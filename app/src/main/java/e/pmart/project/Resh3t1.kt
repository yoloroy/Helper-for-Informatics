package e.pmart.project

class Resh3t1 {
    private fun swap(a: ArrayList<ArrayList<Int>>, x: Int, y: Int): ArrayList<ArrayList<Int>> {
        val b: ArrayList<ArrayList<Int>> = ArrayList()
        for (i in 0 until a.size) {
            b.add(ArrayList())
            for (j in 0 until a.size) {
                b.last().add(a
                        [if (i == x) y else if (i == y) x else i]
                        [if (j == x) y else if (j == y) x else j]
                )
            }
        }
        return b
    }

    private fun check(a: ArrayList<ArrayList<Int>>, b: ArrayList<ArrayList<Int>>): Boolean {
        for (i in 0 until a.size) {
            for (j in 0 until a.size) {
                if (!((a[i][j] - b[i][j] == 0) || (a[i][j] > 0 && b[i][j] > 0))) {
                    return false
                }
            }
        }
        return true
    }

    fun run(a: ArrayList<ArrayList<Int>>, bb: ArrayList<ArrayList<Int>>): ArrayList<Int> {

        val x = ArrayList<Int>()
        var b = bb

        for (i in 0 until a.size) {
            x.add(i)
        }

        while (true) {
            if (check(a, b)) break;
            var j = 0
            for (i in 0 until x.size - 1) {
                if (x[i] < x[i + 1]) {
                    j = i
                }
            }
            var l = 0
            for (i in j + 1 until x.size) {
                if (x[i] > x[j]) {
                    l = i
                }
            }
            if (j == 0 && l == 0) break;
            x[j] = x[l].also { x[l] = x[j] }
            b = swap(b, j, l)
            for (r in 0 until (b.size - j + 1) / 2) {
                x[r + j + 1] = x[b.size - 1 - r].also { x[b.size - 1 - r] = x[r + j + 1] }
                b = swap(b, r + j + 1, b.size - 1 - r);
            }
        }
        if (!check(a, b)) throw Exception("Что-то не сходится")

        return x
    }

}
