import java.util.*;

public class Day12 extends Days {

    public static void main(String[] args) {
        new Day12();
    }

    public Day12() {
        super(31L, 29L);
    }

    record Map(int[][] heightMap, Vec start, Vec end) {
        int getWidth() {
            return heightMap.length;
        }
        int getHeight() {
            return heightMap[0].length;
        }
    }

    private Map buildHeightMap(String data) {
        var splits = data.split("\n");
        var map = new int[splits[0].length()][splits.length];
        int[] start = new int[2];
        int[] end = new int[2];
        for (int y = 0; y < splits.length; y++) {
            var array = splits[y].toCharArray();
            for (int x = 0; x < array.length; x++) {
                map[x][y] = array[x] - 'a';
                if (map[x][y] < 0) {
                    if (array[x] == 'S') {
                        map[x][y] = 0;
                        start[0] = x;
                        start[1] = y;
                    } else {
                        map[x][y] = 'z' - 'a';
                        end[0] = x;
                        end[1] = y;
                    }
                }
            }
        }
        return new Map(map, new Vec(start[0], start[1]), new Vec(end[0], end[1]));
    }

    record Vec(int x, int y) {
        public int distance(Vec vec) {
            return Math.abs(vec.x - x) + Math.abs(vec.y - y);
        }

        Vec add(Vec vec) {
            return new Vec(vec.x + x, vec.y + y);
        }
    }

    record Node(Vec pos, Node parent, int currCost, int heuristicScore) {
        int getTotalScore() {
            return currCost + heuristicScore;
        }
    }

    @Override
    public Object task1(String data) {
        var map = buildHeightMap(data);

        var startNode = new Node(map.start, null, 0, map.end.distance(map.start));

        var priorityQueue = new PriorityQueue<Node>(Comparator.comparingInt(Node::getTotalScore));
        priorityQueue.add(startNode);
        HashSet<Vec> visited = new HashSet<>();

        var dirs = new Vec[]{
                new Vec(0, 1),
                new Vec(0, -1),
                new Vec(-1, 0),
                new Vec(1, 0)};

        Node resultNode = null;
        while (!priorityQueue.isEmpty()) {
            var curr = priorityQueue.poll();
            if (curr.pos.distance(map.end) == 0) {
                resultNode = curr;
                break;
            }
            visited.add(curr.pos);
            for (Vec dir : dirs) {
                var newPos = dir.add(curr.pos);
                if (newPos.x < 0 || newPos.x >= map.getWidth() || newPos.y < 0 || newPos.y >= map.getHeight()) {
                    continue;
                }
                if (map.heightMap[newPos.x][newPos.y] - map.heightMap[curr.pos.x][curr.pos.y] > 1) {
                    continue;
                }
                if (visited.contains(newPos)) {
                    continue;
                }
                var nextNode = new Node(newPos, curr, curr.currCost+1, newPos.distance(map.end));
                priorityQueue.add(nextNode);
            }
        }
        if (resultNode == null) {
            return -1L;
        }
        return resultNode.currCost;
    }

    // Overall could be more efficient
    @Override
    public Object task2(String data) {
        var map = buildHeightMap(data);

        var startQueue = new PriorityQueue<Node>(Comparator.comparing(Node::getTotalScore));


        // we could filter better to reduce the search space but it is probably not worth it
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (map.heightMap[x][y] == 0) {
                    startQueue.add(new Node(new Vec(x,y), null, 0, map.end.distance(new Vec(x,y))));
                }
            }
        }

        HashSet<Vec> visitedStarts = new HashSet<>();

        long shortest = Long.MAX_VALUE;

        while (!startQueue.isEmpty()) {
            var startNode = startQueue.poll();
            var priorityQueue = new PriorityQueue<Node>(Comparator.comparingInt(Node::getTotalScore));
            priorityQueue.add(startNode);
            HashSet<Vec> visited = new HashSet<>();

            var dirs = new Vec[]{
                    new Vec(0, 1),
                    new Vec(0, -1),
                    new Vec(-1, 0),
                    new Vec(1, 0)};

            Node resultNode = null;
            while (!priorityQueue.isEmpty()) {
                var curr = priorityQueue.poll();
                if (visitedStarts.contains(curr.pos)) {
                    continue;
                }
                if (curr.currCost > shortest) {
                    break;
                }
                if (curr.pos.distance(map.end) == 0) {
                    resultNode = curr;
                    break;
                }
                visited.add(curr.pos);
                for (Vec dir : dirs) {
                    var newPos = dir.add(curr.pos);
                    if (newPos.x < 0 || newPos.x >= map.getWidth() || newPos.y < 0 || newPos.y >= map.getHeight()) {
                        continue;
                    }
                    if (map.heightMap[newPos.x][newPos.y] - map.heightMap[curr.pos.x][curr.pos.y] > 1) {
                        continue;
                    }
                    if (visited.contains(newPos)) {
                        continue;
                    }
                    var nextNode = new Node(newPos, curr, curr.currCost + 1, newPos.distance(map.end));
                    priorityQueue.add(nextNode);
                }
            }
            if (resultNode != null) {
                shortest = Math.min(resultNode.currCost, shortest);
            }
            visitedStarts.add(startNode.pos);
        }
        return shortest;
    }
}
