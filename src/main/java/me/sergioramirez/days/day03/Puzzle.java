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
        int y = 0;
        StringBuilder buffer = new StringBuilder();
        for(String line: input) { //467..114..
            List<Coordinate> coordinates = new ArrayList<>();
            for(int x=0; x < line.length(); x++) {
                String character = String.valueOf(line.charAt(x));
                if(character.matches("[\\d]")) { //si es un numero
                    buffer.append(character);
                    coordinates.add(new Coordinate(x, y));
                } else if(character.matches(".") && !buffer.isEmpty()) { //si es un punto y el buffer esta vacio
                    int number = Integer.parseInt(buffer.toString());
                    PartNumber partNumber = new PartNumber(number, new ArrayList<>(coordinates), false);
                    partNumbers.add(partNumber);
                    coordinates.clear();
                    buffer.delete(0, buffer.length());
                } else if(!character.matches("\\.")) { //si no es un punto
                    symbols.add(new Coordinate(x, y));
                }
            }
            y++;
        }
        cols = input.get(0).length();
        rows = input.size();
    }

    private static List<Integer> determine_adjacents() {
        List<Integer> adjacents_numbers = new ArrayList<>();
        for(Coordinate symcoord: symbols) {
            int x = symcoord.getX();
            int y = symcoord.getY();


            List<Coordinate> adjacents_symbol = new ArrayList<>(); //agregar coordinadas adyacentes al simbolo
            List<Integer> adjacents_numbers_symbol = new ArrayList<>();
            //verificar si algun listado de la misma fila, superior o inferior contiene algun match
            adjacents_numbers.addAll(adjacents_numbers_symbol);
         }
        return adjacents_numbers;
    }

    public static void main(String[] args) {

        init_engine_schematic();
        System.out.println(partNumbers);
    }

}
