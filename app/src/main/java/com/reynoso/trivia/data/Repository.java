package com.reynoso.trivia.data;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.reynoso.trivia.controller.AppController;
import com.reynoso.trivia.model.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noé Benjamín Reynoso Aguirre on 9/25/2022.
 */
public class Repository{
    private static ArrayList<Question> questionArrayList = new ArrayList<>();
    private static String URL = "https://opentdb.com/api.php?amount=10&type=boolean";

    public static List<Question> getQuestions(final AnswerListAsyncResponse callBack){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        questionArrayList.add(new Question(
                                jsonArray.getJSONObject(i).getString("question"),jsonArray.getJSONObject(i).getBoolean("correct_answer")
                        ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (callBack !=null){
                    callBack.ProcessFinished(questionArrayList);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Repository.class.getName(), "onErrorResponse: ", error);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return questionArrayList;
    }
}
