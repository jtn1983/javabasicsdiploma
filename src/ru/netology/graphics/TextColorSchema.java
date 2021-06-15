package ru.netology.graphics;

public class TextColorSchema implements ru.netology.graphics.image.TextColorSchema {
    @Override
    public char convert(int color) {
        return color <= 31 ? '▇'
                : color <= 59 ? '●'
                : color <= 87 ? '◉'
                : color <= 115 ? '◍'
                : color <= 143 ? '◎'
                : color <= 171 ? '○'
                : color <= 199 ? '☉'
                : color <= 227 ? '◌' : '-';
    }
}
