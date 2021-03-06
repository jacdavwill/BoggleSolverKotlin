package history

import BoggleTrie
import adj
import time
import java.io.File
import kotlin.math.sqrt

/*
  S E R S
  P A T G
  L I N E
  S E R S
*/
@ExperimentalStdlibApi
class BoggleSolver(wordlistSource: String) {
    private lateinit var board: List<Char>
    private var wordlist: List<String> = File(wordlistSource).readLines().filter { it.length > 2 }
    private var wordsByLetter = wordlist.groupBy { it.substring(0, 3) }
    private var row = 0

    fun solve(board: List<Char>): List<String> {
        this.board = board
        row = sqrt(board.size.toDouble()).toInt()

        val boardTriples = board.indices.flatMap { first ->
            adj(first, row).flatMap { second ->
                adj(second, row).mapNotNull { third ->
                    if (first != third) {
                        "${board[first]}${board[second]}${board[third]}" to listOf(first, second, third)
                    } else null
                }
            }
        }.groupBy { it.first }
        return boardTriples.flatMap {
            wordsByLetter[it.key]?.filter { word ->
                if (word.length == 3) return@filter true
                for (prefix in it.value) {
                    if (search(prefix.second.toMutableList(), word)) {
                        return@filter true
                    }
                }
                false
            } ?: emptyList()
        }
    }

    private fun search(hist: MutableList<Int>, word: String): Boolean {
        val nextLet = word[hist.size]
        adj(hist.last(), row).forEach { pos ->
            if (board[pos] == nextLet && !hist.contains(pos)) {
                hist.add(pos)
                if (hist.size == word.length) {
                    return true
                } else {
                    if (search(hist, word)) {
                        return true
                    } else {
                        hist.removeLast()
                    }
                }
            }
        }
        return false
    }
}

@ExperimentalStdlibApi
fun main() {
    val solver = time(message = "Preprocessing time") { BoggleTrie("wordlist.txt") }
    val words = solver.solve("SERSPATGLINESERS".toLowerCase().toList()).forEach { println(it) }
//    time(500) { solver.solve("SERSPATGLINESERS".toLowerCase().toList()) }
//    time(20_000) { solver.solve(List(16) { 'a' + (0 until 26).random() }) }

//    var best = 0
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
