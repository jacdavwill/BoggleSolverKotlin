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
    private val found = setOf<String>()

    init {
        File(wordlistSource).readLines().forEach { word ->
            if (word.length > 2) {
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
        this.board = board
        row = sqrt(board.size.toDouble()).toInt()

        board.indices.forEach { search(mutableListOf(it), baseNode.childNodes[board[it]]!!) }
        return found.toList()
    }

    private fun search(hist: MutableList<Int>, node: Node) {
        adj(hist.last(), row).forEach { pos ->
            if (!hist.contains(pos)) {
                node.childNodes[board[pos]]?.also {
                    hist.add(pos)
                    it.word?.also { word ->
                        found.plus(word)
                    }
                    if (it.value > 0) {
                        search(hist, it)
                    }C
                    hist.removeLast()
                }
            }
        }
    }
}

@ExperimentalStdlibApi
fun main() {
    val solver = time(message = "Preprocessing time") { BoggleTrie("wordlist.txt") }
    solver.solve("SERSPATGLINESERS".toLowerCase().toList()).apply {
        println(if (size == 1414 && getPoints() == 4527) {
            "Valid: method status"
        } else {
            "There seems to be a problem: $size words with ${getPoints()} were found"
        })
    }
//    time(500) { solver.solve("SERSPATGLINESERS".toLowerCase().toList()) }
    time(100_000, "Avg time with 100,000") { solver.solve(List(16) { 'a' + (0 until 26).random() }) }

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
