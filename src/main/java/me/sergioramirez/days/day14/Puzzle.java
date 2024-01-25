package me.sergioramirez.days.day14;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle {
    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day14\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static String[][] ground = init_ground();

    private static String[][] init_ground() {
        String[][] ground = new String[input.size()][input.get(0).length()];
        for(int h=0; h < input.size(); h++) {
            String line = input.get(h);
            for(int i=0; i < line.length(); i++) {
                ground[h][i] = String.valueOf(line.charAt(i));
            }
        }
        return ground;
    }

    private static long calcule_load() {
        long result = 0;

        for(int y=0; y < ground.length; y++) {
            for(int yb=0; yb < ground.length; yb++) {
                int up = yb-1;
                if(0<=up) {
                    for(int x=0; x < ground[yb].length; x++) {
                        if(ground[yb][x].equals("O") && ground[up][x].equals(".")) {
                            String auxUp = ground[up][x];
                            ground[up][x] = ground[yb][x];
                            ground[yb][x] = auxUp;
                        }
                    }
                }
            }
        }

        for(int y=ground.length-1; y >= 0; y--) {
            result += Arrays.stream(ground[y]).filter(x -> x.equals("O")).count() * (ground.length - y);
        }

        return result;
    }

    private static long calcule_load_cycles() {
        long result = 0;
        LinkedHashSet<String> seen = new LinkedHashSet<>(){{add(matrixToString(ground));}};
        List<String> cycles = new ArrayList<>(){{add(matrixToString(ground));}};

        int it = 0;

        while(true) {
            it++;
            for(int h=0; h < 4; h++)
                flipGround();
            if(seen.contains(matrixToString(ground)))
                break;
            seen.add(matrixToString(ground));
            cycles.add(matrixToString(ground));
        }

        int indexOriginal = cycles.indexOf(matrixToString(ground)); //Index del original, ground es la copia


        /*
            ciclos=100
            it=3
            indexOriginal=0
            indexCycle = (100-0) % (3-0) + 0 = 1 ===> Un ciclo más que la ocurrencia del original.
            arr[0] = original
            arr[1] = proximoCiclo
            arr[n] = copia
            arr[n+1] = proximoCiclo
            arr[0] == arr[n]
            arr[1] == arr[n+1]
        */
        String cycleStr = cycles.get((1000000000 - indexOriginal) % (it - indexOriginal) + indexOriginal); //Obtiene el index del MCM y le suma el index offset (ciclos faltantes)

        String[][] groundCycle = Arrays.stream(cycleStr.split("\\|")).map(r -> r.split("")).toArray(String[][]::new);

        for(int y=groundCycle.length-1; y >= 0; y--) {
            long zeros = Arrays.stream(groundCycle[y]).filter(x -> x.equals("O")).count();
            long pos = (groundCycle.length - y);
            result += zeros * pos;
        }

        return result;
    }

    private static String matrixToString(String[][] matrix) {
        List<String> aux = new ArrayList<>();
        for(String[] row: matrix) {
            aux.add(String.join("", row));
        }
        return String.join("|", aux);
    }

    //Se utiliza la reversa de la matriz transpuesta para rotar 90 grados en sentido del reloj, 4 veces equivale a este-sur-oeste-norte
    private static void flipGround() {

        List<String> flipped = new ArrayList<>();
        for(int c=0; c < ground.length; c++) {
            StringBuilder sbRow = new StringBuilder();
            for (String[] strings : ground) {
                sbRow.append(strings[c]);
            }
            flipped.add(sbRow.toString());
        }

        for(int i = 0; i < flipped.size(); i++)
            ground[i] = flipped.get(i).split("");


        for(int i=0; i < flipped.size(); i++) {
            String row = Arrays.stream(flipped.get(i).split("#", -1)).map(s -> Arrays.stream(s.split("")).sorted(Comparator.reverseOrder()).toList()).map(l -> String.join("", l)).collect(Collectors.joining("#"));
            ground[i] = new StringBuilder(row).reverse().toString().split("");
        }

    }

    public static void main(String[] args) {
        long resultA = calcule_load();
        System.out.println(resultA);

        ground = init_ground();

        long resultB = calcule_load_cycles();
        System.out.println(resultB);


    }

}
