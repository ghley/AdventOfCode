import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Day13 extends Days{
    public static void main(String[] args) {
        new Day13();
    }

    public Day13() {
        super(13, 140);
    }

    enum TokenType {
        LIST_OPEN, LIST_CLOSE, NUMBER
    }

    record Token(TokenType type, int value) {

    }

    @Override
    public Object task1(String data) {
        var comparisons = data.split("\n\n");
        int sum = 0;
        for (int q = 0; q < comparisons.length; q++) {
            var lines = comparisons[q].split("\n");
            var stack1 = toStack(lines[0]);
            var stack2 = toStack(lines[1]);
            int wrongOrder = comparator(stack1, stack2);
            if (wrongOrder < 0) {
                sum += (q+1);
            }
        }
        return sum;
    }

    private int comparator(LinkedList<Token> s1, LinkedList<Token> s2) {
        var stack1 = new LinkedList<Token>(s1);
        var stack2 = new LinkedList<Token>(s2);
        boolean wrongOrder = false;
        while (true) {
            if (stack1.isEmpty() && stack2.isEmpty()) {
                // not sure?
                break;
            }
            if (stack1.isEmpty() && !stack2.isEmpty()) {
                break;
            }
            if (stack2.isEmpty() && !stack1.isEmpty()) {
                wrongOrder = true;
                break;
            }
            var t1 = stack1.pop();
            var t2 = stack2.pop();
            if (t1.type == t2.type) {
                if (t1.type == TokenType.LIST_OPEN || t1.type == TokenType.LIST_CLOSE) {
                    continue;
                }
                if (t1.value > t2.value) {
                    wrongOrder = true;
                    break;
                }else if (t1.value < t2.value) {
                    break;
                }
            } else if (t1.type == TokenType.LIST_OPEN && t2.type == TokenType.NUMBER) {
                stack2.push(new Token(TokenType.LIST_CLOSE, -1));
                stack2.push(t2);
            } else if (t2.type == TokenType.LIST_OPEN && t1.type == TokenType.NUMBER) {
                stack1.push(new Token(TokenType.LIST_CLOSE, -1));
                stack1.push(t1);
            } else if (t1.type == TokenType.LIST_CLOSE && t2.type == TokenType.NUMBER) {
                break;
            } else if (t2.type == TokenType.LIST_CLOSE && t1.type == TokenType.NUMBER) {
                wrongOrder = true;
                break;
            } else if (t1.type == TokenType.LIST_OPEN && t2.type == TokenType.LIST_CLOSE) {
                wrongOrder = true;
                break;
            } else if (t2.type == TokenType.LIST_OPEN && t1.type == TokenType.LIST_CLOSE) {
                break;
            }else {
                throw new RuntimeException("This shouldn't happen "+t1+" "+t2);
            }
        }
        if (wrongOrder) {
            return 1;
        }
        return -1;
    }

    private LinkedList<Token> toStack(String str) {
        StreamTokenizer st = new StreamTokenizer(new StringReader(str));
        LinkedList<Token> tokens = new LinkedList<>();
        try {
            int nextToken = 0;
            while ((nextToken = st.nextToken()) != StreamTokenizer.TT_EOF) {
                switch (nextToken) {
                    case StreamTokenizer.TT_NUMBER -> {
                        tokens.add(new Token(TokenType.NUMBER, (int)st.nval));
                    }
                    case StreamTokenizer.TT_WORD -> {
                        throw new RuntimeException("This shouldn't happen");
                    }
                    default -> {
                        switch ((char)nextToken) {
                            case '['->{
                                tokens.add(new Token(TokenType.LIST_OPEN, -1));
                            }
                            case ']'->{
                                tokens.add(new Token(TokenType.LIST_CLOSE, -1));
                            }
                            case ','-> {
                                // ignore
                            }
                            default->{
                                throw new RuntimeException("This shouldn't happen");
                            }
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return tokens;
    }

    record PacketBean(String str, LinkedList<Token> tokens) {

    }

    @Override
    public Object task2(String data) {
        var allLines = data.replace("\n\n","\n") + "\n[[2]]\n[[6]]";
        var list = Arrays.stream(allLines.split("\n"))
                .map(l -> new PacketBean(l, toStack(l)))
                .sorted(Comparator.comparing(PacketBean::tokens, this::comparator))
                .collect(Collectors.toList());
        int mul = 1;
        for (int q = 0; q < list.size(); q++) {
            if (list.get(q).str.equals("[[2]]")) {
                mul *= (q+1);
            }else if (list.get(q).str.equals("[[6]]")) {
                mul *= (q+1);
                break;
            }
        }
        return mul;
    }
}
