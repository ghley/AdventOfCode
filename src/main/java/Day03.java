
public class Day03 extends Days{
    @Override
    public Object task1(String data) {
        long total = 0;
        for (String str : data.split("\n")) {
            long result = process(str.substring(0, str.length()/2)) & process(str.substring(str.length()/2));
            long highest;
            while ((highest = Long.highestOneBit(result)) != 0) {
                total += (Long.numberOfTrailingZeros(highest));
                result &= ~highest;
            }
        }
        return total;
    }

    @Override
    public Object task2(String data) {
        long total = 0;
        var splits = data.split("\n");
        for (int q = 0; q < splits.length/3; q++) {
            var result = process(splits[q*3]) & process(splits[q*3+1]) & process(splits[q*3+2]);
            long highest = 0;
            while ((highest = Long.highestOneBit(result)) != 0) {
                total += (Long.numberOfTrailingZeros(highest));
                result &= ~highest;
            }
        }
        return total;
    }

    private long process(String str) {
        return str.chars().mapToLong(i->i).reduce(0L,(in,n)-> in | (1L << (n > 90 ?  1 + n - 'a' : 27 + n - 'A')));
    }

    public static void main(String[] args) {
        new Day03();
    }

    public Day03(){
        super(157, 70L);
    }
}
