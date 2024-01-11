package me.sergioramirez.days.day21;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day21\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final String[][] grid = init_grid();
    private static final Map<String, List<String>> graph = init_graph();
    private static final Map<Plot, List<Plot>> graph_plot = init_graph_plot();

    private static String[][] init_grid() {
        String[][] grid = new String[input.size()][];
        for(int r=0; r<input.size(); r++) {
            grid[r] = new String[input.get(r).length()];
            for(int c=0; c < input.get(r).length(); c++)
                grid[r][c] = String.valueOf(input.get(r).charAt(c));
        }
        return grid;
    }

    private static Map<String, List<String>> init_graph() {
        Map<String, List<String>> graph = new HashMap<>();
        for(int r=0; r < grid.length; r++) {
            for(int c=0; c < grid[r].length; c++) {
                String pos = grid[r][c];
                if(pos.equals("#"))
                    continue;
                String node = c + "|" + r;
                List<String> neighbors = get_neighbors(c, r);
                graph.put(node, neighbors);
            }
        }
        return graph;
    }

    private static Map<Plot, List<Plot>> init_graph_plot() {
        Map<Plot, List<Plot>> graph = new HashMap<>();
        for(int r=0; r < grid.length; r++) {
            for(int c=0; c < grid[r].length; c++) {
                String pos = grid[r][c];
                if(pos.equals("#"))
                    continue;
                List<Plot> neighbors = get_neighbors_plot(c, r);
                graph.put(new Plot(c, r, 0), neighbors);
            }
        }
        return graph;
    }

    private static List<String> get_neighbors(int c, int r) {
        List<String> neighbors = new ArrayList<>();
        List<String> dirs = List.of("1|0", "0|1", "-1|0", "0|-1");
        for(String dir: dirs) {
            int dx = Integer.parseInt(dir.split("\\|")[0]);
            int dy = Integer.parseInt(dir.split("\\|")[1]);
            int nx = c+dx;
            int ny = r+dy;
            if(nx >= 0 && nx < grid[0].length && ny >= 0 && ny < grid.length && !grid[ny][nx].equals("#")) {
                neighbors.add(nx + "|" + ny);
            }
        }
        return neighbors;
    }

    private static List<Plot> get_neighbors_plot(int c, int r) {
        List<Plot> neighbors = new ArrayList<>();
        List<String> dirs = List.of("1|0", "0|1", "-1|0", "0|-1");
        for(String dir: dirs) {
            int dx = Integer.parseInt(dir.split("\\|")[0]);
            int dy = Integer.parseInt(dir.split("\\|")[1]);
            int nx = c+dx;
            int ny = r+dy;
            if(nx >= 0 && nx < grid[0].length && ny >= 0 && ny < grid.length && !grid[ny][nx].equals("#")) {
                neighbors.add(new Plot(nx, ny, 0));
            }
        }
        return neighbors;
    }

    //Recursivo ineficiente
    private static int plots_reached_v1(int steps_remaining) {
        String start = get_start();

        HashSet<String> plots_reached = new HashSet<>();
        walk(start, steps_remaining, plots_reached);

        return plots_reached.size();
    }

    private static int plots_reached_v2(int sc, int sr, int steps_remaining) {

        long startTime = System.currentTimeMillis();

        Plot start = new Plot(sc, sr, steps_remaining);

        ArrayDeque<Plot> nodes = new ArrayDeque<>(){{
            add(start);
        }};

        HashSet<Plot> seen = new HashSet<>(){{
            add(start);
        }};

        HashSet<Plot> plots_reached = new HashSet<>();

        while(!nodes.isEmpty()) {

            Plot plot = nodes.poll();
            int remaining = plot.getRemaining();
            if((remaining % 2) == 0)
                plots_reached.add(plot);

            if(remaining == 0)
                continue;

            List<Plot> neighbors = graph_plot.get(plot);

            for(Plot neighbor: neighbors) {
                if(!seen.contains(neighbor)) {
                    neighbor.setRemaining(remaining-1);
                    nodes.add(neighbor);
                    seen.add(neighbor);
                }
            }

        }

        System.out.println("Tiempo: " + ((System.currentTimeMillis() - startTime)));

        return plots_reached.size();
    }

    private static void walk(String plot, int remaining, HashSet<String> plots_reached) {
        if(remaining == 0) {
            plots_reached.add(plot);
            return;
        }

        List<String> neighbors = graph.get(plot);
        for(String neighbor: neighbors) {
            walk(neighbor, remaining-1, plots_reached);
        }
    }

    private static String get_start() {
        String start = "";
        for(int r=0; r < grid.length; r++)
            for(int c=0; c < grid[r].length; c++)
                if(grid[r][c].equals("S"))
                    return c + "|" + r;
        return start;
    }

    private static Plot get_start_plot() {
        Plot start = null;
        for(int r=0; r < grid.length; r++)
            for(int c=0; c < grid[r].length; c++)
                if(grid[r][c].equals("S")) {
                    start = new Plot(c, r, 0);
                    break;
                }
        return start;
    }

    public static long plots_reached_B() {
        Plot start = get_start_plot();
        int size = grid.length;
        int steps = 26501365;
        int sc = start.getC();
        int sr = start.getR();

        long grid_width = Math.floorDiv(steps, size) - 1;

        long odd = (long) Math.pow((Math.floorDiv(grid_width, 2) * 2 + 1), 2);
        long even = (long) Math.pow((Math.floorDiv((grid_width + 1), 2) * 2), 2);

        long odd_points = plots_reached_v2(sr, sc, size * 2 + 1);
        long even_points = plots_reached_v2(sr, sc, size * 2);

        long corner_t = plots_reached_v2(size - 1, sc, size - 1);
        long corner_r = plots_reached_v2(sr, 0, size - 1);
        long corner_b = plots_reached_v2(0, sc, size - 1);
        long corner_l = plots_reached_v2(sr, size - 1, size - 1);

        long small_tr = plots_reached_v2(size - 1, 0, Math.floorDiv(size, 2) - 1);
        long small_tl = plots_reached_v2(size - 1, size - 1, Math.floorDiv(size, 2) - 1);
        long small_br = plots_reached_v2(0, 0, Math.floorDiv(size, 2) - 1);
        long small_bl = plots_reached_v2(0, size - 1, Math.floorDiv(size, 2) - 1);

        long large_tr = plots_reached_v2(size - 1, 0, Math.floorDiv(size * 3, 2) - 1);

        long large_tl = plots_reached_v2(size - 1, size - 1, Math.floorDiv(size * 3, 2) - 1);
        long large_br = plots_reached_v2(0, 0, Math.floorDiv(size * 3, 2) - 1);
        long large_bl = plots_reached_v2(0, size - 1, Math.floorDiv(size * 3, 2) - 1);

        return odd * odd_points +
                even * even_points +
                corner_t + corner_r + corner_b + corner_l +
                (grid_width + 1) * (small_tr + small_tl + small_br + small_bl) +
                grid_width * (large_tr + large_tl + large_br + large_bl);
    }



    public static void main(String[] args) {
        Plot start = get_start_plot(); //S
        int result_A = plots_reached_v2(start.getC(), start.getR(), 64);
        System.out.println(result_A);
        long result_B = plots_reached_B();
        System.out.println(result_B);
    }


}
