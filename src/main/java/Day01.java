import java.io.IOException;
import java.util.Arrays;

public class Day01 extends Days {
    @Override
    public Object task1(String fileContent) {
        return Arrays.stream(fileContent.split("\n\n"))
                .mapToLong(
                        s -> Arrays.stream(s.split("\n"))
                                .mapToLong(Long::parseLong).sum()
                )
                .summaryStatistics().getMax();
    }

    @Override
    public Object task2(String data) {
        return -Arrays.stream(data.split("\n\n"))
                .mapToLong(
                        s -> -Arrays.stream(s.split("\n"))
                                .mapToLong(Long::parseLong).sum()
                )
                .sorted()
                .limit(3).sum();
    }

    public static void main(String[] args) throws IOException {
        new Day01();
    }
    Day01() {
        super(24000L, 45000L);
    }
}
