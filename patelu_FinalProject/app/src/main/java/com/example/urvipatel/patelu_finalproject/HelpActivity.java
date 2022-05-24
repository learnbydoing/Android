package com.example.urvipatel.patelu_finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity implements Button.OnClickListener
{
    TextView backArrow;
    TextView helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        backArrow = (TextView) findViewById(R.id.back);
        helpText = (TextView) findViewById(R.id.helpText);
        backArrow.setText("\u02C2" + "\u02c2");
        backArrow.setTextColor(Color.RED);
        backArrow.setTextSize(24);
        backArrow.setTypeface(Typeface.DEFAULT_BOLD);
        backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent(HelpActivity.this, MainActivity.class);
        startActivityForResult(intent, 500);
        finish();
    }
}
