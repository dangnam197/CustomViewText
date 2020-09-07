package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.customview.text.DrawTextBitmap;
import com.example.customview.textdesign.DrawTextDesign;

public class TestView extends View {
    DrawTextBitmap drawTextBitmap =new DrawTextBitmap();
    DrawTextDesign drawTextDesign = new DrawTextDesign();
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawTextBitmap getDrawTextBitmap() {
        return drawTextBitmap;
    }
    public void start(){
        drawTextDesign.randomText();
        invalidate();
    }
    public void setDrawTextBitmap(DrawTextBitmap drawTextBitmap) {
        this.drawTextBitmap = drawTextBitmap;
        this.drawTextBitmap.setDrawTextListener(new DrawTextBitmap.DrawTextListener() {
            @Override
            public void success() {
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        drawTextBitmap.draw(canvas);
        drawTextDesign.draw(canvas);
    }
}
