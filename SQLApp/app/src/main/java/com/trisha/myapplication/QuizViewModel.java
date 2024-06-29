package com.trisha.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {
    QuizRepository repo = new QuizRepository();
    LiveData<QuestionList> liveData;


    public QuizViewModel() {
        liveData  = repo.getQuestionsFromAPI();
    }

    public LiveData<QuestionList> getLiveData(){
        return liveData;
    }
}
