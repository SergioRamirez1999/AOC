package me.sergioramirez.days.day04;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Card {

    private int id;
    private List<Integer> winning_numbers;
    private List<Integer> own_numbers;
    private int copies;

    public Card(int id, List<Integer> winning_numbers, List<Integer> own_numbers) {
        this.id = id;
        this.winning_numbers = winning_numbers;
        this.own_numbers = own_numbers;
        this.copies = 1;
    }

    public Integer calcule_worth_point() {
        Integer result = 0;
        List<Integer> matches = determine_matches();
        if(!matches.isEmpty())
            result = IntStream.rangeClosed(1, matches.size()).reduce((acc, v) -> acc * 2).getAsInt();
        return result;
    }

    public List<Integer> determine_matches() {
        return own_numbers.stream().filter(n -> winning_numbers.contains(n)).collect(Collectors.toList());
    }

}
