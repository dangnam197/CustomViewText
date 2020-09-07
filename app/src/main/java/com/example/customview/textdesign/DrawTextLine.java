package com.example.customview.textdesign;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class DrawTextLine {
    private String text;
    private int typeLine;
    private Random random = new Random();
    private ArrayList<DrawText> listDrawText = new ArrayList<>();
    private int top;
    private float defaultWidth = 1000f;
    private float startY;
    private float startX = 0;
    private float endY = 0;
    private float endX = 100;

    public void random(String[] texts, float x, float y) {
        startY = y;
        endY = y;
        StringBuilder stringBuilder1 = new StringBuilder();
        for (String s : texts) {
            stringBuilder1.append(s);
            stringBuilder1.append(" ");
        }
        Log.d("testTextLine2", stringBuilder1.toString() + " " + startY);
        if (texts.length < 3) {
            random(texts, 1);
            setSizeOneLine();
            return;
        }
        LineType lineType = LineType.randomLineType();
        switch (lineType) {
            case TYPE_1:
                random(texts, 1);
                setSizeOneLine();
                return;
            case TYPE_1_2_2:
                random(texts, 3);
                setSize();
                return;
            case TYPE_2_2_1:
                random(texts, 3);
                setSize221();
                return;
        }

    }

    private void setSizeOneLine() {
        float width = listDrawText.get(0).getSrcRect().width();
        float scale = defaultWidth / width;
        listDrawText.get(0).setScale(scale);
        listDrawText.get(0).setY(startY);
        listDrawText.get(0).setX(startX);
        endY = startY + listDrawText.get(0).getHeight();
        Log.d("testTextLine1", listDrawText.get(0).getText() +" start:"+startY+ "random: "+endY);



    }

    private void random(String[] texts, int size) {
        StringBuilder stringBuilder1 = new StringBuilder();
        for (String s : texts) {
            stringBuilder1.append(s);
            stringBuilder1.append(" ");
        }
        Log.d("testTextLine", size + " " + stringBuilder1.toString());

        int start = 0;
        if (size == 1) {
            DrawText drawText = new DrawText();
            drawText.setText(texts);
            listDrawText.add(drawText);
            return;
        }
        for (int i = 0; i < size; i++) {
            DrawText drawText = new DrawText();
            int r = random.nextInt(texts.length - start - (size - i - 1));
            if (r == 0) {
                r = 1;
            }
            if (i == 2) r = texts.length - start;
            String[] textRandom = new String[r];
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = start; j < start + r; j++) {
                textRandom[j - start] = texts[j];
                stringBuilder.append(texts[j]);
            }
            start = start + r;
            drawText.setText(textRandom);
            listDrawText.add(drawText);
        }
    }

    public Rect getRect() {
        Log.d("rect", "getRect: "+(int) startX +" - "+ (int) startY+" - "+ (int) endX+" - "+ (int) endY);
        return new Rect((int) startX, (int) startY, (int) endX, (int) endY);
    }

    public void draw(Canvas canvas) {
        for (DrawText drawText : listDrawText) {
            drawText.draw(canvas);
        }
    }

    public void setSize() {
        float x1 = listDrawText.get(0).getSrcRect().width();
        float x2 = listDrawText.get(1).getSrcRect().width();
        float x3 = listDrawText.get(2).getSrcRect().width();

        float y1 = listDrawText.get(0).getSrcRect().height();
        float y2 = listDrawText.get(1).getSrcRect().height();
        float y3 = listDrawText.get(2).getSrcRect().height();

        float f = x2 / x3;
        float b = defaultWidth / (x1 * (f * y3 + y2) / y1 + x2);
        float a = (defaultWidth - b * x2) / x1;

        listDrawText.get(0).setX(startX);
        listDrawText.get(0).setScale(a);
        listDrawText.get(1).setScale(b);
        listDrawText.get(2).setScale(b * f);
        float star = listDrawText.get(0).getWidth();
        listDrawText.get(1).setX(star);
        listDrawText.get(2).setX(star);

        listDrawText.get(0).setY(startY);
        listDrawText.get(1).setY(startY);
        listDrawText.get(2).setY(startY + listDrawText.get(1).getHeight());
        endY = listDrawText.get(0).getHeight() + startY;

    }

    public void setSize221() {
        float x1 = listDrawText.get(0).getSrcRect().width();
        float x2 = listDrawText.get(1).getSrcRect().width();
        float x3 = listDrawText.get(2).getSrcRect().width();

        float y1 = listDrawText.get(0).getSrcRect().height();
        float y2 = listDrawText.get(1).getSrcRect().height();
        float y3 = listDrawText.get(2).getSrcRect().height();

        float f = x1 / x2;
        float b = defaultWidth / (x3 * (f * y2 + y1) / y3 + x1);
        float a = (defaultWidth - b * x1) / x3;

        listDrawText.get(0).setX(startX);
        listDrawText.get(1).setX(startX);
        listDrawText.get(2).setScale(a);
        listDrawText.get(0).setScale(b);
        listDrawText.get(1).setScale(b * f);
        float star = listDrawText.get(0).getWidth();
//        listDrawText.get(1).setX(star);
        listDrawText.get(2).setX(star);

        listDrawText.get(0).setY(startY);
        listDrawText.get(2).setY(startY);
        listDrawText.get(1).setY(startY + listDrawText.get(0).getHeight());
        endY = listDrawText.get(2).getHeight() + startY;

    }
}
