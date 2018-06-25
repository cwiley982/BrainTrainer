package com.caitlynwiley.braintrainer;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView scoreTextView;
    TextView timeLeftTextView;
    TextView equationTextView;
    int[] answerButtons = {R.id.choice1, R.id.choice2, R.id.choice3, R.id.choice4};
    TextView feedbackTextView;
    Button playAgainButton;
    Button goButton;
    RelativeLayout gameLayout;

    int totalQuestions;
    int questionsCorrect;
    int answer;
    int num1;
    int num2;
    int[] choices;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.scoreTextView);
        timeLeftTextView = findViewById(R.id.timeLeftTextView);
        equationTextView = findViewById(R.id.equationText);
        feedbackTextView = findViewById(R.id.feedbackTextView);
        playAgainButton = findViewById(R.id.playAgainButton);
        goButton = findViewById(R.id.goButton);
        gameLayout = findViewById(R.id.gameLayout);

        totalQuestions = 0;
        questionsCorrect = 0;
        choices = new int[4];
        rand = new Random();

        goButton.setVisibility(View.VISIBLE);
        gameLayout.setVisibility(View.INVISIBLE);
    }

    public void startGame(View view) {
        for (int i = 0; i < 4; i++) {
            ((Button) findViewById(answerButtons[i])).setEnabled(true);
        }
        totalQuestions = 0;
        questionsCorrect = 0;
        timeLeftTextView.setText("30s");
        feedbackTextView.setText("");
        playAgainButton.setVisibility(View.INVISIBLE);
        //reset timer
        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftTextView.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                timeLeftTextView.setText("0s");
                //print score in feedback text view
                feedbackTextView.setText("Your score: " + scoreTextView.getText());
                //enable play again button
                playAgainButton.setVisibility(View.VISIBLE);
                //disable choice buttons
                for (int i = 0; i < 4; i++) {
                    ((Button) findViewById(answerButtons[i])).setEnabled(false);
                }
            }
        }.start();
        //reset score
        scoreTextView.setText("0/0");
        nextQuestion();
    }

    private void nextQuestion() {
        choices = new int[4];
        //get next equation and answer
        num1 = rand.nextInt(25) + 1;
        num2 = rand.nextInt(25) + 1;
        answer = num1 + num2;
        choices[rand.nextInt(4)] = answer;

        //populate array with options and put options on buttons
        for (int i = 0; i < 4; i++) {
            //don't overwrite answer
            if (choices[i] == 0) {
                choices[i] = rand.nextInt(50) + 1;
                for (int j = 0; j < i; j++) {
                    if (choices[i] == choices[j]) {
                        //generate a new number
                        choices[i] = rand.nextInt(50) + 1;
                        //effectively restarts the loop
                        j = -1;
                    }
                }
            }
            ((Button) findViewById(answerButtons[i])).setText(String.valueOf(choices[i]));
        }

        //fill equationTextView
        equationTextView.setText(num1 + " + " + num2);
    }

    public void checkAnswer(View view) {
        //check which option they chose and show whether they got it right or wrong, then update the score
        String feedback;
        if (Integer.parseInt(((Button) view).getText().toString()) == answer) {
            feedback = "Correct!";
            questionsCorrect++;
        } else {
            feedback = "Wrong!";
        }
        totalQuestions++;
        feedbackTextView.setText(feedback);
        scoreTextView.setText(questionsCorrect + "/" + totalQuestions);
        nextQuestion();
    }

    public void showGameScreen(View view) {
        goButton.setVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);

        startGame(view);
    }
}
