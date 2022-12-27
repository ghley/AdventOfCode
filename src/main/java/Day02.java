import java.util.Arrays;
import java.util.Map;

public class Day02 extends Days {
    @Override
    public Object task1(String data) {
        Map<String, Long> scoreMap =
                Map.of(
                        "A X", 1L + 3L,
                        "B X", 1L + 0L,
                        "C X", 1L + 6L,
                        "A Y", 2L + 6L,
                        "B Y", 2L + 3L,
                        "C Y", 2L + 0L,
                        "A Z", 3L + 0L,
                        "B Z", 3L + 6L,
                        "C Z", 3L + 3L
                );
        return Arrays.stream(data.split("\n"))
                .mapToLong(
                        scoreMap::get
                ).sum();
    }

    @Override
    public Object task2(String data) {
        var pickVal = new int[]{1,2,3};
        var score = new int[]{0,3,6};

        return Arrays.stream(data.split("\n"))
                .mapToLong(s->{
                    var chars = s.toCharArray();
                    var ldw = (chars[2] - 'X');
                    var pick = (chars[0] - 'A');
                    var ours = (ldw == 0 ? (pick + 2) : (ldw == 1 ? pick : pick + 1))%3;
                    return score[ldw] + pickVal[ours];
                }).sum();
    }

    public static void main(String[] args) {
        new Day02( );
    }

    Day02() {
        super(15L, 12L);
    }


}
