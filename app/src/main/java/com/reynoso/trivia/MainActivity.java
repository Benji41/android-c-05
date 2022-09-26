package com.reynoso.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.reynoso.trivia.controller.AppController;
import com.reynoso.trivia.data.Repository;
import com.reynoso.trivia.databinding.ActivityMainBinding;
import com.reynoso.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        Utils.toast("WASSSSUP");
        Utils.snackBar(mainBinding.getRoot(),"Noe");
        List<Question> questionList= Repository.getQuestions();
    }
}