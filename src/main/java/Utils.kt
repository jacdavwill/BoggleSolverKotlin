fun List<String>.getPoints() = this.groupBy { it.length }.map {
    when (it.key) {
        3, 4 -> 1
        5 -> 2
        6 -> 3
        7 -> 5
        else -> 11
    } * it.value.size
}.sum()

fun <T> time(times: Int = 1, message: String = "", func: () -> T): T {
    val startTime = System.nanoTime()
    return func().apply {
        (1 until times).forEach {
            func()
        }
        println("$message: ${(System.nanoTime() - startTime) / times / 1_000_000.0}")
    }
}

fun adj(a: Int, row: Int): List<Int> = when (val pos = a / row to a % row) {
    0 to 0 -> listOf(a + 1, a + row, a + row + 1)
    0 to row - 1 -> listOf(a - 1, a + row, a + row - 1)
    row - 1 to 0 -> listOf(a + 1, a - row, a - row + 1)
    row - 1 to row - 1 -> listOf(a - 1, a - row, a - row - 1)
    else -> when {
        pos.first == 0 -> listOf(a - 1, a + 1, a + row, a + row - 1, a + row + 1)
        pos.first == row - 1 -> listOf(a - 1, a + 1, a - row, a - row - 1, a - row + 1)
        pos.second == 0 -> listOf(a + row, a - row, a + row + 1, a + 1, a - row + 1)
        pos.second == row - 1 -> listOf(a + row, a - row, a + row - 1, a - 1, a - row - 1)
        else -> listOf(a + 1, a - 1, a + row, a + row - 1, a + row + 1, a - row, a - row - 1, a - row + 1)
    }
}
