ms/board
---------- Regular ------------------
83   -> Original
43   -> group words by first letter
3.4  -> group by first three letters (only 41k words have to actually be searched for on best board, avg: 10-20k)
3.2  -> auto accept words of length 3 (because already grouped by possible threes)
0.9  -> remember prefix history
0.82 -> only check if first and third pos in prefix overlap


----------- Trie ---------------------
0.08  -> Original