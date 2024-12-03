package me.sergioramirez.days.day02;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle
{

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\2024\\src\\main\\java\\me\\sergioramirez\\days\\day02\\input.txt";

    private static final List<List<Integer>> reports = init_lists(InputTextUtil.read_all_lines(filepath));

    private static List<List<Integer>> init_lists(List<String> input) {
        return input.stream().map(
                l -> Arrays.stream(l.split("\\s"))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
        ).collect(Collectors.toList());
    }



    public static int count_safe_reports() {
        int safeReports = 0;

        int minDifference = 1;
        int maxDifference = 3;

        for(List<Integer> report: reports) {
            Integer nLeft = null;
            boolean isSafeReport = true;
            boolean isIncremental = true;
            int index = 0;
            for(Integer nActual: report) {
                if(nLeft == null) {
                    nLeft = nActual;
                    index++;
                    continue;
                }
                if(nActual > nLeft) { //Incremental
                    if(index > 1 && !isIncremental) {
                        isSafeReport = false;
                        break;
                    }
                    isIncremental = true;
                }

                if(nActual < nLeft) { //Decremental
                    if(index > 1 && isIncremental) {
                        isSafeReport = false;
                        break;
                    }
                    isIncremental = false;
                }

                if(!isBetween(Math.abs(nLeft - nActual), minDifference, maxDifference)) {
                    isSafeReport = false;
                    break;
                }

                nLeft = nActual;
                index++;
            }
            if(isSafeReport) {
                safeReports++;
            }
        }

        return safeReports;
    }

    public static boolean isBetween(int number, int min, int max) {
        return number >= min && number <= max;
    }



    public static void main( String[] args ) {
        int result_A = count_safe_reports();
        System.out.println(result_A);
    }
}
