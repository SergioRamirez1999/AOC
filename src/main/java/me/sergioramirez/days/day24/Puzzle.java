package me.sergioramirez.days.day24;

import me.sergioramirez.util.InputTextUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day24\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final String REGEX_INPUT = "(\\d+), (\\d+), (\\d+) @ (-?\\d+), (-?\\d+), (-?\\d+)";
    private static final List<HailStone> hailstones = init_hailstones();

    private static List<HailStone> init_hailstones() {
        List<HailStone> hailstones = new ArrayList<>();

        Pattern pattern = Pattern.compile(REGEX_INPUT);

        for(String line: input) {
            Matcher matcher = pattern.matcher(line);
            if(!matcher.matches()) continue;
            long x = Long.parseLong(matcher.group(1));
            long y = Long.parseLong((matcher.group(2)));
            long z = Long.parseLong((matcher.group(3)));
            int velX = Integer.parseInt(matcher.group(4));
            int velY = Integer.parseInt(matcher.group(5));
            int velZ = Integer.parseInt(matcher.group(6));
            hailstones.add(new HailStone(x,y,z,velX,velY,velZ));
        }

        return hailstones;
    }

    public static long count_intersections(long min, long max) {
        long count = 0;

        for(int i=0; i < hailstones.size(); i++) {
            HailStone h1 = hailstones.get(i);

            for(int h=i+1; h < hailstones.size(); h++) {
                HailStone h2 = hailstones.get(h);
                long a2 = h1.getA();
                long b2 = h1.getB();
                long c2 = h1.getC();

                long a1 = h2.getA();
                long b1 = h2.getB();
                long c1 = h2.getC();

                if(new BigDecimal(a1).multiply(new BigDecimal(b2)).compareTo(new BigDecimal(b1).multiply(new BigDecimal(a2))) == 0) continue;

                BigDecimal denominator = new BigDecimal(a1).multiply(BigDecimal.valueOf(b2)).subtract(new BigDecimal(a2).multiply(BigDecimal.valueOf(b1)));

                BigDecimal numeratorX = new BigDecimal(c1).multiply(BigDecimal.valueOf(b2)).subtract(new BigDecimal(c2).multiply(BigDecimal.valueOf(b1)));
                BigDecimal x = numeratorX.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);

                BigDecimal numeratorY = new BigDecimal(c2).multiply(BigDecimal.valueOf(a1)).subtract(new BigDecimal(c1).multiply(BigDecimal.valueOf(a2)));
                BigDecimal y = numeratorY.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);

                if (x.compareTo(new BigDecimal(min)) >= 0 && x.compareTo(new BigDecimal(max)) <= 0 &&
                        y.compareTo(new BigDecimal(min)) >= 0 && y.compareTo(new BigDecimal(max)) <= 0) {
                    boolean matches = Stream.of(h1, h2).allMatch(hs -> ((x.subtract(new BigDecimal(hs.getX()))).multiply(new BigDecimal(hs.getVelX()))).compareTo(BigDecimal.ZERO) >= 0 &&
                            ((y.subtract(new BigDecimal(hs.getY()))).multiply(new BigDecimal(hs.getVelY()))).compareTo(BigDecimal.ZERO) >= 0);
                    if(!matches) continue;
                    count++;
                }
            }
        }

        return count;
    }

    /*
    Invoco script de python que posee la funcionalidad de resolver un sistema de ecuaciones lineales (sympy)
    pasando como parámetros las variables o símbolos y los datos de un segmento de las hailstones
    */
    public static long throw_rock() {

        List<String> inputArgs = getInputArgs();

        try {
            String pathSolver = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day24\\solve.py";
            String symbolicArgs = "xr,yr,zr,vxr,vyr,vzr";
            String argsEqs = String.join("|", inputArgs);
            ProcessBuilder processBuilder = new ProcessBuilder("python", pathSolver, symbolicArgs, argsEqs);
            Process p = processBuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return Long.parseLong(stdInput.lines().toList().get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static List<String> getInputArgs() {
        List<String> inputArgs = new ArrayList<>();

        for(int i=0; i < 3; i++) {
            HailStone hailStone = hailstones.get(i);
            long sx = hailStone.getX();
            long sy = hailStone.getY();
            long sz = hailStone.getZ();
            long vy = hailStone.getVelY();
            long vx = hailStone.getVelX();
            long vz = hailStone.getVelZ();

            String axisVelArgs = sx+","+sy+","+sz+","+vx+","+vy+","+vz;
            inputArgs.add(axisVelArgs);
        }
        return inputArgs;
    }

    public static void main(String[] args) {
        long result_A = count_intersections(200000000000000L, 400000000000000L);
        System.out.println(result_A);
        long result_B = throw_rock();
        System.out.println(result_B);

    }
}
