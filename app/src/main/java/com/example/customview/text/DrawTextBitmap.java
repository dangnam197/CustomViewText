package com.example.customview.text;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import com.example.customview.PolygonWrapView;

import java.util.ArrayList;
import java.util.List;

public class DrawTextBitmap {
    private Bitmap bitmap;
    public enum WrapMode {
        Letters,
        Words
    }

    private Path mPath;
    private String mText;
    private float mFontSize = 30;
    private float mOldFontSize = 0;

    private int mTextColor = Color.BLACK;
    private Paint mPaint = new Paint();
    private Bitmap mPathMap;
    private Paint mPaintRect = new Paint();
    private List<String> textFragments;
    private PolygonWrapView.WrapMode mWrapMode = PolygonWrapView.WrapMode.Words;
    private ArrayList<DrawTextLine> listLine = new ArrayList<>();
    private boolean isChangeSize = true;
    public DrawTextListener drawTextListener;
    private static final String TAG = "DrawTextBitmap";
    public void setText(String text){
        this.mText = text;
        textFragments = getTextFragments();


    }
    public void setDrawTextListener(DrawTextListener drawTextListener) {
        this.drawTextListener = drawTextListener;
    }

    public Bitmap getBitmap() {
        return mPathMap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.mPathMap = bitmap;
    }

    public void run(){
        if (mPathMap == null || mText == null)
            return;

        // if the path map hasn't been generated, generate it now.
       // if (mPathMap == null)
//            generatePathMap();
//        canvas.drawBitmap(mPathMap,0,0,null);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mFontSize);
         List<Rect> writableRects = getTextRects();



        int rectIndex = 0;
        int fragmentIndex = 0;
        Rect rect = null;
        String textFragment = null;
        DrawTextLine drawTextLine = null;
        DrawText drawText = null;
        float textWidth;

        // maybe find a better way to limit this loop?
        listLine.clear();
        while (true) {
            // we don't have a rectangle. Get the next 1 in the list.
            if (rect == null) {
                // no more rectangles to draw text on. Finish.
                if (rectIndex >= writableRects.size()){
                    if (isChangeSize&&mFontSize > 4){
                        if(mFontSize - 1 == mOldFontSize) isChangeSize = false;
                        mOldFontSize = mFontSize;
                        mFontSize = mFontSize - 1;
                        Log.d(TAG, "run: "+mFontSize);
                        run();
                    }else {
                        if(drawTextListener!=null) drawTextListener.success();
                    }
                    break;
                }
//                    return;
                rect = new Rect(writableRects.get(rectIndex));
                drawTextLine = new DrawTextLine();
                drawTextLine.setY(rect.centerY());
                listLine.add(drawTextLine);
                rectIndex++;
            }

            // we don't have text to print. Get the next word in the list.
            if (textFragment == null) {
                // no more text to draw. Finish.
                if (fragmentIndex >= textFragments.size()){
                    if (isChangeSize&&mFontSize<100){
                        if(mFontSize + 1 == mOldFontSize) isChangeSize = false;
                        mOldFontSize = mFontSize;
                        mFontSize = mFontSize + 1;
                        Log.d(TAG, "run: "+mFontSize);
                        run();

                    }else {
                        if(drawTextListener!=null) drawTextListener.success();
                    }
                    break;
                }

                textFragment = textFragments.get(fragmentIndex);
                drawText = new DrawText(textFragment);
                fragmentIndex++;
            }

            // find how much width this text wants.
            textWidth = mPaint.measureText(textFragment);

            // if the rectangle doesn't have enough width, set it to null, indicating its "used up" and we need to next rect. Don't continue drawing text, find a new rect first.
            if (textWidth > rect.width()) {
                rect = null;
                continue;
            }

            // draw the text.
//            canvas.drawText(textFragment, rect.left, rect.centerY(), mPaint);
            drawText.setStartX(rect.left);
            drawTextLine.addText(drawText);
            drawTextLine.setEndLine((int) (rect.left + textWidth));

            // the word has been drawn. Set it null indicating a new 1 is needed in the next iteration.
            textFragment = null;

            // remove the used width from the rect and continue.
            rect.left += textWidth;

            // In word mode, account for the space that was removed.
            if (mWrapMode == PolygonWrapView.WrapMode.Words) {
                rect.left += mPaint.measureText(" ");
            }
        }
    }
    private List<String> getTextFragments() {
        List<String> result = new ArrayList<String>();

        if (mWrapMode == PolygonWrapView.WrapMode.Letters) {
            for (int i = 0; i < mText.length(); i++) {
                result.add("" + mText.charAt(i));
            }
        } else if (mWrapMode == PolygonWrapView.WrapMode.Words) {
            String[] words = mText.split("\\s+");

            for (String word : words)
                result.add(word);
        }


        return result;
    }

    private List<Rect> getTextRects() {
        final List<Rect> result = new ArrayList<Rect>();

        boolean isInPolygon = false;
        Rect rect = null;

        // place y in the center of the text, jump in fontsize steps.
        for (int y = (int) (mFontSize*2 ); y < mPathMap.getHeight(); y += mFontSize) {
            // place x at 0, jump with 5 px steps. This can be adjusted for better accuracy / performance.
            for (int x = (int) (mFontSize); x < mPathMap.getWidth()-mFontSize/2; x += 5) {
                // Havent found a point within the polygon yet, but now I have!
                if (!isInPolygon && mPathMap.getPixel(x, y) == 0) {
                    int y1 = y - (int) (mFontSize / 2);
                    int y2 = y + (int) (mFontSize / 2);
                    if(y1 < 0 || y2 > mPathMap.getHeight()||mPathMap.getPixel(x,y1) != 0 ||mPathMap.getPixel(x,y2)!=0) continue;
                    isInPolygon = true;
                    rect = new Rect((int) (x + mFontSize/2) , y - (int) (mFontSize / 2), x, y + (int) (mFontSize / 2));
                }
                // We found where the polygon started in this row, and now we found where it ends.
                else if (isInPolygon && mPathMap.getPixel(x, y) != 0) {
                    isInPolygon = false;
                    rect.right = (int) (x - mFontSize/2);

                    result.add(rect);
//                    canvas.drawRect(yhh,mPaintRect);
                }
            }

            // If the edge is in the ploygon, limit the rect to the right side of the view.
            if (isInPolygon) {
                rect.right = (int) (mPathMap.getWidth() -mFontSize);
                result.add(rect);
            }
        }

        return result;
    }
    public void draw(Canvas canvas){
        Log.d(TAG, "draw: "+listLine.size());
        for (DrawTextLine drawTextLine:listLine){
            drawTextLine.drawLine(canvas,mPaint);
        }
    }
    public interface DrawTextListener{
        public void success();
    }
}
