package me.sergioramirez.days.day05;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transformation {
    private Long destination;
    private Long source;
    private Long offset;

}
