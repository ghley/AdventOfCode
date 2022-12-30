import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day15 extends Days {
    public static void main(String[] args) {
        new Day15();
    }

    public Day15() {
        super(26, 56000011);
    }

    record Vec(long x, long y) {
        long manDis(Vec to) {
            return Math.abs(x - to.x) + Math.abs(y - to.y);
        }

        Vec add(Vec vec) {
            return new Vec(x + vec.x, y + vec.y);
        }

        Vec add(Vec vec, long scalar) {
            return new Vec(x + vec.x * scalar, y + vec.y * scalar);
        }
    }

    record Sensor(Vec pos, Vec closest) {
        long distance() {
            return pos.manDis(closest);
        }
    }

    @Override
    public Object task1(String data) {
        var sensors = Arrays.stream(data.split("\n"))
                .map(this::lineToCoords)
                .map(l -> new Sensor(new Vec(l[0], l[1]), new Vec(l[2], l[3])))
                .collect(Collectors.toList());
        var stats = sensors.stream()
                .flatMapToLong(s -> Arrays.stream(new long[]{s.pos.x + s.distance(), s.pos.x - s.distance()}))
                .summaryStatistics();
        return numPerLine(sensors, stats.getMin(), stats.getMax(), isTest() ? 10 : 2000000L);
    }

    @Override
    public Object task2(String data) {
        long maxX = isTest() ? 20 : 4000000;
        long maxY = isTest() ? 20 : 4000000;
        var sensors = Arrays.stream(data.split("\n"))
                .map(this::lineToCoords)
                .map(l -> new Sensor(new Vec(l[0], l[1]), new Vec(l[2], l[3])))
                .collect(Collectors.toList());

        // I'm probably overestimating the searchspace but we are talking about not even a magnitude
        Vec RIGHT_DOWN = new Vec(1,-1);
        Vec RIGHT_UP = new Vec(1, 1);
        Vec LEFT_DOWN = new Vec(-1, -1);
        Vec LEFT_UP = new Vec(-1, 1);

        Map<Vec, Integer> open = new HashMap<>();

        for (var sensor : sensors) {
            System.out.println(sensor.pos);
            Set<Vec> newVecs = new HashSet<>();
            var vecTop = sensor.pos.add(new Vec(0, 1), sensor.distance()+1);
            var vecBot = sensor.pos.add(new Vec(0, -1), sensor.distance()+1);

            newVecs.add(vecTop);
            newVecs.add(vecBot);

            for (int q = 0; q < sensor.distance() + 1; q++) {
                newVecs.add(vecTop.add(RIGHT_DOWN, q));
                newVecs.add(vecTop.add(LEFT_DOWN, q));
                newVecs.add(vecBot.add(RIGHT_UP, q));
                newVecs.add(vecBot.add(LEFT_UP,q));
            }
            newVecs.forEach(vec->{
                open.computeIfPresent(vec, (v, i)->{
                    return i + 1;
                });
                open.putIfAbsent(vec, 1);
            });
        }

        var list = open.entrySet().stream().sorted((e1,e2)->{
            return Integer.compare(e2.getValue(), e1.getValue());
        }).map(Map.Entry::getKey)
                        .filter(v->v.x >= 0 && v.x <= maxX && v.y >= 0 && v.y <= maxY)
                .filter(v->!sensors.stream().filter(s->s.pos.equals(v) || s.closest.equals(v)).findAny().isPresent())
                        .collect(Collectors.toList());

        for (Vec v : list) {
            boolean captured = false;
            for (var sensor : sensors) {
                captured |= v.manDis(sensor.pos) <= sensor.distance();
                if (v.equals(sensor.closest)) {
                    captured = false;
                    break;
                }
                if (v.equals(sensor.pos)) {
                    captured = false;
                    break;
                }
            }
            if (!captured) {
                return v.x * 4000000 + v.y;
            }
        }
        return -1;
    }

    public long numPerLine(List<Sensor> sensors, long min, long max, long y) {
        long c = 0;
        for (long x = min - 2; x < max + 2; x++) {
            var currVec = new Vec(x, y);
            boolean captured = false;
            for (var sensor : sensors) {
                captured |= currVec.manDis(sensor.pos) <= sensor.distance();
                if (currVec.equals(sensor.closest)) {
                    captured = false;
                    break;
                }
                if (currVec.equals(sensor.pos)) {
                    captured = false;
                    break;
                }
            }
            if (captured) {
                c++;
            }
        }
        return c;
    }


    public long[] lineToCoords(String line) {
        return Arrays.stream(line.split(" "))
                .filter(l -> l.startsWith("x") || l.startsWith("y"))
                .map(l -> l.split("[=|:|,]")[1])
                .mapToLong(Long::parseLong).toArray();
    }
}
