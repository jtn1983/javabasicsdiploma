package ru.netology.graphics;

import ru.netology.graphics.image.TextColorSchema;

public class ColorSchema implements TextColorSchema {
    @Override
    public char convert(int color) {
        char[] symbols = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
        return symbols[(int) Math.floor(color / 256. * symbols.length)];
    }
}
