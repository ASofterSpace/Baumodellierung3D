package com.asofterspace.baumodellierung3d;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        if (targetButton != null) {
            targetButton.setBackgroundColor(selectedColor);
        }
    }

    private void initCanvas() {

        // first we need to be clear about the dimensions...
        int width = 1753;
        int height = 1753;

        // we now create the basic objects to work with - a bitmap and a canvas
        Bitmap baseBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas baseCanvas = new Canvas(baseBitmap);

        // we draw the floor plan
        Drawable floorplanPng = getResources().getDrawable(R.drawable.grundriss_noisy, null);
        floorplanPng.setBounds(0, 0, width, height);
        floorplanPng.draw(baseCanvas);

        // we assign our selfmade canvas
        View v = new FloorplanCanvas(getApplicationContext());
        v.draw(baseCanvas);

        // we show the resulting result
        ImageView iv = findViewById(R.id.iFloorplan);
        iv.setImageBitmap(baseBitmap);
    }

}
