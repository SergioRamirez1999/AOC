package me.sergioramirez.days.day05;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seed {
    private Long number;
    private List<Long> destinations;

    public Seed(Long number) {
        this.number = number;
        this.destinations = new ArrayList<>();
    }
}
