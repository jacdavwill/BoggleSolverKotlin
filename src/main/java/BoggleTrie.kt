import java.io.File
import kotlin.math.sqrt

/*
  S E R S  0  1  2  3
  P A T G  4  5  6  7
  L I N E  8  9  10 11
  S E R S  12 13 14 15
*/
@ExperimentalStdlibApi
class BoggleTrie(wordlistSource: String) {
    private lateinit var board: List<Char>
    private val baseNode = Node()
    private var row = 0
    private lateinit var found: MutableSet<String>
    private var adjList = listOf(
            listOf(1, 4, 5), listOf(0, 2, 4, 5, 6), listOf(1, 3, 5, 6, 7), listOf(2, 6, 7),
            listOf(0, 1, 5, 8, 9), listOf(0, 1, 2, 4, 6, 8, 9, 10), listOf(1, 2, 3, 5, 7, 9, 10, 11), listOf(2, 3, 6, 10, 11),
            listOf(4, 5, 9, 12, 13), listOf(4, 5, 6, 8, 10, 12, 13, 14), listOf(5, 6, 7, 9, 11, 13, 14, 15), listOf(6, 7, 10, 14, 15),
            listOf(8, 9, 13), listOf(8, 9, 10, 12, 14), listOf(9, 10, 11, 13, 15), listOf(10, 11, 14)
    )
    var sum1: Long = 0
    var sum2: Long = 0
    var sum3: Long = 0
    var sum4: Long = 0
    var sum5: Long = 0
    var sum6: Long = 0
    var sum7: Long = 0
    var count1 = 0
    var time1: Long = 0

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
        board.indices.forEach { search(mutableListOf(it), baseNode.childNodes[board[it]]!!) }
        return found.toList()
    }

    private fun search(hist: MutableList<Int>, node: Node) {
        adjList[hist.last()].forEach { pos ->
            if (!hist.contains(pos)) {
                node.childNodes[board[pos]]?.also {
                    hist.add(pos)
                    it.word?.also { word ->
                        found.add(word)
                    }
                    if (it.value > 0) {
                        search(hist, it)
                    }
                    hist.removeLast()
                }
            }
        }
    }
}

@ExperimentalStdlibApi
fun main() {
    val solver = time(message = "Preprocessing time") { BoggleTrie("wordlist.txt") }

    val time1 = System.nanoTime()
//    time(5_000, "Test w/time") { solver.solve("SERSPATGLINESERS".toLowerCase().toList()) }
    val totalDiff: Double = System.nanoTime() - time1.toDouble()

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

//    var best = 0
//    var counter = 0
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
