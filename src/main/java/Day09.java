import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Day09 extends Days {
    // all variables have to be static because the constructor of the parent
    // invokes task1 and task2 before the class itself is initialised... my bad
    private static final HashMap<Vec, Vec> lookup = new HashMap<>();
    static {
        lookup.put(Vec.of(2, 0), Vec.of(1, 0));
        lookup.put(Vec.of(0, 2), Vec.of(0, 1));
        lookup.put(Vec.of(-2, 0), Vec.of(-1, 0));
        lookup.put(Vec.of(0, -2), Vec.of(0, -1));
        lookup.put(Vec.of(2, 1), Vec.of(1, 1));
        lookup.put(Vec.of(-2, 1), Vec.of(-1, 1));
        lookup.put(Vec.of(2, -1), Vec.of(1, -1));
        lookup.put(Vec.of(-2, -1), Vec.of(-1, -1));
        lookup.put(Vec.of(1, 2), Vec.of(1, 1));
        lookup.put(Vec.of(-1, 2), Vec.of(-1, 1));
        lookup.put(Vec.of(1, -2), Vec.of(1, -1));
        lookup.put(Vec.of(-1, -2), Vec.of(-1, -1));
        lookup.put(Vec.of(-2, -2), Vec.of(-1, -1));
        lookup.put(Vec.of(2, -2), Vec.of(1, -1));
        lookup.put(Vec.of(-2, 2), Vec.of(-1, 1));
        lookup.put(Vec.of(2, 2), Vec.of(1, 1));
    }

    private static final Vec UP = Vec.of(0, 1);
    private static final Vec DOWN = Vec.of(0, -1);
    private static final Vec LEFT = Vec.of(-1, 0);
    private static final Vec RIGHT = Vec.of(1, 0);

    public static void main(String[] args) {
        new Day09();
    }

    public Day09() {
        super(13L, 1);
    }

    @Override
    public Object task1(String data) {
        var head = Vec.of(0, 0);
        var tail = Vec.of(0, 0);
        var states = new HashSet<Vec>();
        states.add(tail.copy());
        for (var str : data.split("\n")) {
            var split = str.split(" ");
            int num = Integer.parseInt(split[1]);
            for (int q = 0; q < num; q++) {
                switch (split[0]) {
                    case "U" -> {
                        head.add(UP);
                    }
                    case "D" -> {
                        head.add(DOWN);
                    }
                    case "R" -> {
                        head.add(RIGHT);
                    }
                    case "L" -> {
                        head.add(LEFT);
                    }
                }
                var delta = tail.delta(head);
                if (lookup.containsKey(delta)) {
                    tail.add(lookup.get(delta));
                    states.add(tail.copy());
                }
            }
        }

        return states.size();
    }

    @Override
    public Object task2(String data) {
        var rope = new Vec[10];
        for (int q = 0; q < rope.length; q++) {
            rope[q] = Vec.of(0,0);
        }
        var states = new HashSet<Vec>();
        states.add(rope[rope.length-1].copy());
        for (var str : data.split("\n")) {
            var split = str.split(" ");
            int num = Integer.parseInt(split[1]);
            for (int q = 0; q < num; q++) {
                switch (split[0]) {
                    case "U" -> {
                        rope[0].add(UP);
                    }
                    case "D" -> {
                        rope[0].add(DOWN);
                    }
                    case "R" -> {
                        rope[0].add(RIGHT);
                    }
                    case "L" -> {
                        rope[0].add(LEFT);
                    }
                }
                for (int i = 0; i < rope.length-1; i++) {
                    var delta = rope[1+i].delta(rope[i]);
                    if (lookup.containsKey(delta)) {
                        rope[1+i].add(lookup.get(delta));
                    }else{
                        if (delta.x + delta.y > 2) {
                            System.out.println(delta);
                            System.out.println("ARGH");
                            System.exit(-1);
                        }
                        break; // if a segment isn't moving, the tail won't move either
                    }
                }
                states.add(rope[9].copy());
            }
        }

        return states.size();
    }

    static class Vec {
        int x = 0;
        int y = 0;

        public Vec() {

        }

        public Vec(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public static Vec of(int x, int y) {
            return new Vec(x, y);
        }

        public Vec add(Vec vec) {
            this.x += vec.x;
            this.y += vec.y;
            return this;
        }

        public Vec sub(Vec vec) {
            this.x -= vec.x;
            this.y -= vec.y;
            return this;
        }

        public Vec delta(Vec to) {
            return to.copy().sub(this);
        }

        public Vec copy() {
            return new Vec(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vec vec = (Vec) o;

            if (x != vec.x) return false;
            return y == vec.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return "Vec{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}
