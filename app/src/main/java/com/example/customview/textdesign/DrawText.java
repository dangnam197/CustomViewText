package com.example.customview.textdesign;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;

public class DrawText {
    private String text;
    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;
    private float scale = 1f;
    private Rect srcRect = new Rect();
    private TextPaint textPaint = new TextPaint();
    private Paint paint = new Paint();
    private final float defaultSize = 30;

    public DrawText() {
        textPaint.setTextSize(defaultSize);
        textPaint.setColor(Color.BLACK);
//        textPaint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setText(String[] texts) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String text : texts) {
            stringBuilder.append(text);
            stringBuilder.append(" ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        this.text = stringBuilder.toString();
        textPaint.getTextBounds(text,0,text.length(), srcRect);
//        Log.d("testTExt",this.text);
    }

    public Rect getSrcRect() {
        return srcRect;
    }


    public void draw(Canvas canvas){
        canvas.drawRect(x,y,x+width,y+height,paint);
        canvas.drawText(text,x - srcRect.left * scale*0.9f + 0.05f*width,y- srcRect.top*scale*0.9f +0.05f*height,textPaint);
//        Log.d("drawTest", "draw: "+srcRect.left +" - "+srcRect.top+" "+(x + width)+(y+height));
        Log.d("drawTest", "draw: "+x+" - "+y+" "+text);

    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.width = scale * srcRect.width();
        this.height = scale * srcRect.height();
        this.scale = scale;
        textPaint.setTextSize(defaultSize*scale*0.9f);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
