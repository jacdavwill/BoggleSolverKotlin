import java.io.File
import kotlin.math.sqrt

/*
  S E R S
  P A T G
  L I N E
  S E R S
*/
@ExperimentalStdlibApi
class BoggleTrie(wordlistSource: String) {
    private lateinit var board: List<Char>
    private val baseNode = Node()
    private var row = 0
    private lateinit var found: MutableSet<String>
    private lateinit var adjList: List<List<Int>>
    var sum1: Long = 0
    var sum2: Long = 0
    var sum3: Long = 0
    var sum4: Long = 0
    var count1 = 0

    init {
        File(wordlistSource).readLines().forEach { word ->
            if (word.length in 3..15) {
                var currNode = baseNode
                word.forEach { letter ->
                    currNode.value++
                    currNode = currNode.childNodes[letter] ?: let {
                        Node().also {
                            currNode.childNodes[letter] = it
                        }
                    }
                }
                currNode.word = word
            }
        }
    }

    class Node {
        var value = 0
        var word: String? = null
        val childNodes = HashMap<Char, Node?>()
    }

    fun solve(board: List<Char>): List<String> {
        this.found = hashSetOf()
        this.board = board
        row = sqrt(board.size.toDouble()).toInt()
        adjList = board.indices.map { adj(it, row) }

        board.indices.forEach { search(mutableListOf(it), baseNode.childNodes[board[it]]!!) }
        return found.toList()
    }

    private fun search(hist: MutableList<Int>, node: Node) {
//        val time1 = System.nanoTime()
        val adj = adjList[hist.last()]
//        sum1 += System.nanoTime() - time1
        adj.forEach { pos ->
//            val time2 = System.nanoTime()
            val cont = !hist.contains(pos)
//            sum2 += System.nanoTime() - time2
            if (cont) {
                node.childNodes[board[pos]]?.also {
//                    val time3 = System.nanoTime()
                    hist.add(pos)
                    it.word?.also { word ->
                        found.add(word)
                    }
//                    sum3 += System.nanoTime() - time3
                    if (it.value > 0) {
                        search(hist, it)
                    }
//                    val time4 = System.nanoTime()
                    hist.removeLast()
//                    sum4 += System.nanoTime() - time4
                }
            }
        }
    }
}

@ExperimentalStdlibApi
fun main() {
    val solver = time(message = "Preprocessing time") { BoggleTrie("wordlist.txt") }

    val time1 = System.nanoTime()
    solver.solve("SERSPATGLINESERS".toLowerCase().toList())
    val totalDiff: Double = System.nanoTime() - time1.toDouble()
//    println("sum1: ${solver.sum1/totalDiff}")
//    println("sum2: ${solver.sum2/totalDiff}")
//    println("sum3: ${solver.sum3/totalDiff}")
//    println("sum4: ${solver.sum4/totalDiff}")

//    solver.solve(List(16) { 'a' + (0 until 26).random() })

//    solver.solve("SERSPATGLINESERS".toLowerCase().toList()).apply {
//        println(if (size == 1414 && getPoints() == 4527) {
//            "Method Status: VALID"
//        } else {
//            "There seems to be a problem: $size words with ${getPoints()} points were found"
//        })
//    }
//    time(5_000, "Avg time with best board") { solver.solve("SERSPATGLINESERS".toLowerCase().toList()) }
    time(200_000, "Avg time with random boards") { solver.solve(List(16) { 'a' + (0 until 26).random() }) }

    var best = 0
///    var counter = 0
//    val time = System.currentTimeMillis()
//    while (System.currentTimeMillis() - time < 60_000) {
//        counter++
//        val board = List(16) { 'a' + (0 until 26).random() }
//        solver.solve(board).getPoints().also {
//            if (it > best) {
//                best = it
//                println("(${it}) $board)")
//            }
//        }
//    }
//    println("Best Score: $best with $counter iterations")
}
