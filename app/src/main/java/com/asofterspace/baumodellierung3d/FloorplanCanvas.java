package com.asofterspace.baumodellierung3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * The canvas for the floorplan on which the basic floorplan image will be loaded, and on which
 * the user will be able to draw to actually realize walls, windows etc.
 */

public class FloorplanCanvas extends View {

    public FloorplanCanvas(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint wall = new Paint();
        wall.setColor(Color.BLACK);
        canvas.drawRect(0, 0, 200, 20, wall);
    }

}
