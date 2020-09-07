package com.example.customview.text;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawText {
    private String word = "";
    private int startX = 0;

    public DrawText(String word) {
        this.word = word;
    }

    public DrawText(String word, int startX) {
        this.word = word;
        this.startX = startX;
    }
    public void translateX(int x){
        startX += x;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void drawText(Canvas canvas, int y, Paint paint){
        canvas.drawText(word,startX,y,paint);
    }
}
