import java.util.*;
/** Find the fewest coins that make a total, allowing repeated positive denominations. The best answer for amount n extends an already solved smaller amount. Trace denominations 1,3,4 and total 6: greedy uses three coins, while 3+3 uses two. O(amount × denominations) time and O(amount) space. */
public class MinCoins {
    static int solve(int[] coins,int amount) { if(amount<0 || Arrays.stream(coins).anyMatch(c->c<=0)) throw new IllegalArgumentException(); int[] best=new int[amount+1]; Arrays.fill(best,amount+1); best[0]=0; for(int n=1;n<=amount;n++) for(int c:coins) if(c<=n) best[n]=Math.min(best[n],best[n-c]+1); return best[amount]>amount?-1:best[amount]; }
}
