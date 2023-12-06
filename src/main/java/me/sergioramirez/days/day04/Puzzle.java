package me.sergioramirez.days.day04;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Puzzle {

    static String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day04\\input.txt";
    static List<String> input = InputTextUtil.read_all_lines(filepath);
    static List<Card> cards = new ArrayList<>();

    static void init_cards() {
        int id = 1;
        for(String line: input) {
            String numbers = line.split(":")[1];
            String winning_numbers_str = numbers.split("\\|")[0];
            String own_numbers_str = numbers.split("\\|")[1];
            List<Integer> winning_numbers = Arrays.stream(winning_numbers_str.split(" ")).filter(v -> !v.isEmpty()).map(String::trim).map(Integer::valueOf).toList();
            List<Integer> own_numbers = Arrays.stream(own_numbers_str.split(" ")).filter(v -> !v.isEmpty()).map(String::trim).map(Integer::valueOf).toList();
            cards.add(new Card(id, winning_numbers, own_numbers));
            id++;
        }
    }

    static Integer calculate_score() {
        return cards.stream().mapToInt(Card::calcule_worth_point).sum();
    }

    static Integer calculate_total_scratchcards() {
        int i = 0;
        for(Card card: cards) {
            int matches = card.determine_matches().size();
            for(int c = 0; c < card.getCopies(); c++) {
                for(int h=i; h < i+matches; h++) {
                    Card cardCopy = cards.get(h+1);
                    cardCopy.setCopies(cardCopy.getCopies()+1);
                }
            }
            i++;
        }
        return cards.stream().mapToInt(Card::getCopies).sum();
    }

    public static void main(String[] args) {
        init_cards();
        System.out.println(calculate_score());
        System.out.println(calculate_total_scratchcards());
    }
}
