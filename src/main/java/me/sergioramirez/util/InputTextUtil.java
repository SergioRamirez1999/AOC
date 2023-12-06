package me.sergioramirez.util;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InputTextUtil {

    public static List<String> read_all_lines(String filepath) {
        List<String> all_lines = new ArrayList<>();
        try {
            all_lines = Files.readAllLines(Paths.get(filepath), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all_lines;
    }

    public static String read_all_string(String filepath) {
        String text = "";
        try {
            text = Files.readString(Paths.get(filepath), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }
}
