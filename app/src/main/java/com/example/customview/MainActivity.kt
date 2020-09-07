package com.example.customview

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.text.DrawTextBitmap
import com.example.customview.textdesign.DrawTextDesign
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private var mPolygonWrapView: PolygonWrapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
      //  mPolygonWrapView = findViewById(R.id.polygonWrap);

        val text = "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
       // mPolygonWrapView?.setText(text);

        val path =  Path();

        // sample of adding a path with a bezier curve element
        path.moveTo(150f, 50f);
        path.lineTo(500f, 50f);
        path.cubicTo(700f, 300f, 400f, 600f, 800f, 1000f);
        path.lineTo(300f,400f)
        path.lineTo(150f, 1000f);
        path.lineTo(150f, 0f);

        // only needed when you don't close the path.
        path.close();

        mPolygonWrapView?.setPath(path);
        mPolygonWrapView?.setFontSize(40f);

        mPolygonWrapView?.setBackgroundColor(Color.WHITE);

        mPolygonWrapView?.invalidate();


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
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test)
        val maxHeight = 1000
        val maxWidth = 1000
        val scale = Math.min(
            maxHeight.toFloat() / bitmap.width,
            maxWidth.toFloat() / bitmap.height
        )

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        val mPathMap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        val drawTextBitmap = DrawTextBitmap()
        drawTextBitmap.bitmap = mPathMap
        drawTextBitmap.setText(text)
        drawTextBitmap.setDrawTextListener {
            Toast.makeText(this, "da xong", Toast.LENGTH_SHORT).show()
        }
       // test_view.setDrawTextBitmap(drawTextBitmap)
        test_view.start()
        drawTextBitmap.run()
        test_view.setOnClickListener {
            test_view.start()
        }
//        val drawTextDesign  = DrawTextDesign()
//        drawTextDesign.randomText()

       

//        val file = File(d?.fileDescriptor)
    }

}