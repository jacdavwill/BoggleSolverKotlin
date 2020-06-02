package history

import adj
import getPoints
import java.io.File
import kotlin.math.sqrt

/*
  S E R S  0  1  2  3
  P A T G  4  5  6  7
  L I N E  8  9  10 11
  S E R S  12 13 14 15
*/
@ExperimentalStdlibApi
class BoggleTrieTracking(wordlistSource: String) {
    private lateinit var board: List<Char>
    private val baseNode = Node()
    private var row = 0
    private var tracking = false
    private lateinit var found: MutableSet<String>
    private lateinit var quality: MutableList<Int>
    private lateinit var adjList: List<List<Int>>

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

    fun solveForPoints(board: List<Char>, tracking: Boolean = false): Pair<Int, List<Int>> {
        val resp = solve(board, tracking)
        return resp.first.getPoints() to resp.second
    }

    fun solve(board: List<Char>, tracking: Boolean = false): Pair<List<String>, List<Int>> {
        found = hashSetOf()
        quality = MutableList(board.size) { 0 }
        this.board = board
        row = sqrt(board.size.toDouble()).toInt()
        this.tracking = tracking
        adjList = board.indices.map { adj(it, row) }
        row = sqrt(board.size.toDouble()).toInt()
        board.indices.forEach { search(mutableListOf(it), baseNode.childNodes[board[it]]!!) }
        return found.toList() to quality
    }

    private fun search(hist: MutableList<Int>, node: Node) {
        adjList[hist.last()].forEach { pos ->
            if (!hist.contains(pos)) {
                node.childNodes[board[pos]]?.also {
                    hist.add(pos)
                    it.word?.also { word ->
                        found.add(word)
                        if (tracking) { hist.forEach { quality[it]++ } }
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
