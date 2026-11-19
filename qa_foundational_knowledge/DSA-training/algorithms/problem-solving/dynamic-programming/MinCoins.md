# Min Coins

Find the fewest coins that make a total, allowing repeated positive denominations.

## Work through it

The best answer for amount n extends an already solved smaller amount. Trace denominations 1,3,4 and total 6: greedy uses three coins, while 3+3 uses two. O(amount × denominations) time and O(amount) space.

Read the [Java implementation](MinCoins.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
