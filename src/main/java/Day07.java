import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Day07 extends Days{

    public static void main(String[] args) {
        new Day07();
    }

    public Day07() {
        super(95437L, 24933642L);
    }
    record File(Map<String, File> children, String name, long size){
        public boolean isFile() {
            return children == null;
        }
    }

    class Recursive<T, F> {
        Function<T, F> func;
    }

    @Override
    public Object task1(String data) {
        var root = buildTree(data);
        var folderSizes = getFolderSizes(root);
        return folderSizes.entrySet().stream()
                .filter(e->e.getValue() < 100000L)
                .mapToLong(e->e.getValue()).sum();
    }

    @Override
    public Object task2(String data) {
        var totalSize = 70000000L;
        var requiredSize = 30000000L;
        var root = buildTree(data);
        var folderSizes = getFolderSizes(root);
        var neededSpace = requiredSize - (totalSize - folderSizes.get(root));
        return folderSizes.values()
                .stream().filter(size -> size >= neededSpace)
                .sorted().findFirst().get();
    }

    /**
     * We will assume the input data is a valid console output
     */
    public File buildTree(String data) {
        var queue = new LinkedList<String>(Arrays.asList(data.split("\n")));
        var pathStack = new LinkedList<File>();
        var root = new File(new HashMap<>(), "/", 0);
        pathStack.add(root);
        while (!queue.isEmpty()) {
            var line = queue.pollFirst();
            var split = line.split(" ");
            switch(split[1]) {
                case "cd" -> {
                    switch (split[2]) {
                        case "/" -> {
                            pathStack.clear();
                            pathStack.add(root);
                        }
                        case ".." -> {
                            pathStack.pollLast();
                        }
                        default -> {
                            // we will assume ls will be performed before cd
                            pathStack.addLast(pathStack.getLast().children.get(split[2]));
                        }
                    }
                }
                case "ls" -> {
                    while (!queue.isEmpty() && !queue.peekFirst().startsWith("$")) {
                        line = queue.pollFirst();
                        split = line.split(" ");
                        switch (split[0]) {
                            case "dir" -> {
                                pathStack.getLast().children.putIfAbsent(split[1],
                                        new File(new HashMap<>(), split[1], 0));
                            }
                            default -> {
                                pathStack.getLast().children.putIfAbsent(split[1],
                                        new File(null, split[1], Long.parseLong(split[0])));
                            }
                        }
                    }
                }
            }
        }
        return root;
    }


    private Map<File, Long> getFolderSizes(File root) {
        Map<File, Long> folderSizes = new HashMap<>();
        var recursive = new Recursive<File, Long>();
        recursive.func = (f)->{
            if (f.isFile()) {
                return f.size;
            }
            folderSizes.putIfAbsent(f,
                    f.children.values().stream()
                            .mapToLong(recursive.func::apply)
                            .sum());
            return folderSizes.get(f);
        };
        recursive.func.apply(root);
        return folderSizes;
    }


}
