package history

import BoggleTrie
import kotlin.random.Random

/*
  S E R S  0  1  2  3
  P A T G  4  5  6  7
  L I N E  8  9  10 11
  S E R S  12 13 14 15
*/
/* Plan
1) create 500 random boards
2) take each of the top 100 and change one of the 4 worst spots
3) repeat
*/

val letters = listOf( // history.getLetters removed because of low use frequency (j,q,x,z)
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k', 'l',
        'm', 'n', 'o', 'p', 'r', 's', 't', 'u', 'v', 'w', 'y'
)

const val cubes = 16
const val epochs = 10_000

// This doesn't work very well yet, it quickly finds a local max and then never gets stuck

@ExperimentalStdlibApi
fun main() {
    val solver = BoggleTrieTracking("wordlist.txt")
//    val resp = solver.solve("serspatglinesers".toList(), true)
    var topBoards: List<Pair<Pair<Int, List<Int>>, MutableList<Char>>> = emptyList()
    var boards = (0 until 100).map {
        MutableList(cubes) { randLetter() }
    }
    repeat(epochs) {
        val solved = boards.map { solver.solveForPoints(it, true) to it }
                .plus(topBoards).sortedByDescending { it.first.first }
        topBoards = solved.take(50)
        boards = topBoards.flatMap { board ->
            solved[it].first.second.getWorstPos(2).map { worst ->
                if (Random.nextDouble(0.0, 1.0) < .75) {
                    board.second.apply { set(worst, randLetter()) }
                } else {
                    board.second.apply { set(board.second.indices.random(), randLetter()) }
                }
            }
        }
        if (it % 5 == 0) {
            val topAvg = topBoards.map { it.first.first }.average()
            println("Epoch($it) Best(${topBoards[0].first.first}) Top 100 AVG($topAvg)")
            println("Best Board: ${topBoards[0].second}")
        }
    }
}

fun randLetter() = letters[letters.indices.random()]

fun List<Int>.getWorstPos(numPos: Int) = mapIndexed { index, it -> index to it }
        .sortedBy { it.second }
        .take(numPos).map { it.first }
