package com.example.customview.text;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class DrawTextLine {
    private int widthLine = 0;
    private int endLine = 0;
    private int y;
    private ArrayList<DrawText> listDrawText = new ArrayList<>();
    public void drawLine(Canvas canvas, Paint paint){
        for (DrawText drawText : listDrawText){
            drawText.drawText(canvas,y,paint);
        }
    }
    public void addText(DrawText drawText){
        listDrawText.add(drawText);
    }

    public int getWidthLine() {
        return widthLine;
    }

    public void setWidthLine(int widthLine) {
        this.widthLine = widthLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
