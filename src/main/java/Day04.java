public class Day04 extends Days{

    @Override
    public Object task1(String data) {
        long c = 0;
        for (var split : data.split("\n")) {
            var groups = split.split(",");
            var group1 = groups[0].split("-");
            var group2 = groups[1].split("-");
            var a = Integer.parseInt(group1[0]);
            var b = Integer.parseInt(group1[1]);
            var x = Integer.parseInt(group2[0]);
            var y = Integer.parseInt(group2[1]);
            c += (a >= x && b <= y) || (x >= a && y <= b) ? 1 : 0;
        }
        return c;
    }

    @Override
    public Object task2(String data) {
        long c = 0;
        for (var split : data.split("\n")) {
            var groups = split.split(",");
            var group1 = groups[0].split("-");
            var group2 = groups[1].split("-");
            var a = Integer.parseInt(group1[0]);
            var b = Integer.parseInt(group1[1]);
            var x = Integer.parseInt(group2[0]);
            var y = Integer.parseInt(group2[1]);
            c += (a >= x && a <= y) || (x >= a && x <= b) ? 1 : 0;
        }
        return c;
    }

    public static void main(String[] args) {
        new Day04();
    }

    public Day04() {
        super(2L, 4L);
    }
}
