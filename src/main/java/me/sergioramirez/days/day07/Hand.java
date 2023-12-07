package me.sergioramirez.days.day07;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hand implements Comparable<Hand> {
    private String cards;
    private Integer bidAmount;
    @ToString.Exclude
    private Integer strengthCategory;
    @ToString.Exclude
    private Integer strengthGlobally;

    @ToString.Exclude
    private Map<String, Integer> label_value;

    public Hand(String cards, Integer bidAmount, Map<String, Integer> label_value) {
        this.cards = cards;
        this.bidAmount = bidAmount;
        this.strengthCategory = 0;
        this.strengthGlobally = 0;
        this.label_value = label_value;
    }

    public String determine_type_hand() {
        String type;
        Map<String, Integer> card_count = new HashMap<>();
        for(String card: cards.split("")) {
            if(card_count.containsKey(card)) card_count.put(card, card_count.get(card) + 1);
            else card_count.put(card, 1);
        }

        List<Map.Entry<String, Integer>> sets = card_count.entrySet().stream().sorted(Map.Entry.comparingByValue((t1, t2) -> Integer.compare(t2, t1))).toList();

        switch (card_count.size()) {
            case 5:
                type = "High card";
                break;
            case 4:
                type = "One pair";
                break;
            case 3: //Conflicto, agarrar segundo elemento, si es 1 es un three of kind, si es un 2 es un two pair
                if(sets.get(1).getValue() == 2)
                    type = "Two pair";
                else type = "Three of kind";
                break;
            case 2: //Conflicto, agarrar ultimo elemento, si es 1 es un Four of a kind, si es un 2 es un Full house
                if(sets.get(sets.size()-1).getValue() == 2)
                    type = "Full house";
                else type = "Four of a kind";
                break;
            default:
                type = "Five of a kind";
                break;
        }
        return type;
    }

    @Override
    public int compareTo(Hand o) {
        int value = 0;
        for(int i = 0; i < cards.split("").length; i++){
            String c1 = cards.split("")[i];
            String c2 = o.getCards().split("")[i];
            Integer n1 = label_value.get(c1);
            Integer n2 = label_value.get(c2);
            Integer comp = n1.compareTo(n2);
            if(comp != 0) {
                value = comp;
                break;
            }
        }
        return value;
    }
}
