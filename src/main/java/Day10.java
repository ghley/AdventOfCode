import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/*
Not happy with the result, something's wrong
 */
public class Day10 extends Days{
    private static final String TASK_2 = """
            
            ##..##..##..##..##..##..##..##..##..##..
            ###...###...###...###...###...###...###.
            ####....####....####....####....####....
            #####.....#####.....#####.....#####.....
            ######......######......######......####
            #######.......#######.......#######.....""";


    public static void main(String[] args) {
        new Day10();
    }

    class CPU {
        int x = 1;
        int c = 1;

        public void nop() {
            duringCycleHook.accept(this);
            c++;
        }

        public void add(int v) {
            duringCycleHook.accept(this);
            c++;
            duringCycleHook.accept(this);
            c++;
            x += v;
        }

        Consumer<CPU> duringCycleHook = (cpu)->{};
    }

    public Day10() {
        super(13140L,TASK_2);
    }

    @Override
    public Object task1(String data) {
        var cpu = new CPU();
        AtomicLong signal = new AtomicLong(0);
        cpu.duringCycleHook = (c)-> {
            if ((c.c - 20) % 40 == 0) {
                signal.addAndGet((c.c) * c.x);
            }
        };
        for (var split : data.split("\n")) {
            var cmd = split.split(" ");
            switch (cmd[0]) {
                case "noop" -> {
                    cpu.nop();
                }
                case "addx" ->{
                    cpu.add(Integer.parseInt(cmd[1]));
                }
            }
        }
        return signal.get();
    }

    @Override
    public Object task2(String data) {
        var cpu = new CPU();
        char[] output = new char[240];
        cpu.duringCycleHook = (c)-> {
            int p = c.x;
            int i = c.c % 40;
            if ((i >= p && i < p+3)
                || (i >= p-40 && i < p+3-40)) { // sprites somehow wrap around as well I guess
                output[c.c-1] = '#';
            }else {
                output[c.c-1] = '.';
            }
            System.out.println(i+" "+p);
            System.out.println(c.c+" "+c.x);
            System.out.println("----");
        };
        for (var split : data.split("\n")) {
            var cmd = split.split(" ");
            switch (cmd[0]) {
                case "noop" -> {
                    cpu.nop();
                }
                case "addx" ->{
                    cpu.add(Integer.parseInt(cmd[1]));
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int q = 0; q < 6; q++) {
            sb.append("\n");
            sb.append(new String(Arrays.copyOfRange(output, q * 40, (q+1)*40)));
        }
        return sb.toString();
    }
}
