package com.example.urvipatel.patelu_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements ImageView.OnClickListener
{
    private static final int SEND_RESULT = 100; // request code
    private static final int SEND_MODE = 200; // request code

    TextView help;
    ImageView ten;
    ImageView eleven;
    String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View parent = findViewById(R.id.activity_main);

        help = (TextView) findViewById(R.id.help);
        help.setOnClickListener(this);

        ten = (ImageView) findViewById(R.id.ten);
        eleven = (ImageView) findViewById(R.id.eleven);
        ten.setOnClickListener(this);
        eleven.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("ten") || v.getTag().equals("eleven")) {
            mode = (String) v.getTag();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("Mode", mode);
            startActivityForResult(intent, SEND_MODE);
        } else {
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivityForResult(intent, SEND_RESULT);
        }
    }
}
