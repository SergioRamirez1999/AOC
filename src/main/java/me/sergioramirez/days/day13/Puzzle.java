package me.sergioramirez.days.day13;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day13\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);

    private static final List<List<List<String>>> patterns = new ArrayList<>();

    private static void init_patterns() {
        List<List<String>> pattern = new ArrayList<>();
        for(int i=0; i<input.size(); i++) {
            String line = input.get(i);
            if(!line.isEmpty()) {
                pattern.add(List.of(line.split("")));
                if(i < input.size()-1) continue;
            }
            patterns.add(new ArrayList<>(pattern));
            pattern.clear();
        }
    }

    private static long summarize_pattern() {
        long result = 0L;

        for(List<List<String>> pattern: patterns) {

            boolean rowMatch = false;
            boolean colMatch = false;
            long rowsReflected = 0;
            long colsReflected = 0;


            for (int i = 0; i < (pattern.size()-1) && !rowMatch; i++) {
                int finalI = i;
                rowMatch = IntStream.range(0, pattern.get(i).size()).allMatch(v -> pattern.get(finalI).get(v).equals(pattern.get(finalI + 1).get(v)));
                if (rowMatch) {
                    List<List<String>> sub1 = pattern.subList(0, i).reversed();
                    List<List<String>> sub2 = new ArrayList<>();
                    if (i + 2 <= pattern.size()) {
                        sub2 = pattern.subList(i + 2, pattern.size());
                    }
                    int length = Math.min(sub1.size(), sub2.size());
                    boolean allReflected = true;
                    for (int h = 0; h < length && allReflected; h++) {
                        String ref1 = String.join("", sub1.get(h));
                        String ref2 = String.join("", sub2.get(h));
                        allReflected = ref1.equals(ref2);
                    }
                    if (!allReflected)
                        rowMatch = false;
                    else
                        rowsReflected = (i + 1) * 100L;

                }
            }

            if(rowsReflected == 0L) { // Si encontró reflexion en filas no hay que buscar en columnas
                //cols
                for(int i=0; i<(pattern.get(0).size()-1) && !colMatch; i++) {
                    int finalI = i;
                    String col1 = IntStream.range(0, pattern.size()).mapToObj(y -> pattern.get(y).get(finalI)).collect(Collectors.joining());
                    String col2 = IntStream.range(0, pattern.size()).mapToObj(y -> pattern.get(y).get(finalI+1)).collect(Collectors.joining());
                    colMatch = col1.equals(col2);
                    if(colMatch) {
                        List<List<String>> sub1 = new ArrayList<>();

                        for(int h=0; h < i; h++) {
                            List<String> sub1Aux = new ArrayList<>();
                            for(int z=0; z < pattern.size(); z++) {
                                sub1Aux.add(pattern.get(z).get(h));
                            }
                            sub1.add(sub1Aux);
                        }

                        sub1 = sub1.reversed();

                        List<List<String>> sub2 = new ArrayList<>();
                        if(i+2 <= pattern.size()) {
                            for(int h=i+2; h < pattern.get(i).size(); h++) {
                                List<String> sub2Aux = new ArrayList<>();
                                for(int z=0; z < pattern.size(); z++) {
                                    sub2Aux.add(pattern.get(z).get(h));
                                }
                                sub2.add(sub2Aux);
                            }
                        }
                        int length = Math.min(sub1.size(), sub2.size());
                        boolean allReflected = true;
                        for(int h=0; h<length && allReflected; h++) {
                            String ref1 = String.join("", sub1.get(h));
                            String ref2 = String.join("", sub2.get(h));
                            allReflected = ref1.equals(ref2);
                        }
                        if(!allReflected)
                            colMatch = false;
                        else
                            colsReflected = i+1;

                    }
                }
            }

            result += rowsReflected + colsReflected;
        }

        return result;
    }

    public static void main(String[] args) {
        init_patterns();
        long result_A = summarize_pattern();
        System.out.println(result_A);
    }

}
