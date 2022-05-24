package com.example.urvipatel.patelu_finalproject;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, ImageButton.OnClickListener
{
    ImageButton newGameButton;
    ImageButton backButton;
    ImageButton hintButton;
    Toolbar toolbar;

    private static final String TAG = "GameActivity";
    private static final int SEND_RESULT = 200; // request code
    private static final int NEW_GAME = 500; // request code

    float dX;
    float dY;

    int lastAction;
    int cardCount;
    int left;
    int top;
    int right;
    int bottom;

    int MODE;

    List<Integer> validSpots = new ArrayList<>();
    List<Integer> sumJQK = new ArrayList<>();

    Map<Integer, Integer> JQK = new HashMap<>();
    Map<Integer, String> mappings = new HashMap<>();


    int sumNum = 0;
    String outcome = "";
    String mode;

    int score;

    ImageButton dealPileButton;
    ImageView discardImage;
    ImageView c1;
    ImageView c2;
    ImageView c3;
    ImageView c4;
    ImageView c5;
    ImageView c6;
    ImageView c7;
    ImageView c8;
    ImageView c9;

    TextView modeIndicator;
    TextView scoreIndicator;
    TextView sumIndicator;
    TextView noMoves;

    ImageView[] board = new ImageView[9];

    // Scenarios for demo
    
    //Win
    //String[] cards = {"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13"};

    //Win and Place card on top of card just placed, same run
    //String[] cards = {"c11", "c12", "c13", "c2", "c8", "c3", "c7", "c3", "c7", "c10", "c11", "c12", "c13", "c3"};

    //Loss
    //String[] cards = {"s5", "d5", "d6", "c13", "s13", "d11", "h11", "d11", "c11", "c13", "c13", "c5", "c5"};

    //Immediate Loss
    //String[] cards = {"s6", "d6", "d1", "c13", "s13", "d11", "h11", "s11", "c11"}; //, "c13", "c13", "c5", "c5"};

    String[] cards = {"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13",
            "d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "d11", "d12", "d13",
            "h1", "h2", "h3", "h4", "h5", "h6", "h7", "h8", "h9", "h10", "h11", "h12", "h13",
            "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "s12", "s13"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.getBackground().setAlpha(75);
        newGameButton = (ImageButton) findViewById(R.id.newGameImageButton);
        backButton = (ImageButton) findViewById(R.id.backButton);
        hintButton = (ImageButton) findViewById(R.id.hintButton);
        newGameButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        hintButton.setOnClickListener(this);

        modeIndicator = (TextView) findViewById(R.id.mode);
        discardImage = (ImageView) findViewById(R.id.discardImage);
        discardImage.setImageResource(android.R.color.transparent);
        noMoves = (TextView) findViewById(R.id.noMoves);


        discardImage.setOnTouchListener(this);
        View parent = findViewById(R.id.activity_game);
        parent.post(new Runnable() {
            public void run() {
                Rect discardInitialRect = new Rect();
                discardImage.getHitRect(discardInitialRect);
                left = discardImage.getLeft();
                top = discardImage.getTop();
                right = discardImage.getRight();
                bottom = discardImage.getBottom();
                noMoves.setVisibility(View.INVISIBLE);
                noMoves.setTextSize(24);
                newGame();
            }
        });

        dealPileButton = (ImageButton) findViewById(R.id.dealPileButton);
        dealPileButton.setOnClickListener(this);

        newGameButton = (ImageButton) findViewById(R.id.newGameImageButton);
        newGameButton.setOnClickListener(this);

        c1 = (ImageView) findViewById(R.id.c1);
        c2 = (ImageView) findViewById(R.id.c2);
        c3 = (ImageView) findViewById(R.id.c3);
        c4 = (ImageView) findViewById(R.id.c4);
        c5 = (ImageView) findViewById(R.id.c5);
        c6 = (ImageView) findViewById(R.id.c6);
        c7 = (ImageView) findViewById(R.id.c7);
        c8 = (ImageView) findViewById(R.id.c8);
        c9 = (ImageView) findViewById(R.id.c9);

        scoreIndicator = (TextView) findViewById(R.id.score);
        sumIndicator = (TextView) findViewById(R.id.sum);
        sumIndicator.setText("");

        mappings.put(11, "J");
        mappings.put(12, "Q");
        mappings.put(13, "K");

        Intent intent = getIntent();
        if (intent != null) {
            mode = intent.getCharSequenceExtra("Mode").toString();
            MODE = mode.equals("ten") ? 10 : 11;
        }

    }//End onCreate


    @Override
    protected void onStart() {
        super.onStart();
        modeIndicator.setText(mode.toUpperCase());
        modeIndicator.setTextSize(24);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("dealPile"))
        {
            dealCard();
        }
        else if(v.getTag().equals("newGameToolbarButton"))
        {
            newGame();
        }
        else if(v.getTag().equals("backToolbarButton"))
        {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivityForResult(intent, SEND_RESULT);
        }
        else
        {
            showHints();
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                view.setY(event.getRawY() + dY);
                view.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:

                if (validSpots.isEmpty()) {
                    //Put discard back on discard pile
                    discardImage.setX(left);
                    discardImage.setY(top);
                    return true;
                }

                if(discardImage.getTag() == null)
                {
                    return true;
                }


                if (validSpots.isEmpty() && noMoves.getVisibility() == View.INVISIBLE) {
                    //noMoves.setVisibility(View.VISIBLE);
                    outcome = "You lose!!";

                    Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
                    intent.putExtra("Result", outcome);
                    intent.putExtra("GameMode", mode);
                    startActivityForResult(intent, SEND_RESULT);
                    dealPileButton.setVisibility(View.INVISIBLE);
                    discardImage.setVisibility(View.INVISIBLE);
                }

                Rect discardRect = new Rect();
                discardImage.getHitRect(discardRect);
                int discardImageResId = getResources().getIdentifier((String) discardImage.getTag(), "drawable", getPackageName());

                Payload payld = findFrame(discardRect);
                int spot;

                if (payld.index == -1) {
                    //Put discard back on discard pile
                    discardImage.setX(left);
                    discardImage.setY(top);
                    return true;
                }

                ImageView cardUnderneath = payld.imageView;

                if (!validSpots.contains(payld.index)) {
                    //Put discard back on discard pile
                    discardImage.setX(left);
                    discardImage.setY(top);
                    return true;
                }

                String cardUnderneathName = (String) cardUnderneath.getTag();
                int cardUnderneathVal = Integer.parseInt(cardUnderneathName.substring(1));

                if (cardUnderneathVal >= 11 && sumNum != 0)  //Put J, Q, or K after a non-face card
                {
                    discardImage.setX(left);
                    discardImage.setY(top);
                    return true;
                }


                if (cardUnderneathVal < 11 && sumJQK.isEmpty()) //Starting 10 or 11 run
                {
                    if (sumNum == 0) {
                        sumNum = sumNum + cardUnderneathVal;
                        sumIndicator.setText(String.format(Locale.getDefault(), "%d", sumNum));
                        cardUnderneath.setImageResource(discardImageResId);
                        cardUnderneath.setTag((discardImage.getTag()));
                        spot = validSpots.indexOf(payld.index);
                        validSpots.remove(spot);
                        score += 1;
                        scoreIndicator.setText(String.format(Locale.getDefault(), "%d", score));
                        discardImage.setImageResource(android.R.color.transparent);
                        discardImage.setTag(null);
                        //Put discard back on discard pile
                        discardImage.setX(left);
                        discardImage.setY(top);

                        if (sumNum == 10 && MODE == 10) //MODE is 10 so update score and return
                        {
                            sumNum = 0;
                            getValidSpots();
                            scoreIndicator.setText(String.format(Locale.getDefault(), "%d", score));
                            sumIndicator.setText(""); //Done with run, zero out indicator

                            if (validSpots.isEmpty() && noMoves.getVisibility() == View.INVISIBLE) {
                                //noMoves.setVisibility(View.VISIBLE);
                                outcome = "You lose!!";

                                Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
                                intent.putExtra("Result", outcome);
                                intent.putExtra("GameMode", mode);
                                startActivityForResult(intent, SEND_RESULT);
                                dealPileButton.setVisibility(View.INVISIBLE);
                                discardImage.setVisibility(View.INVISIBLE);
                            }
                        }
                        return true;
                    } else {
                        if (cardUnderneathVal + sumNum == MODE) //If 2 cards sum to MODE
                        {
                            sumNum += cardUnderneathVal;
                            //sumIndicator.setText(String.format(Locale.getDefault(), "%d", sumNum));
                            sumIndicator.setText("");  //Done with run, zero out indicator
                            score += 1;
                            scoreIndicator.setText(String.format(Locale.getDefault(), "%d", score));
                            cardUnderneath.setImageResource(discardImageResId);
                            cardUnderneath.setTag((discardImage.getTag()));
                            discardImage.setImageResource(android.R.color.transparent);
                            discardImage.setTag(null);
                            //Put discard back on discard pile
                            discardImage.setX(left);
                            discardImage.setY(top);
                            sumNum = 0;
                            getValidSpots();

                            if (validSpots.isEmpty() && noMoves.getVisibility() == View.INVISIBLE) {
                                //noMoves.setVisibility(View.VISIBLE);
                                outcome = "You lose!!";

                                Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
                                intent.putExtra("Result", outcome);
                                intent.putExtra("GameMode", mode);
                                startActivityForResult(intent, SEND_RESULT);
                                dealPileButton.setVisibility(View.INVISIBLE);
                                discardImage.setVisibility(View.INVISIBLE);
                            }
                            return true;
                        } else {
                            //Put discard back on discard pile
                            discardImage.setX(left);
                            discardImage.setY(top);
                        }
                    }
                } else {
                    if (cardUnderneathVal < 11) {
                        //Put discard back on discard pile
                        discardImage.setX(left);
                        discardImage.setY(top);
                        return true;
                    } else {
                        if (sumJQK.size() <= 1 && !sumJQK.contains(cardUnderneathVal)) //JQK run
                        {
                            cardUnderneath.setImageResource(discardImageResId);
                            cardUnderneath.setTag((discardImage.getTag()));
                            spot = validSpots.indexOf(payld.index);
                            validSpots.remove(spot);
                            discardImage.setImageResource(android.R.color.transparent);
                            discardImage.setTag(null);
                            //Put discard back on discard pile
                            discardImage.setX(left);
                            discardImage.setY(top);
                            sumJQK.add(cardUnderneathVal);
                            String sumIndicatorText = sumIndicator.getText() + mappings.get(cardUnderneathVal);
                            sumIndicator.setText(sumIndicatorText);
                            score += 1;
                            scoreIndicator.setText(String.format(Locale.getDefault(), "%d", score));
                            return true;
                        } else {
                            if (sumJQK.size() == 2 && !sumJQK.contains(cardUnderneathVal)) {
                                cardUnderneath.setImageResource(discardImageResId);
                                cardUnderneath.setTag(discardImage.getTag());
                                sumJQK.add(cardUnderneathVal);
                                //String sumIndicatorText = sumIndicator.getText() + mappings.get(cardUnderneathVal);
                                //sumIndicator.setText(sumIndicatorText);
                                sumIndicator.setText("");  //Done with run, zero out indicator
                                sumJQK.clear();
                                discardImage.setImageResource(android.R.color.transparent);
                                discardImage.setTag(null);
                                //Put discard back on discard pile
                                discardImage.setX(left);
                                discardImage.setY(top);
                                score += 1;
                                scoreIndicator.setText(String.format(Locale.getDefault(), "%d", score));
                                getValidSpots();

                                if (validSpots.isEmpty() && noMoves.getVisibility() == View.INVISIBLE) {
                                    //noMoves.setVisibility(View.VISIBLE);
                                    outcome = "You lose!!";

                                    Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
                                    intent.putExtra("Result", outcome);
                                    intent.putExtra("GameMode", mode);
                                    startActivityForResult(intent, SEND_RESULT);
                                    dealPileButton.setVisibility(View.INVISIBLE);
                                    discardImage.setVisibility(View.INVISIBLE);
                                }
                                return true;
                            } else {
                                //Put discard back on discard pile
                                discardImage.setX(left);
                                discardImage.setY(top);
                            }
                        }
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    } // End onTouch

    private void newGame()
    {
        shuffle();
        setupGame();
        cardCount = 0;
        createBoard();
        JQK.clear();
        sumNum = 0;
        score = 0;
        scoreIndicator.setText(String.format(Locale.getDefault(), "%d", score));
        sumIndicator.setText("");
        discardImage.setImageResource(android.R.color.transparent);
        getValidSpots();
        if(validSpots.isEmpty())
        {
            noMoves.setVisibility(View.VISIBLE);
            dealPileButton.setVisibility(View.INVISIBLE);
            discardImage.setVisibility(View.INVISIBLE);
        }
        else
        {
            noMoves.setVisibility(View.INVISIBLE);
            dealPileButton.setVisibility(View.VISIBLE);
            discardImage.setVisibility(View.VISIBLE);
        }

        discardImage.setTag(null);
    }//End newGame()

    private boolean JQKComplete()
    {
        return  sumIndicator.getText().equals("JQK") ||
                sumIndicator.getText().equals("JKQ") ||
                sumIndicator.getText().equals("QJK") ||
                sumIndicator.getText().equals("QKJ") ||
                sumIndicator.getText().equals("KJQ") ||
                sumIndicator.getText().equals("KQJ");
    }//End JQKComplete()

    private void getValidSpots()
    {
        validSpots.clear();
        for(int i = 0; i < 9; i++)
        {
            String cardName = (String) board[i].getTag();
            int cardValue = Integer.parseInt(cardName.substring(1));

            if(cardValue < 11)
            {
                checkSum(i, cardValue);
            }
            else
            {
                checkJQK(i, cardValue);
            }
        }
    }//End getValidSpots()

    private Payload findFrame(Rect discardRect)
    {
        Payload p = new Payload();

        for(int k = 0; k < 9; k++)
        {
            Rect boardCardRect = new Rect();
            board[k].getHitRect(boardCardRect);

            if (boardCardRect.intersect(discardRect) && validSpots.contains(k))
            {
                p.imageView = board[k];
                p.index = k;
                return p;
            }
        }
        p.imageView = discardImage;  // Set to null?
        p.index = -1;
        return p;
    }//End findFrame()

    private void checkSum(int i, int val)
    {
        int target = MODE - val;
        if(target == 0 && MODE == 10)
        {
            validSpots.add(i);
            return;
        }

        for(int j = 0; j < 9; j++)
        {
            String cardName2 = (String) board[j].getTag();
            int cardValue2 = Integer.parseInt(cardName2.substring(1));
            if(i != j && cardValue2 == target)
            {
                if(!validSpots.contains(i))
                {
                    validSpots.add(i);
                }
                if(!validSpots.contains(j))
                {
                    validSpots.add(j);
                }
            }
        }
    }//End checkSum()

    private void checkJQK(int i, int val)
    {
        JQK.clear();
        JQK.put(val, i);
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(11);
        arrayList.add(12);
        arrayList.add(13);

        arrayList.remove(arrayList.indexOf(val));

        for (int k = 0; k < 9; k++) {
            String cardName = (String) board[k].getTag();
            int cardValue = Integer.parseInt(cardName.substring(1));

            if (i != k && cardValue > 10) {
                if (arrayList.contains(cardValue)) //check if list is empty??
                {
                    JQK.put(cardValue, k);
                    arrayList.remove(arrayList.indexOf(cardValue));
                }
            }
        }
        if (JQK.size() == 3)
        {
            for(Map.Entry<Integer, Integer> entry : JQK.entrySet())
            {
                if(!validSpots.contains(entry.getValue()))
                {
                    validSpots.add(entry.getValue());
                }
            }
        }
    }//End checkJQK

    private void shuffle()
    {
        String tmp;
        int x;
        for(int i = 0; i < cards.length; i++)
        {
            Random random = new Random();
            x = random.nextInt(cards.length - 1);
            tmp = cards[i];
            cards[i] = cards[x];
            cards[x] = tmp;
        }
        Log.d(TAG, "Finished shuffling");
    }//End shuffle()

    private void setupGame()
    {
        board[0] = c1;
        board[1] = c2;
        board[2] = c3;
        board[3] = c4;
        board[4] = c5;
        board[5] = c6;
        board[6] = c7;
        board[7] = c8;
        board[8] = c9;
    }//End setupGame()

    private void createBoard()
    {
        for(int i = 0; i < 9; i++)
        {
            int id = getResources().getIdentifier(cards[i], "drawable", getPackageName());
            board[i].setImageResource(id);
            board[i].setTag(cards[i]);
            cardCount += 1;
        }
    }//End createBoard()

    private void dealCard()
    {
        if(cardCount == cards.length)
        {
            outcome = "You win!!";
            Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
            intent.putExtra("Result", outcome);
            intent.putExtra("GameMode", mode);
            dealPileButton.setVisibility(View.INVISIBLE);
            discardImage.setVisibility(View.INVISIBLE);
            startActivityForResult(intent, SEND_RESULT);
            finish();
            return;
        }
        if(discardImage.getTag() == null)
        {
            int id = getResources().getIdentifier(cards[cardCount], "drawable", getPackageName());
            discardImage.setImageResource(id);
            discardImage.setTag(cards[cardCount]);
            cardCount += 1;
            return;
        }
        if(validSpots.isEmpty() && noMoves.getVisibility() == View.INVISIBLE)
        {
            //noMoves.setVisibility(View.VISIBLE);
            outcome = "You lose!!";

            Intent intent = new Intent(GameActivity.this, ResultsActivity.class);
            intent.putExtra("Result", outcome);
            intent.putExtra("GameMode", mode);
            startActivityForResult(intent, SEND_RESULT);
            dealPileButton.setVisibility(View.INVISIBLE);
            discardImage.setVisibility(View.INVISIBLE);
        }
    }//End dealCard()

    private void showHints()
    {
        if(validSpots.isEmpty())
        {
            Toast.makeText(this, "No moves left", Toast.LENGTH_LONG).show();
            return;
        }

        if(!sumIndicator.getText().equals(""))
        {
            Toast.makeText(this, "Finish current run", Toast.LENGTH_SHORT).show();
            return;
        }

        int first = validSpots.get(0);
        String cardName1 = (String) board[first].getTag();
        int value1 = Integer.parseInt(cardName1.substring(1));

        if(value1 == 10 && MODE == 10)
        {
            blinkCards(board[first]);
            return;
        }

        if(value1 < 11)
        {
            blinkCards(board[first],getSecondCard(value1));
        }
        else
        {
            ImageView[] remFaceCards = getRemainingFaceCards(value1);
            blinkCards(board[first], remFaceCards[0], remFaceCards[1]);
        }
    }

    private ImageView getSecondCard(int value1)
    {
        for(int i = 1; i < validSpots.size(); i++)
        {
            String cardName2 = (String) board[validSpots.get(i)].getTag();
            int value2 = Integer.parseInt(cardName2.substring(1));

            if(value1 + value2 == MODE)
            {
                return board[validSpots.get(i)];
            }
        }
        return board[validSpots.get(0)];
    }

    private ImageView[] getRemainingFaceCards(int value1)
    {
        ImageView[] retViews = new ImageView[2];
        List<Integer> faceCards = new ArrayList<>();
        faceCards.add(value1);
        int i = 0;
            for (int j = 1; j < validSpots.size(); j++) {
                String cardName2 = (String) board[validSpots.get(j)].getTag();
                int value2 = Integer.parseInt(cardName2.substring(1));

                if (value2 >=  11 && !faceCards.contains(value2)) {
                    faceCards.add(value2);
                    retViews[i++] = board[validSpots.get(j)];
                }
                if (faceCards.size() == 3)
                {
                    return retViews;
                }
            }
        return null;
    }


    private void blinkCards(ImageView... cards)
    {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1);
        animation.setRepeatMode(Animation.REVERSE);

        for(int i = 0; i < cards.length; i++)
        {
            cards[i].startAnimation(animation);
        }
    }
}//End GameActivity()