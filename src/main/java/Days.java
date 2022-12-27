import java.io.IOException;
import java.sql.SQLOutput;

public abstract class Days {
    private Object testResult1;
    private Object testResult2;

    public Days(Object testResult1, Object testResult2) {
        this.testResult1 = testResult1;
        this.testResult2 = testResult2;
        process();
    }

    public abstract Object task1(String data);
    public abstract Object task2(String data);

    public void process() {
        var name = this.getClass().getSimpleName();
        var str = load(name+".txt");
        var testStr = load(name+"Test.txt");

        System.out.println("Task 1\n======");
        test(task1(testStr), testResult1);
        System.out.println("Result: "+task1(str)+"\n\n");
        System.out.println("Task 2\n======");
        test(task2(testStr), testResult2);
        System.out.println("Result: "+task2(str));
    }

    private void test(Object result, Object testResult1) {
        if (testResult1 == null || !testResult1.toString().equals(result.toString())) {
            if (testResult1 == null) {
                System.out.println("Task 2 has no defined result to test against.");
            }else {
                System.out.println(String.format("Expected: " + testResult1 + "\ngot: " + result));
            }
            System.exit(-1);
        }else {
            System.out.println("Test OK");
        }
    }


    private String load(String path) {
        try {
            return new String(Days.class.getResourceAsStream(path).readAllBytes())
                    .replaceAll("\r", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
