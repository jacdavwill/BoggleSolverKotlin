ms/board
---------- Regular ------------------
83   -> Original
43   -> group words by first letter
3.4  -> group by first three history.getLetters (only 41k words have to actually be searched for on best board, avg: 10-20k)
3.2  -> auto accept words of length 3 (because already grouped by possible threes)
0.9  -> remember prefix history
0.82 -> only check if first and third pos in prefix overlap


----------- Trie ---------------------
0.08  -> Original
0.063 -> Node holds word
0.060 -> Found words stored in mutableSet
0.050 -> Cache the adjacency list at the beginning
0.048 -> Hardcode the adjacency list for 4x4 board
0.038 -> Save history as list of bools rather than list of ints