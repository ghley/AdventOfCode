import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * We will ignore lots of the input as all the entries are
 * formatted the same and are severely limited, so don't
 * expect a sophisticated parser.
 *
 * There is probably a better way to do this with only queues
 * and removing a lot of OO
 *
 * The divisors are designed to be prime numbers specifically
 * to make the checks slow. Let's keep them in a database for faster access
 */
public class Day11 extends Days {
    public static void main(String[] args) {
        new Day11();
    }

    public Day11() {
        super(10605L, 2713310158L);
    }

    enum Operators {
        MULTIPLY, ADD
    }

    static class Monkey {
        LinkedList<Long> items;
        Operators operator;
        String[] operands;
        int divisor;
        int[] cases;

        long inspectCounter = 0L;

        public long getInspectCounter() {
            return inspectCounter;
        }

        @Override
        public String toString() {
            return "Monkey{" +
                    "items=" + items +
                    ", inspectCounter=" + inspectCounter +
                    '}';
        }
    }

    private List<Monkey> parseMonkeys(String data) {
        var monkeysStr = data.split("\n\n");
        List<Monkey> monkeys = new ArrayList<>();
        for (var monkeyStr : monkeysStr) {
            var lines = monkeyStr.split("\n");
            var monkey = new Monkey();
            monkey.items =
                    new LinkedList<>(Arrays.stream(lines[1].split(":")[1].split(","))
                            .map(String::trim).map(Long::parseLong).collect(Collectors.toList()));
            var operationLine = lines[2].split("=")[1].trim().split(" ");
            monkey.operator = operationLine[1].equals("*") ? Operators.MULTIPLY : Operators.ADD;
            monkey.operands = new String[] {operationLine[0], operationLine[2]};
            monkey.divisor = Integer.parseInt(lines[3].split("by")[1].trim());
            monkey.cases = new int[]{ // false/true
                    Integer.parseInt(lines[5].split("monkey")[1].trim()),
                    Integer.parseInt(lines[4].split("monkey")[1].trim())
            };
            monkeys.add(monkey);
        }
        return monkeys;
    }

    public long simulate(List<Monkey> monkeys, int rounds, long reduceWorryLevels) {
        // if n is divisible by P, so is (n-kP), thus to make sure numbers are always divisible by
        // the divisors, we reduce the resulting item value by kP whereas P is the multiple
        // of all test values
        long modulo = monkeys.stream().mapToLong(m->m.divisor).reduce(1L, (i, j)->i*j);
        Map<Integer, Map<BigInteger, Boolean>> divisibleTable = new HashMap<>();
        for (var monkey : monkeys) {
            divisibleTable.put(monkey.divisor, new HashMap<>());
        }
        var worry = new BigInteger("3");
        for (int q = 0; q < rounds; q++) {
            for (Monkey monkey : monkeys) {
                while (!monkey.items.isEmpty()) {
                    monkey.inspectCounter++;
                    var item = monkey.items.pop();
                    var leftOperand = monkey.operands[0].equals("old") ?
                            item : Long.parseLong(monkey.operands[0]);
                    var rightOperand = monkey.operands[1].equals("old") ?
                            item : Long.parseLong(monkey.operands[1]);
                    long newItem = 0;
                    switch (monkey.operator) {
                        case MULTIPLY -> {
                            newItem = leftOperand * rightOperand;
                        }
                        case ADD -> {
                            newItem = leftOperand + rightOperand;
                        }
                    }
                    newItem /= reduceWorryLevels;
                    newItem %= modulo;
                    boolean divisible = newItem % monkey.divisor == 0;
                    monkeys.get(monkey.cases[divisible ? 1 : 0]).items.addLast(newItem);
                }
            }
        }
        long result = monkeys.stream().sorted((m1, m2) -> Long.compare(m2.inspectCounter, m1.inspectCounter))
                .limit(2).mapToLong(Monkey::getInspectCounter)
                .reduce(1L, (i, j)->i * j);
        return result;
    }

    @Override
    public Object task1(String data) {
        var monkeys = parseMonkeys(data);
        return simulate(monkeys, 20, 3);
    }

    @Override
    public Object task2(String data) {
        var monkeys = parseMonkeys(data);
        long reducer = monkeys.stream().mapToLong(m->m.divisor).reduce(1L, (i, j)->i*j);
        return simulate(monkeys, 10000, 1);
    }
}
