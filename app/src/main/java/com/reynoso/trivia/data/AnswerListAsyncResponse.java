package com.reynoso.trivia.data;

import com.reynoso.trivia.model.Question;

import java.util.ArrayList;

/**
 * Created by Noé Benjamín Reynoso Aguirre on 9/26/2022.
 */
public interface AnswerListAsyncResponse {
    void ProcessFinished(ArrayList<Question> questionArrayList);
}
