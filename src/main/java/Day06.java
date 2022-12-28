/**
 * Assumes single line
 */
public class Day06 extends Days {

    /**
     * since the sliding window is only 4 long we will hardcode it
     * and check for all elements each loop
     */
    @Override
    public Object task1(String data) {
        var cArr = data.toCharArray();
        for (int q = 0; q < cArr.length - 4; q++) {
            if (cArr[q] == cArr[q+1] ||
                    cArr[q] == cArr[q+2] ||
                    cArr[q] == cArr[q+3] ||
                    cArr[q+1] == cArr[q+2] ||
                    cArr[q+1] == cArr[q+3] ||
                    cArr[q+2] == cArr[q+3])
                continue;
            return q+4;
        }
        return -1L;
    }

    /**
     * Going for space efficiency over speed
     */
    @Override
    public Object task2(String data) {
        var cArr = data.toCharArray();
        for (int q =0; q < cArr.length-14; q++) {
            var l = 0b0;
            for (int r = 0; r < 14; r++) {
                l |=  (1 << (cArr[q+r] - 'a'));
            }
            int c = 0;
            for (c = 0; l != 0; c++) {
                l &= (l - 1);
            }
            if (c == 14) {
                return q + 14;
            }
        }
        return -1L;
    }

    public static void main(String[] args) {
        new Day06();
    }

    public Day06() {
        super(11L, 26L);
    }
}
