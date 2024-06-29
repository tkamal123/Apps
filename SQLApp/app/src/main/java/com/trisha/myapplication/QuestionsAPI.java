package com.trisha.myapplication;


import retrofit2.Call;
import retrofit2.http.GET;

public interface QuestionsAPI {
    @GET("myquizappapi.php") //endpoint
    Call<QuestionList> getQuestions();


}
