package com.asofterspace.baumodellierung3d;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.asofterspace.toolbox.Utils;

public class MainActivity extends AppCompatActivity {

    public final static String PROGRAM_TITLE = "Baumodellierung 3D";
    public final static String VERSION_NUMBER = "0.0.0.1(" + Utils.TOOLBOX_VERSION_NUMBER + ")";
    public final static String VERSION_DATE = "2. December 2018 - 3. December 2018";

    private Button bDrawWall;
    private Button bDrawWindow;
    private Button bDrawElectric;
    private Button bDrawSprinklers;

    private Paint currentPaint = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // let the Utils know in what program it is being used
        Utils.setProgramTitle(PROGRAM_TITLE);
        Utils.setVersionNumber(VERSION_NUMBER);
        Utils.setVersionDate(VERSION_DATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrangeButtons();

        initCanvas();

        toggleTo(null);
    }

    private void arrangeButtons() {

        bDrawWall = addToggleButton(R.id.rbDrawWall);
        bDrawWindow = addToggleButton(R.id.rbDrawWindow);
        bDrawElectric = addToggleButton(R.id.rbDrawElectric);
        bDrawSprinklers = addToggleButton(R.id.rbDrawSprinkler);

        Button bGenerateModel = findViewById(R.id.bGenerateModel);
        bGenerateModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTo(null);
            }
        });
    }

    private Button addToggleButton(int buttonId) {

        final Button resultButton = findViewById(buttonId);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTo(resultButton);
            }
        });
        return resultButton;
    }

    private void toggleTo(Button targetButton) {

        int defaultColor = getResources().getColor(R.color.colorPrimary);
        int selectedColor = getResources().getColor(R.color.colorPrimaryDark);

        bDrawWall.setBackgroundColor(defaultColor);
        bDrawWindow.setBackgroundColor(defaultColor);
        bDrawElectric.setBackgroundColor(defaultColor);
        bDrawSprinklers.setBackgroundColor(defaultColor);

        if (targetButton == null) {
            currentPaint = null;
            return;
        }

        targetButton.setBackgroundColor(selectedColor);

        if (targetButton.equals(bDrawWall)) {
            currentPaint = new Paint();
            currentPaint.setColor(Color.BLACK);
            currentPaint.setStrokeWidth(30);
        }

        if (targetButton.equals(bDrawWindow)) {
            currentPaint = new Paint();
            currentPaint.setColor(Color.CYAN);
            currentPaint.setStrokeWidth(30);
        }

        if (targetButton.equals(bDrawElectric)) {
            currentPaint = new Paint();
            currentPaint.setColor(Color.YELLOW);
            currentPaint.setStrokeWidth(15);
        }

        if (targetButton.equals(bDrawSprinklers)) {
            currentPaint = new Paint();
            currentPaint.setColor(Color.BLUE);
            currentPaint.setStrokeWidth(15);
        }
    }

    private void initCanvas() {

        // first we need to be clear about the dimensions...
        final int floorplanWidth = 1753;
        final int floorplanHeight = 1753;

        // we now create the basic objects to work with - a bitmap and a canvas
        final Bitmap baseBitmap = Bitmap.createBitmap(floorplanWidth, floorplanHeight, Bitmap.Config.ARGB_8888);
        final Canvas baseCanvas = new Canvas(baseBitmap);

        // we draw the floor plan
        final Drawable floorplanPng = getResources().getDrawable(R.drawable.grundriss_noisy, null);
        floorplanPng.setBounds(0, 0, floorplanWidth, floorplanHeight);
        floorplanPng.draw(baseCanvas);

        // we show the resulting result
        final ImageView iv = findViewById(R.id.iFloorplan);
        iv.setImageBitmap(baseBitmap);

        iv.setOnTouchListener(new View.OnTouchListener() {

            private boolean floorplanClicked = false;
            private float floorplanX;
            private float floorplanY;

            private float convertCoordX(float in) {
                return in * floorplanWidth / iv.getWidth();
            }

            private float convertCoordY(float in) {
                return in * floorplanHeight / iv.getHeight();
            }

            @Override
            public boolean onTouch(View v, MotionEvent me) {

                // no painting before a paint is selected ;)
                if (currentPaint == null) {
                    return true;
                }

                switch (me.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        floorplanClicked = true;
                        floorplanX = convertCoordX(me.getX());
                        floorplanY = convertCoordY(me.getY());
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (floorplanClicked) {
                            float nowX = convertCoordX(me.getX());
                            float nowY = convertCoordY(me.getY());
                            // only allow straight lines, such that the demo looks nice...
                            if (Math.abs(nowX - floorplanX) < Math.abs(nowY - floorplanY)) {
                                nowX = floorplanX;
                            } else {
                                nowY = floorplanY;
                            }
                            Bitmap previewBitmap = baseBitmap.copy(baseBitmap.getConfig(), true);
                            Canvas previewCanvas = new Canvas(previewBitmap);
                            previewCanvas.drawLine(floorplanX, floorplanY, nowX, nowY, currentPaint);
                            iv.setImageBitmap(previewBitmap);
                            iv.invalidate();
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        if (floorplanClicked) {
                            floorplanClicked = false;
                            float nowX = convertCoordX(me.getX());
                            float nowY = convertCoordY(me.getY());
                            // only allow straight lines, such that the demo looks nice...
                            if (Math.abs(nowX - floorplanX) < Math.abs(nowY - floorplanY)) {
                                nowX = floorplanX;
                            } else {
                                nowY = floorplanY;
                            }
                            baseCanvas.drawLine(floorplanX, floorplanY, nowX, nowY, currentPaint);
                            iv.setImageBitmap(baseBitmap);
                            iv.invalidate();
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        break;
                }

                return true;
            }
        });
    }

}
