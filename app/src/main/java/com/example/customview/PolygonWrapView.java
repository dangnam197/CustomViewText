package com.example.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PolygonWrapView extends View {
    public enum WrapMode {
        Letters,
        Words
    }

    private Path mPath;
    private String mText;
    private float mFontSize;
    private int mTextColor;

    private Paint mPaint;
    private Bitmap mPathMap;
    private Paint mPaintRect;

    private WrapMode mWrapMode = WrapMode.Words;

    public PolygonWrapView(Context context) {
        super(context);
        init();
    }

    public PolygonWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PolygonWrapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mFontSize = 20;
        mTextColor = 0xFF000000;
        mPaintRect = new Paint();
        mPaintRect.setStyle(Paint.Style.STROKE);
        mPaintRect.setStrokeWidth(5);
        mPaintRect.setColor(Color.parseColor("#FFC107"));
    }

    public void setPath(Path path) {
        mPath = path;

        // invalidate the path map.
        mPathMap = null;
    }

    // This method converts the path into a bitmap which will be used to determine if a point is within the path
    private void generatePathMap() {
//        if (mPath != null) {
//            // the path map bitmap can have poor quality, we're only checking for color or no color in each pixel.
//            mPathMap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_4444);
//
//            Canvas canvas = new Canvas(mPathMap);
//
//            Paint pathPaint = new Paint();
//            pathPaint.setStyle(Paint.Style.FILL);
//            pathPaint.setColor(Color.BLUE);
//
//            // draw the path.
//            canvas.drawPath(mPath, pathPaint);
//        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        int maxHeight = 1000;
        int maxWidth = 1000;
        float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        mPathMap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setFontSize(float fontSize) {
        mFontSize = fontSize;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public void setWrapMode(WrapMode wrapMode) {
        mWrapMode = wrapMode;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // make sure we have enough data to begin drawing text.
        if (mPath == null || mText == null || getMeasuredWidth() == 0 || getMeasuredHeight() == 0)
            return;

        // if the path map hasn't been generated, generate it now.
        if (mPathMap == null)
            generatePathMap();
        canvas.drawBitmap(mPathMap,0,0,null);

        final List<Rect> writableRects = getTextRects(canvas);
        final List<String> textFragments = getTextFragments();

        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mFontSize);

        int rectIndex = 0;
        int fragmentIndex = 0;
        Rect rect = null;
        String textFragment = null;
        float textWidth;

        // maybe find a better way to limit this loop?
        while (true) {
            // we don't have a rectangle. Get the next 1 in the list.
            if (rect == null) {
                // no more rectangles to draw text on. Finish.
                if (rectIndex >= writableRects.size())
                    return;

                rect = new Rect(writableRects.get(rectIndex));
                rectIndex++;
            }

            // we don't have text to print. Get the next word in the list.
            if (textFragment == null) {
                // no more text to draw. Finish.
                if (fragmentIndex >= textFragments.size())
                    return;

                textFragment = textFragments.get(fragmentIndex);
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
           // canvas.drawText(textFragment, rect.left, rect.centerY(), mPaint);

            // the word has been drawn. Set it null indicating a new 1 is needed in the next iteration.
            textFragment = null;

            // remove the used width from the rect and continue.
            rect.left += textWidth;

            // In word mode, account for the space that was removed.
            if (mWrapMode == WrapMode.Words) {
                rect.left += mPaint.measureText(" ");
            }
        }
    }

    // get each String fragment as a list. For letters mode, each element will be a letter or char. For words mode, each element will be a word.
    private List<String> getTextFragments() {
        List<String> result = new ArrayList<String>();

        if (mWrapMode == WrapMode.Letters) {
            for (int i = 0; i < mText.length(); i++) {
                result.add("" + mText.charAt(i));
            }
        } else if (mWrapMode == WrapMode.Words) {
            String[] words = mText.split("\\s+");

            for (String word : words)
                result.add(word);
        }


        return result;
    }

    private List<Rect> getTextRects(Canvas canvas) {
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
                rect.right = (int) (getMeasuredWidth() -mFontSize);
                result.add(rect);
            }
        }

        return result;
    }
}