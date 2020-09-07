package com.example.customview.textdesign;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class DrawTextDesign {
    private String text = "Ngày 28/8, Trung Quốc cập nhật danh sách công nghệ bị hạn chế xuất khẩu";
    private ArrayList<DrawTextLine> listTextLine = new ArrayList<>();
    private Rect rect;

    public void randomText() {
        rect = null;
        listTextLine.clear();
        String[] list = text.split("\\s+");
        Random random = new Random();
        int range = 12;
        int start = 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        while (start < list.length) {
//            stringBuilder = new StringBuilder();
            if(start + range > list.length) range = list.length - start;
            int r = random.nextInt(range)+1;
            String[] line = new String[r];
            for (int i = start; i < start + r; i++) {
                line[i - start] = list[i];
            }
            DrawTextLine drawTextLine = new DrawTextLine();
            if(rect == null) {
                drawTextLine.random(line,0,0);
            }else {
                drawTextLine.random(line,0,rect.bottom);
            }
            listTextLine.add(drawTextLine);
            rect = drawTextLine.getRect();
//            Log.d("StringTess", "randomText: "+stringBuilder.toString());
            start += r;
            range = 12;
        }
    }
    public void draw(Canvas canvas){
        for (DrawTextLine drawTextLine : listTextLine){
            drawTextLine.draw(canvas);
        }
    }
}
