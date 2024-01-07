package me.sergioramirez.days.day23;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day23\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final String[][] hikingMap = init_hiking_map();

    private static String[][] init_hiking_map() {
        String[][] map = new String[input.size()][];
        for(int y=0; y < input.size(); y++) {
            String line = input.get(y);
            map[y] = new String[line.length()];
            for(int x=0; x < line.length(); x++)
                map[y][x] = String.valueOf(line.charAt(x));
        }
        return map;
    }

    private static Integer longest_hike() {
        LinkedList<Step> queue = new LinkedList<>();
        queue.add(new Step(1, 0, 0, new HashSet<>()));
        int count = 0;
        Map<Step, Integer> history_max = new HashMap<>();
        while(!queue.isEmpty()) {
            Step step = queue.poll();
            if(history_max.containsKey(step) && history_max.get(step) >= step.getCount())
                continue;
            history_max.put(step, step.getCount());
            if(step.getVisited().contains(step))
                continue;
            if(step.getY() == hikingMap.length-1) {
                count = Math.max(count, step.getCount());
                continue;
            }
            List<String> positions = get_positions(step.getX(), step.getY(), true);
            LinkedHashSet<Step> new_seen = new LinkedHashSet<>(step.getVisited());
            new_seen.add(step);
            positions.forEach(p -> queue.add(new Step(Integer.parseInt(p.split("\\|")[0]), Integer.parseInt(p.split("\\|")[1]), step.getCount()+1, new_seen)));

        }
        return count;
    }

    private static Integer longest_hike_cyclic() {
        HashSet<String> intersections = obtain_intersections();
        int xStart = IntStream.range(0, hikingMap[0].length).filter(x -> hikingMap[0][x].equals(".")).findFirst().getAsInt();
        int xEnd = IntStream.range(0, hikingMap[hikingMap.length-1].length).filter(x -> hikingMap[hikingMap.length-1][x].equals(".")).findFirst().getAsInt();
        String start = xStart+"|0";
        String end = xEnd + "|" + (hikingMap.length-1);
        intersections.add(start);
        intersections.add(end);
        Map<String, Map<String, Integer>> graph = obtain_graph(intersections);
        return dfs(start, end, new HashSet<>(), graph);
    }

    private static int dfs(String pointSource, String endPoint, HashSet<String> seen, Map<String, Map<String, Integer>> graph) {
        if(pointSource.equals(endPoint))
            return 0;

        int m = Integer.MIN_VALUE;

        seen.add(pointSource);
        Map<String, Integer> paths = graph.get(pointSource);
        for(String pointDest: paths.keySet()) {
            if(!seen.contains(pointDest))
                m = Math.max(m, dfs(pointDest, endPoint, seen, graph) + graph.get(pointSource).get(pointDest));
        }
        seen.remove(pointSource);

        return m;
    }

    private static Map<String, Map<String, Integer>> obtain_graph(HashSet<String> intersections) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();
        LinkedList<Step> queue = new LinkedList<>();

        for(String pointSource: intersections) {
            String[] pointArr = pointSource.split("\\|");
            int xSource = Integer.parseInt(pointArr[0]);
            int ySource = Integer.parseInt(pointArr[1]);
            HashSet<String> seen = new HashSet<>() {{
                add(pointSource);
            }};
            queue.add(new Step(xSource, ySource, 0));

            while(!queue.isEmpty()) {
                Step step = queue.poll();
                if(step.getCount() != 0 && intersections.contains(step.getPointStr())) {
                    if(!graph.containsKey(pointSource)) graph.put(pointSource, new HashMap<>());
                    graph.get(pointSource).put(step.getPointStr(), step.getCount());
                    continue;
                }
                List<String> neighbors = get_positions(step.getX(), step.getY(), false);
                neighbors.stream().filter(n -> !seen.contains(n)).forEach(n -> {
                    String[] neighArr = n.split("\\|");
                    queue.add(new Step(Integer.parseInt(neighArr[0]), Integer.parseInt(neighArr[1]), step.getCount()+1));
                });
                seen.addAll(neighbors);

            }
        }

        return graph;
    }

    private static HashSet<String> obtain_intersections() {
        HashSet<String> intersections = new HashSet<>();
        List<String> interTemp;
        for(int y=0; y < hikingMap.length; y++) {
            String[] row = hikingMap[y];
            for(int x = 0; x < row.length; x++) {
                if(hikingMap[y][x].equals("#"))
                    continue;
                interTemp = get_positions(x, y, false);
                if(interTemp.size() >= 3)
                    intersections.add(x + "|" + y);
            }
        }
        return intersections;
    }

    private static List<String> get_positions(int x, int y, boolean slip) {

        List<String> positions = new ArrayList<>();

        if(slip) {
            if(hikingMap[y].length > x+1 &&
                    !hikingMap[y][x+1].equals("#") && !hikingMap[y][x+1].equals("<")) {
                positions.add((x+1) + "|" + y);
            }
            if(x-1 >= 0 &&
                    !hikingMap[y][x-1].equals("#") && !hikingMap[y][x-1].equals(">")) {
                positions.add((x-1) + "|" + y);
            }
            if(hikingMap.length > y+1 &&
                    !hikingMap[y+1][x].equals("#") && !hikingMap[y+1][x].equals("^")) {
                positions.add(x + "|" + (y+1));
            }
            if(y-1 >= 0 &&
                    !hikingMap[y-1][x].equals("#") && !hikingMap[y-1][x].equals("v")) {
                positions.add(x + "|" + (y-1));
            }
            return positions;
        }

        if(hikingMap[y].length > x+1 &&
                !hikingMap[y][x+1].equals("#")) {
            positions.add((x+1) + "|" + y);
        }
        if(x-1 >= 0 &&
                !hikingMap[y][x-1].equals("#")) {
            positions.add((x-1) + "|" + y);
        }
        if(hikingMap.length > y+1 &&
                !hikingMap[y+1][x].equals("#")) {
            positions.add(x + "|" + (y+1));
        }
        if(y-1 >= 0 &&
                !hikingMap[y-1][x].equals("#")) {
            positions.add(x + "|" + (y-1));
        }

        return positions;
    }

    public static void main(String[] args) {
        Integer result_A = longest_hike();
        System.out.println(result_A);
        Integer result_B = longest_hike_cyclic();
        System.out.println(result_B);
    }
}
