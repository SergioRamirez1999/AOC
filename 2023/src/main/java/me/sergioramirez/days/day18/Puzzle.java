package me.sergioramirez.days.day18;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day18\\input.txt";
    private static List<String> input = InputTextUtil.read_all_lines(filepath);
    private static int perimeter = 0;

    private static long calcule_area() {
        long area = 0L;
        perimeter = 0;
        List<Position> digs = process_dig_plan();
        List<Position> left = digs.subList(0, digs.size()-1);
        List<Position> right = digs.subList(1, digs.size());
        for(int i=0; i < left.size(); i++) {
            Position p1 = left.get(i);
            Position p2 = right.get(i);
            area += (long) (p2.getX() - p1.getX()) * (p2.getY()+p1.getY());
        }
        return Math.abs(area)/2 + perimeter/2 + 1;
    }

    private static List<Position> process_dig_plan() {
        List<Position> digs = new ArrayList<>();
        int[] x_y_d = new int[]{0,0,0};
        int[] in_x = new int[]{0,1,0,-1};
        int[] in_y = new int[]{-1,0,1,0};
        digs.add(new Position(x_y_d[0], x_y_d[1]));
        for(String line: input) {
            String direction = line.split(" ")[0];
            int meters = Integer.parseInt(line.split(" ")[1]);
            perimeter+=meters;
            setDirection(direction, x_y_d);
            steps(x_y_d, in_x, in_y, meters, digs);
        }
        return digs;
    }

    private static void steps(int[] x_y_d, int[] in_x, int[] in_y, int meters, List<Position> digs) {
        int move_x = Math.abs(in_x[x_y_d[2]]*meters);
        int move_y = Math.abs(in_y[x_y_d[2]]*meters);
        for(int i=0; i < move_x; i++) {
            x_y_d[0] += in_x[x_y_d[2]];
        }
        for(int i=0; i < move_y; i++) {
            x_y_d[1] += in_y[x_y_d[2]];
        }
        digs.add(new Position(x_y_d[0], x_y_d[1]));

    }

    private static void setDirection(String direction, int[] x_y_d) {
        switch (direction) {
            case "U", "3":
                x_y_d[2] = 0;
                break;
            case "R", "0":
                x_y_d[2] = 1;
                break;
            case "D", "1":
                x_y_d[2] = 2;
                break;
            default:
                x_y_d[2] = 3;
                break;
        }
    }

    private static List<String> reinterpret_input() {
        List<String> input2 = new ArrayList<>();
        for(String line: input) {
            String color = line.split(" ")[2];
            String direction = color.substring(color.length()-2, color.length()-1);
            String meters = String.valueOf(Integer.parseInt(color.substring(2, color.length()-2),16));
            input2.add(String.join(" ", direction, meters, color));
        }
        return input2;
    }

    public static void main(String[] args) {
        long result_A = calcule_area();
        System.out.println(result_A);
        input = reinterpret_input();
        long result_B = calcule_area();
        System.out.println(result_B);
    }
}
