package me.sergioramirez.days.day06;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Race {
    private Long time;
    private Long distance;
}
