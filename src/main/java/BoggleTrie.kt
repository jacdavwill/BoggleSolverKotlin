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
    private var tracking = false
    private lateinit var found: MutableSet<String>
    private lateinit var quality: MutableList<Int>
    private var adjList = listOf( // for boards of size other than 4x4 replace fill this using the Utils.adj() function
            listOf(1, 4, 5), listOf(0, 2, 4, 5, 6), listOf(1, 3, 5, 6, 7), listOf(2, 6, 7),
            listOf(0, 1, 5, 8, 9), listOf(0, 1, 2, 4, 6, 8, 9, 10), listOf(1, 2, 3, 5, 7, 9, 10, 11), listOf(2, 3, 6, 10, 11),
            listOf(4, 5, 9, 12, 13), listOf(4, 5, 6, 8, 10, 12, 13, 14), listOf(5, 6, 7, 9, 11, 13, 14, 15), listOf(6, 7, 10, 14, 15),
            listOf(8, 9, 13), listOf(8, 9, 10, 12, 14), listOf(9, 10, 11, 13, 15), listOf(10, 11, 14)
    )

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

    fun solve(board: List<Char>, tracking: Boolean = false): List<String> {
        found = hashSetOf()
        this.board = board
        row = sqrt(board.size.toDouble()).toInt()
        this.tracking = tracking
//        adjList = board.indices.map { adj(it, row) }
        row = sqrt(board.size.toDouble()).toInt()
        board.indices.forEach { search(MutableList(board.size) { pos -> it == pos }, it,baseNode.childNodes[board[it]]!!) }
        return found.toList()
    }

    private fun search(hist: MutableList<Boolean>, lastPos: Int, node: Node) {
        adjList[lastPos].forEach { pos ->
            if (!hist[pos]) {
                node.childNodes[board[pos]]?.also {
                    hist[pos] = true
                    it.word?.also { word ->
                        found.add(word)
                    }
                    if (it.value > 0) {
                        search(hist, pos, it)
                    }
                    hist[pos] = false
                }
            }
        }
    }
}

@ExperimentalStdlibApi
fun main() {
    val solver = time(message = "Preprocessing time") { BoggleTrie("wordlist.txt") }

    val time1 = System.nanoTime()
    time(1, "Test w/time") { solver.solve("SERSPATGLINESERS".toLowerCase().toList()) }.apply {
        if (size == 1414) {
            println("OK")
        } else {
            println("Something is wrong!")
        }
    }
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
    time(200_000, "Avg time with random boards") { solver.solve(List(16) { 'a' + (0 until 26).random() }, true) }
}
