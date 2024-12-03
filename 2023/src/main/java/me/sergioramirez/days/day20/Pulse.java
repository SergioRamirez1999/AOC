package me.sergioramirez.days.day20;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pulse {
    private Module source;
    private Module destination;
    private String type;
}
