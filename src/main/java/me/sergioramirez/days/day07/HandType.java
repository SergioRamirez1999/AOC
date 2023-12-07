package me.sergioramirez.days.day07;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandType {
    private String type;
    private List<Hand> hands;
}
