package me.sergioramirez.days.day24;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HailStone {
    private long x;
    private long y;
    private long z;
    private int velX;
    private int velY;
    private int velZ;

    private long a;
    private long b;
    private long c;

    public HailStone(long x, long y, long z, int velX, int velY, int velZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.a = velY;
        this.b = -velX;
        this.c = (velY * x) - (velX * y);
    }
}
