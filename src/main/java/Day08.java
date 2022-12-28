import java.util.Arrays;

/**
 * I think there is a solution where you can half the number of operations
 * by simply using the information provided from the passthrough into one direction
 *
 * But there is probably a prettier solution out there
 */
public class Day08 extends Days {

    public static void main(String[] args) {
        new Day08();
    }

    public Day08() {
        super(21L, 8L);
    }

    @Override
    public Object task1(String data) {
        var lines = data.split("\n");
        int h = lines.length;
        int w = lines[0].length();
        int t = 16;
        int r = 12;
        int b = 8;
        int l = 4;
        var map = new int[h][w];
        for (int y = 0; y < h; y++) {
            var line = lines[y].toCharArray();
            for (int x = 0; x < w; x++){
                map[y][x] = (byte)(line[x] - '0');
            }
        }
        for (int x = 1; x < w; x++) {
            int ix = w - x - 1;
            for (int y = 0; y < h; y++) {
                int left = map[y][x-1];
                int right = map[y][ix+1];
                int leftMax = Math.max((left >> l) & 0xf, (left & 0xf));
                int rightMax = Math.max((right >> r) & 0xf, (right & 0xf));
                map[y][x] |= leftMax << l;
                map[y][ix] |= rightMax << r;
            }
        }
        for (int y = 1; y < h; y++) {
            int iy = h - y - 1;
            for (int x = 0; x < w; x++) {
                int top = map[y-1][x];
                int bottom = map[iy+1][x];
                int topMax = Math.max((top >> t) & 0xf, (top & 0xf));
                int bottomMax = Math.max((bottom >> b) & 0xf, (bottom & 0xf));
                map[y][x] |= topMax << t;
                map[iy][x] |= bottomMax << b;
            }
        }

        long c = w * 2 + (h-2)*2;
        for (int x = 1; x < w-1; x++) {
            for (int y = 1; y < h-1; y++) {
                int m = map[y][x];
                int tv = m & 0xf;
                m >>= 4;
                int tl = m & 0xf;
                m >>= 4;
                int tb = m & 0xf;
                m >>= 4;
                int tr = m & 0xf;
                m >>= 4;
                int tt = m & 0xf;
                if (tv > tl || tv > tr || tv > tb || tv > tt) {
                    c++;
                }
            }
        }
        return c;
    }

    @Override
    public Object task2(String data) {
        var lines = data.split("\n");
        //lets do this all within a single int
        int h = lines.length;
        int w = lines[0].length();
        int t = 28;
        int r = 20;
        int b = 12;
        int l = 4;
        var map = new long[h][w];
        for (int y = 0; y < h; y++) {
            var line = lines[y].toCharArray();
            for (int x = 0; x < w; x++){
                map[y][x] = (byte)(line[x] - '0');
            }
        }

        for (int x = 1; x < w; x++) {
            int ix = w - x - 1;
            for (int y = 0; y < h; y++) {
                long curr = map[y][x];
                long iCurr = map[y][ix];
                long left = map[y][x-1];
                long right = map[y][ix+1];
                boolean leftBlocking = false;
                boolean rightBlocking = false;
                int leftTotal = 1;
                int rightTotal = 1;
                while (!leftBlocking) {
                    leftBlocking = (left & 0xf) >= (curr & 0xf);
                    if (!leftBlocking) {
                        leftTotal += (left >> l) & 0xff;
                        if (x - leftTotal == 0) {
                            break;
                        }
                        left = map[y][x-leftTotal];
                    }
                }
                while (!rightBlocking) {
                    rightBlocking = (right & 0xf) >= (iCurr & 0xf);
                    if (!rightBlocking) {
                        rightTotal += (right >> r) & 0xff;
                        if (ix + rightTotal == w-1) {
                            break;
                        }
                        right = map[y][ix+rightTotal];
                    }
                }
                map[y][x] |= (long)(leftTotal & 0xff) << l;
                map[y][ix] |= (long)(rightTotal & 0xff) << r;
            }
        }
        for (int y = 1; y < h; y++) {
            int iy = h - y - 1;
            for (int x = 0; x < w; x++) {
                long curr = map[y][x];
                long iCurr = map[iy][x];
                long top = map[y-1][x];
                long bottom = map[iy+1][x];
                boolean topBlocking = false;
                boolean bottomBlocking = false;
                int topTotal = 1;
                int bottomTotal = 1;
                while (!topBlocking) {
                    topBlocking = (top & 0xf) >= (curr & 0xf);
                    if (!topBlocking) {
                        topTotal += (top >> t) & 0xff;
                        if (y - topTotal == 0) {
                            break;
                        }
                        top = map[y-topTotal][x];
                    }
                }
                while (!bottomBlocking) {
                    bottomBlocking = (bottom & 0xf) >= (iCurr & 0xf);
                    if (!bottomBlocking) {
                        bottomTotal += (bottom >> b) & 0xff;
                        if (iy + bottomTotal == h-1) {
                            break;
                        }
                        bottom = map[iy+bottomTotal][x];
                    }
                }
                map[y][x] |= (long)(topTotal & 0xff) << t;
                map[iy][x] |= (long)(bottomTotal & 0xff) << b;
            }
        }

        long max = 0;
        for (int x = 1; x < w-1; x++) {
            for (int y = 1; y < h-1; y++) {
                long m = map[y][x];
                m >>= 4;
                long tl = m & 0xff;
                m >>= 8;
                long tb = m & 0xff;
                m >>= 8;
                long tr = m & 0xff;
                m >>= 8;
                long tt = m & 0xff;
                max = Math.max(tl * tb * tr * tt, max);
            }
        }
        return max;
    }
}
