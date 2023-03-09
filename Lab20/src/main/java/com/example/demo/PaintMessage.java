package com.example.demo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaintMessage {
    private String color;
    private int x;
    private int y;

    public PaintMessage() {}
    public PaintMessage(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}