import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * We will assume that there will never be more than single digit stack, makes it easier to read out the values
 */
public class Day05 extends Days{

    @Override
    public Object task1(String data) {
        var splits = data.split("\n");
        String[] stacks = new String[0];
        int line = 0;
        for (int q = 0; q < splits.length; q++) {
            if (splits[q].startsWith(" 1 ")) {
                stacks = new String[splits[q].charAt(splits[q].length()-1) - '0'];
                Arrays.fill(stacks, "");
                line = q;
                break;
            }
        }
        for (int q = line-1; q >= 0; q--) {
            for (int i =0; i < stacks.length; i++) {
                var value = i * 4 + 1 >= splits[q].length() ? ' ' : splits[q].charAt(i * 4 + 1);
                if (value != ' ') {
                    stacks[i] += value;
                }
            }
        }
        for (int q = line+2; q < splits.length; q++) {
            // move 3 from 3 to 7
            var lSplits = splits[q].split(" ");
            var num = Integer.parseInt(lSplits[1]);
            var from = Integer.parseInt(lSplits[3]) - 1;
            var to = Integer.parseInt(lSplits[5]) - 1;
            for (int r = 0; r < num; r++) {
                stacks[to] += stacks[from].charAt(stacks[from].length()-1);
                stacks[from] = stacks[from].substring(0, stacks[from].length()-1);
            }
        }
        return Arrays.stream(stacks).map(s->""+s.charAt(s.length()-1)).collect(Collectors.joining());
    }

    @Override
    public Object task2(String data) {
        var splits = data.split("\n");
        String[] stacks = new String[0];
        int line = 0;
        for (int q = 0; q < splits.length; q++) {
            if (splits[q].startsWith(" 1 ")) {
                stacks = new String[splits[q].charAt(splits[q].length()-1) - '0'];
                Arrays.fill(stacks, "");
                line = q;
                break;
            }
        }
        for (int q = line-1; q >= 0; q--) {
            for (int i =0; i < stacks.length; i++) {
                var value = i * 4 + 1 >= splits[q].length() ? ' ' : splits[q].charAt(i * 4 + 1);
                if (value != ' ') {
                    stacks[i] += value;
                }
            }
        }
        for (int q = line+2; q < splits.length; q++) {
            // move 3 from 3 to 7
            var lSplits = splits[q].split(" ");
            var num = Integer.parseInt(lSplits[1]);
            var from = Integer.parseInt(lSplits[3]) - 1;
            var to = Integer.parseInt(lSplits[5]) - 1;
            stacks[to] += stacks[from].substring(stacks[from].length()-num);
            stacks[from] = stacks[from].substring(0, stacks[from].length()-num);
        }
        return Arrays.stream(stacks).map(s->""+s.charAt(s.length()-1)).collect(Collectors.joining());
    }

    public static void main(String[] args) {
        new Day05();
    }

    Day05() {
        super("CMZ", "MCD");
    }
}
