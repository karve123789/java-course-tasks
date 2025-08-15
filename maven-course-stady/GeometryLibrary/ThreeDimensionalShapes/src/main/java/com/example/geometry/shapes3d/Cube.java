package com.example.geometry.shapes3d;

public class Cube {
    private double side;

    public Cube(double side) {
        if (side <= 0) {
            throw new IllegalArgumentException("Сторона куба должна быть положительной");
        }
        this.side = side;
    }

    public double getVolume() {
        return side * side * side;
    }

    public double getSurfaceArea() {
        return 6 * side * side;
    }
}