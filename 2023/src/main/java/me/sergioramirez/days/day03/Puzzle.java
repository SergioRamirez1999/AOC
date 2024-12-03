package me.sergioramirez.days.day03;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Puzzle {
    static String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day03\\input.txt";
    static List<String> input = InputTextUtil.read_all_lines(filepath);
    static List<PartNumber> partNumbers = new ArrayList<>();
    static List<Coordinate> symbols = new ArrayList<>();
    static int cols;
    static int rows;

    private static void init_engine_schematic() {
        cols = input.get(0).length();
        rows = input.size();

        int y = 0;
        StringBuilder buffer = new StringBuilder();
        for(String line: input) { //467..114..
            List<Coordinate> coordinates = new ArrayList<>();
            for(int x=0; x < line.length(); x++) {
                char character = line.charAt(x);
                if(Character.isDigit(character)) {
                    buffer.append(character);
                    coordinates.add(new Coordinate(x, y, character));
                    if(x+1 == cols || !Character.isDigit(line.charAt(x+1))) {
                        int number = Integer.parseInt(buffer.toString());
                        PartNumber partNumber = new PartNumber(number, new ArrayList<>(coordinates), false);
                        partNumbers.add(partNumber);
                        coordinates.clear();
                        buffer.delete(0, buffer.length());
                    }
                } else if(character != '.') {
                    symbols.add(new Coordinate(x, y, character));
                }
            }
            y++;
        }

    }

    private static Integer determine_adjacents() {
        for(Coordinate symcoord: symbols) {
            int x = symcoord.getX();
            int y = symcoord.getY();

            for(int i=x-1;i<=x+1;i++){
                for(int j=y-1;j<=y+1;j++){
                    if(!(i<0 | j<0 | i>=rows | j>=cols)){
                        for(PartNumber part:partNumbers){
                            if(!part.isVisited()){
                                if(part.containCoordinate(new Coordinate(i,j))){
                                    part.setVisited(true);
                                }
                            }
                        }
                    }
                }
            }

         }
        return partNumbers.stream().filter(PartNumber::isVisited).mapToInt(PartNumber::getValue).sum();
    }

    private static Integer determine_gear_ratios() {
        List<Integer> gears_ratio = new ArrayList<>();
        for(Coordinate symcoord: symbols) {
            int x = symcoord.getX();
            int y = symcoord.getY();

            partNumbers.forEach(p -> p.setVisited(false));

            if(symcoord.getCharacter() == '*') {
                List<Integer> gears_symbol = new ArrayList<>();
                for (int i = x - 1; i <= x + 1; i++) {
                    for (int j = y - 1; j <= y + 1; j++) {
                        if (!(i < 0 | j < 0 | i >= rows | j >= cols)) {
                            for (PartNumber part : partNumbers) {
                                if(!part.isVisited())
                                    if (part.containCoordinate(new Coordinate(i, j))) {
                                        gears_symbol.add(part.getValue());
                                        part.setVisited(true);
                                    }
                            }
                        }
                    }
                }
                if(gears_symbol.size() == 2)
                    gears_ratio.add(gears_symbol.stream().reduce((acc, v) -> acc * v).get());

            }
        }

        return gears_ratio.stream().mapToInt(x -> x).sum();
    }

    public static void main(String[] args) {
        init_engine_schematic();
        int result_A = determine_adjacents();
        System.out.println(result_A);

        int result_B = determine_gear_ratios();
        System.out.println(result_B);
    }

}
