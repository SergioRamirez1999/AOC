package me.sergioramirez.days.day17;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;

public class Puzzle {
    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day17\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);

    private static final String[][] grid = init_grid();

    private static String[][] init_grid() {
        String[][] grid = new String[input.size()][];
        for(int r=0; r<input.size(); r++) {
            grid[r] = new String[input.get(r).length()];
            for(int c=0; c < input.get(r).length(); c++)
                grid[r][c] = String.valueOf(input.get(r).charAt(c));
        }
        return grid;

    }

    private static Integer shortest_path(int minSameDir, int maxSameDir) {
        int min = 0;

        //length, r, c, directionR, directionC, stepDirection
        //150|01|01|-1|0|2
        String start = "0|0|0|0|0|0";

        HashSet<String> seen = new HashSet<>();

        PriorityQueue<String> queue_step = new PriorityQueue<>((s1, s2) -> {
            Integer n1 = Integer.parseInt(s1.split("\\|")[0]);
            Integer n2 = Integer.parseInt(s2.split("\\|")[0]);
            return  n1.compareTo(n2);
        }) {{
            add(start);
        }};

        while(!queue_step.isEmpty()) {

            String stepStr = queue_step.poll();
            String[] stepArr = stepStr.split("\\|");
            int weight = Integer.parseInt(stepArr[0]);
            int row = Integer.parseInt(stepArr[1]);
            int col = Integer.parseInt(stepArr[2]);
            int rowDir = Integer.parseInt(stepArr[3]);
            int colDir = Integer.parseInt(stepArr[4]);
            int stepsDir = Integer.parseInt(stepArr[5]);

            if(row == grid.length-1 && col == grid[0].length-1) {
                min = weight;
                break;
            }

            if(seen.contains(stepStr.substring(stepStr.indexOf("|")+1)))
                continue;

            seen.add(stepStr.substring(stepStr.indexOf("|")+1));

            if(stepsDir < maxSameDir && !get_direction_str(stepStr).equals("00")) {
                int nRow = row + rowDir;
                int nCol = col + colDir;
                if(nRow >= 0 && nRow < grid.length && nCol >= 0 && nCol < grid[0].length) {
                    queue_step.add(String.join("|", String.valueOf(weight + Integer.parseInt(grid[nRow][nCol])), String.valueOf(nRow), String.valueOf(nCol), String.valueOf(rowDir), String.valueOf(colDir), String.valueOf(stepsDir+1)));
                }
            }

            String[] dirsArr = "0,1|1,0|0,-1|-1,0".split("\\|");
            if(maxSameDir == 4 || (stepsDir == 0 || stepsDir >= minSameDir)) {
                for (String s : dirsArr) {
                    String[] aux = s.split(",");
                    int ndr = Integer.parseInt(aux[0]);
                    int ndc = Integer.parseInt(aux[1]);
                    if ((ndr != rowDir || ndc != colDir) && (ndr != -rowDir || ndc != -colDir)) {
                        int nr = row + ndr;
                        int nc = col + ndc;
                        if (nr >= 0 && nr < grid.length && nc >= 0 && nc < grid[0].length) {
                            queue_step.add(String.join("|", String.valueOf( weight + Integer.parseInt(grid[nr][nc])), String.valueOf(nr), String.valueOf(nc), String.valueOf(ndr), String.valueOf(ndc), "1"));
                        }
                    }
                }
            }

        }

        return min;
    }

    private static String get_direction_str(String stepStr) {
        return stepStr.split("\\|")[3] + stepStr.split("\\|")[4];
    }


    public static void main(String[] args) {
        Integer result_A = shortest_path(0, 3);
        System.out.println(result_A);
        Integer result_B = shortest_path(4, 10);
        System.out.println(result_B);
    }
}
