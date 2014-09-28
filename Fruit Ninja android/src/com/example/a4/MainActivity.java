/**
 * CS349 Winter 2014
 */
package com.example.a4;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.a4complete.R;

public class MainActivity extends Activity {
    private Model model;
    private Model model2;
    private MainView mainView;
    private TitleView titleView;
    public static Point displaySize;
    public static TextView gameover;
    public static Button startB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setTitle("CS349 A4 Demo");

        // save display size
        Display display = getWindowManager().getDefaultDisplay();
        displaySize = new Point();
        display.getSize(displaySize);

        // initialize model
        model = new Model();
        model2 = new Model();

        // set view
        setContentView(R.layout.main);


        //set button
        //startB=new Button("start");


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // create the views and add them to the main activity
        titleView = new TitleView(this.getApplicationContext(), model2);
        ViewGroup v1 = (ViewGroup) findViewById(R.id.main_1);
        v1.addView(titleView);


        //set text
        gameover= (TextView) findViewById(R.id.gameover);
        gameover.setVisibility(View.INVISIBLE);
        gameover.bringToFront();

        startB = (Button) findViewById(R.id.start);
        startB.bringToFront();
        startB.setBackgroundColor(Color.BLUE);
        startB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mainView = new MainView(getApplicationContext(), model,model2);
                ViewGroup v2 = (ViewGroup) findViewById(R.id.main_2);
                v2.addView(mainView);

                // notify all views
                model.initObservers();

            }
        });

    }
}
