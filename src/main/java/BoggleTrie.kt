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
    private val baseNode = Node(null, '@')
    private var row = 0
    private var nodeCount = 1
    private val found = setOf<String>()

    init {
        File(wordlistSource).readLines().forEach { word ->
            if (word.length > 2) {
                var currNode = baseNode
                word.forEach { letter ->
                    currNode.value++
                    nodeCount++
                    currNode = currNode.childNodes[letter] ?: let {
                        Node(currNode, letter).also {
                            currNode.childNodes[letter] = it
                        }
                    }
                }
                currNode.isWord = true
            }
        }
    }

    class Node(var parent: Node?, var letter: Char) {
        var value = 0
        var isWord = false
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
                    if (it.isWord) {
                        found.plus(hist.toWord())
                    }
                    if (it.value > 0) {
                        search(hist, it)
                    }
                    hist.removeLast()
                }
            }
        }
    }

    private fun List<Int>.toWord(): String = map { board[it] }.joinToString(separator = "")
}

@ExperimentalStdlibApi
fun main() {
    val time1 = System.currentTimeMillis()
    val solver = BoggleTrie("wordlist.txt")
    println(System.currentTimeMillis() - time1)
//    val words = solver.solve("SERSPATGLINESERS".toLowerCase().toList())
//    println(time(500) { solver.solve("SERSPATGLINESERS".toLowerCase().toList()) })
    println(time(100_000) { solver.solve(List(16) { 'a' + (0 until 26).random() }) })

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
