import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 extends Days{
    public static void main(String[] args) {
        new Day14();
    }

    public Day14() {
        super(24,93);
    }

    @Override
    public Object task1(String data) {
        var map = buildMap(data, false);
        return simulate(map);
    }

    private static long simulate(Map map) {
        long c = 0;
        boolean fellOff = false;
        while (!fellOff) {
            int x = map.startPoint.x;
            int y = map.startPoint.y;
            if (map.isBlocking(x,y)) {
                break;
            }
            while (true) {
                if (x < 0 || x >= map.dim.x || y+1 >= map.dim.y) {
                    fellOff = true;
                    break;
                }
                if (map.isBlocking(x, y+1)) {
                    if (map.isBlocking(x-1, y+1)) {
                        if (map.isBlocking(x+1, y+1)) {
                            map.set(x, y, 1);
                            c++;
                            break;
                        }else {
                            x++;
                            y++;
                        }
                    }else {
                        x--;
                        y++;
                    }
                }else {
                    y++;
                }
            }
        }
        return c;
    }

    @Override
    public Object task2(String data) {
        var map = buildMap(data, true);
        return simulate(map);
    }

    record Vec(int x, int y) {

    }
    record Line(Vec... vecs) {

    }
    record Map(Vec startPoint, Vec dim, int[] mapData) {

        void set(int x, int y, int value) {
            mapData[x + y * dim.x] = value;
        }

        boolean isBlocking(int x, int y) {
            return mapData[x + y * dim.x] != 0;
        }

        void prettyPrint() {
            for (int y = 0; y < dim.y ; y++) {
                String line = "";
                for (int x = 0; x < dim.x; x++) {
                    if (x == startPoint.x && y == startPoint.y) {
                        line += '+';
                        continue;
                    }
                    switch(mapData[x + y * dim.x]) {
                        case 0 ->{
                            line += '.';
                        }
                        case -1 ->{
                            line += '#';
                        }
                        case 1 ->{
                            line += 'o';
                        }
                    }
                }
                System.out.println(line);
            }
        }
    }

    private Map buildMap(String data, boolean withFloor) {
        List<Line> lines = new ArrayList<>();
        for (var line : data.split("\n")) {
            lines.add(new Line(Arrays.stream(line.split("->"))
                    .map(String::trim)
                    .map(e->Arrays.stream(e.split(",")).mapToInt(Integer::parseInt).toArray()
                    ).map(l -> new Vec(l[0], l[1])).toArray(Vec[]::new)));
        }
        var statX = lines.stream().flatMap(l->Arrays.stream(l.vecs))
                .mapToInt(Vec::x).summaryStatistics();
        var statY = lines.stream().flatMap(l->Arrays.stream(l.vecs))
                .mapToInt(Vec::y).summaryStatistics();
        // do not offset the Y axis, as 500,0 is the start point
        int height = statY.getMax() + 1 + (withFloor ? 2 : 0);
        int offsetX = withFloor ? 500 - (height+2) : statX.getMin();
        int width = withFloor ? 2 * (height+2) : (statX.getMax() - offsetX + 1);
        if (withFloor) {
            lines.add(new Line(new Vec(offsetX, height-1), new Vec(offsetX + width-1,height-1)));
        }
        var start = new Vec(500 - offsetX, 0);
        int[] mapData = new int[width * height];
        var map = new Map(start, new Vec(width, height), mapData);
        for (var line : lines) {
            for (int q = 0; q < line.vecs.length-1; q++) {
                int x = line.vecs[q].x;
                int y = line.vecs[q].y;
                int tx = x - line.vecs[q+1].x;
                int ty = y - line.vecs[q+1].y;
                int dx = (int)Math.signum(tx);
                int dy = (int)Math.signum(ty);
                int max = Math.max(tx*dx, ty*dy);
                x -= offsetX;

                // only overdraw the last segment
                for (int r = 0; r < max + (q == line.vecs.length-2 ? 1 : 0); r++) {
                    map.set(x, y, -1);
                    x-=dx;
                    y-=dy;
                }
            }
        }
        return map;
    }
}
