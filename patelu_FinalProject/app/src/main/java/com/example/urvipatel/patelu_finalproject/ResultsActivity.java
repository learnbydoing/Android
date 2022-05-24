package com.example.urvipatel.patelu_finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class ResultsActivity extends Activity {

    TextView messageTextView;
    int width;
    int height;
    String messageFromGameActivity= "";
    String modeFromGameActivityStr = "";
    private static int NEW_GAME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        messageTextView = (TextView) findViewById(R.id.message);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_results);


        layout.post(new Runnable() {
            @Override
            public void run() {
                width = layout.getWidth();
                height = layout.getHeight();
                ImageView img_animation = (ImageView) findViewById(R.id.homer);

                Intent intent = getIntent();

                if(intent != null)
                {
                    messageFromGameActivity = intent.getCharSequenceExtra("Result").toString();
                    modeFromGameActivityStr = intent.getCharSequenceExtra("GameMode").toString();
                    messageTextView.setText(messageFromGameActivity);

                    if (messageFromGameActivity.equals("You win!!")) {
                        img_animation.setImageResource(R.drawable.win);
                        layout.setBackgroundColor(Color.rgb(144, 212, 146));
                        messageTextView.setTextColor(Color.BLUE);
                    } else {
                        img_animation.setImageResource(R.drawable.lose);
                        layout.setBackgroundColor(Color.WHITE);
                        messageTextView.setTextColor(Color.RED);
                    }
                }

                TranslateAnimation animation = new TranslateAnimation(width/2, width/2, height, 100);
                animation.setDuration(2000);
                animation.setFillAfter(true);
                img_animation.startAnimation(animation);

                TranslateAnimation animation2 = new TranslateAnimation(0, width/2, 100, 100);
                animation2.setDuration(2000);
                animation2.setFillAfter(true);
                messageTextView.startAnimation(animation2);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        createDialog();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    private void createDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String newGameOf = getString(R.string.dialog_message);
        builder.setMessage(newGameOf + " " + modeFromGameActivityStr.toUpperCase() + "?");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(ResultsActivity.this, GameActivity.class);
                intent.putExtra("Mode", modeFromGameActivityStr);
                startActivityForResult(intent, NEW_GAME);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                startActivityForResult(intent, NEW_GAME);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
