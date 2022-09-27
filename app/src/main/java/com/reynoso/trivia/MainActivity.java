package com.reynoso.trivia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
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
    }
    private void updateQuestion(){
        if (currentQuestion < questionList.size()-1 && isReady){
                currentQuestion++;
                mainBinding.textViewQuestionsCounter.setText(currentQuestion+1+"/"+questionList.size());
                question=questionList.get(currentQuestion);
                mainBinding.textViewQuestion.setText(question.getQuestion());
        }
        if (!isReady){
            shake();
        }
        isReady = false;

    }
    private void checkAnswer(boolean answerP){
        if (answerP == question.isAnswer()){
            isReady=true;
            fadeCardView();
            Utils.snackBar(this.mainBinding.getRoot(),"Correct!!!");
        }else {
            shake();
            Utils.snackBar(this.mainBinding.getRoot(),"Incorrect f@#$");
        }

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
}