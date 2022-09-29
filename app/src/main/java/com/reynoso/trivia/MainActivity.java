package com.reynoso.trivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.reynoso.trivia.controller.AppController;
import com.reynoso.trivia.data.AnswerListAsyncResponse;
import com.reynoso.trivia.data.Repository;
import com.reynoso.trivia.databinding.ActivityMainBinding;
import com.reynoso.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private int currentQuestion =-1;
    private List<Question> questionList;
    private Question question;
    private boolean isReady = true;
    private SharedPreferences sharedPreferences;
    private int score=0;
    private String MESSAGE_ID ="GET_SCORE";
    private boolean hasBeenAnswered = false;
    private int percentage = 0;
    private String message ="MAX_SCORE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        mainBinding.textViewCurrentScore.setText("Current Score: "+String.valueOf(score));
        questionList= Repository.getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void ProcessFinished(ArrayList<Question> questionArrayList) {
                updateQuestion();
            }
        });

        mainBinding.buttonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(true);
            }
        });

        mainBinding.buttonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAnswer(false);
            }
        });

        mainBinding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion();
                isReady=false;
            }
        });
        sharedPreferences = getSharedPreferences(this.MESSAGE_ID,MODE_PRIVATE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainBinding.textViewMaxScore.setText("Max score: "+sharedPreferences.getInt(message,0));
    }

    private void updateQuestion(){
        if (currentQuestion < questionList.size()-1 && isReady){
                currentQuestion++;
                mainBinding.textViewQuestionsCounter.setText(currentQuestion+1+"/"+questionList.size());
                question=questionList.get(currentQuestion);
                mainBinding.textViewQuestion.setText(question.getQuestion());
                mainBinding.textViewEasterEgg.setText("");
                hasBeenAnswered = false;
                percentage=0;
        }
        if (!isReady){
            shake();
        }
        isReady = false;
        if (currentQuestion == questionList.size()-1) {
            saveMaxScore();
            mainBinding.invalidateAll();
        }

    }
    private void checkAnswer(boolean answerP){
        if (answerP == question.isAnswer()){
            isReady=true;
            updateScore(100,true);
            fadeCardView();
            Utils.snackBar(this.mainBinding.getRoot(),"Correct!!!");
        }else {
            updateScore(-100,false);
            shake();
            Utils.snackBar(this.mainBinding.getRoot(),"Incorrect f@#$");
        }

    }
    private void updateScore(int update, boolean answer){
        if (isReady && !answer ){
            currentQuestion=-1;
            updateQuestion();
            if(score > 0)score=0;
            mainBinding.textViewEasterEgg.setText("Oh Sorry!! \n(\\_(\\\n" +
                    "(=°;°)=\n" +
                    "(,(\")(\")");
            percentage=0;
        }
        if (!isReady){
            if (percentage ==0) score+=update; else score+=update+update*percentage/100;
        }
        if (isReady && !hasBeenAnswered){
            score+=update;
        }
        this.hasBeenAnswered = true;
        mainBinding.textViewCurrentScore.setText("Current Score: "+String.valueOf(score));
    }
    private void fadeCardView(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(300);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mainBinding.textViewQuestion.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainBinding.textViewQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mainBinding.cardViewQuestion.startAnimation(alphaAnimation);
    }
    private void shake(){
        Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake_animation);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mainBinding.textViewQuestion.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainBinding.textViewQuestion.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mainBinding.cardViewQuestion.startAnimation(shake);
    }
    private void saveMaxScore(){
        if(score >= sharedPreferences.getInt(message,0)) {
            sharedPreferences.edit().putInt(message, score).apply();
        }
    }
}