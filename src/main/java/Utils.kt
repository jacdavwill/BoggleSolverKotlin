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

fun <T> duration(seconds: Int = 60, iterationsBetweenChecks: Int = 100, message: String = "", func: () -> T): Int {
    val startTime = System.nanoTime()
    var count = 0
    while((System.nanoTime() - startTime) / 1_000_000_000 < seconds) {
        (1..iterationsBetweenChecks).forEach {  func() }
        count += iterationsBetweenChecks
    }
    println("$message: $count iterations, Avg ms per board: ${seconds.toDouble() * 1000 / count}")
    return count
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

//fun adj(pos: Int, row: Int) {
//    int x = pos / row
//    int y = pos % row
//    if (x == 0 && y == 0) {
//        places[0] = pos + 1
//        places[1] = pos + row
//        places[2] = pos + row + 1
//        return 3
//    } else if (x == 0 && y == row - 1) {
//        places[0] = pos - 1
//        places[1] = pos + row
//        places[2] = pos + row - 1
//        return 3
//    } else if (x == row - 1 && y == 0) {
//        places[0] = pos + 1
//        places[1] = pos - row
//        places[2] = pos - row + 1
//        return 3
//    } else if (x == row - 1 && y == row - 1) {
//        places[0] = pos - 1
//        places[1] = pos - row
//        places[2] = pos - row - 1
//        return 3
//    } else if (x == 0) {
//        places[0] = pos - 1
//        places[1] = pos + 1
//        places[2] = pos + row
//        places[3] = pos + row - 1
//        places[4] = pos + row + 1
//        return 5
//    } else if (x == row - 1) {
//        places[0] = pos - 1
//        places[1] = pos + 1
//        places[2] = pos - row
//        places[3] = pos - row - 1
//        places[4] = pos - row + 1
//        return 5
//    } else if (y == 0) {
//        places[0] = pos + row
//        places[1] = pos - row
//        places[2] = pos + row + 1
//        places[3] = pos + 1
//        places[4] = pos - row + 1
//        return 5
//    } else if (y == row - 1) {
//        places[0] = pos + row
//        places[1] = pos - row
//        places[2] = pos + row - 1
//        places[3] = pos - 1
//        places[4] = pos - row - 1
//        return 5
//    } else {
//        places[0] = pos + 1
//        places[1] = pos - 1
//        places[2] = pos + row
//        places[3] = pos + row - 1
//        places[4] = pos + row + 1
//        places[5] = pos - row
//        places[6] = pos - row - 1
//        places[7] = pos - row + 1
//        return 8
//    }
