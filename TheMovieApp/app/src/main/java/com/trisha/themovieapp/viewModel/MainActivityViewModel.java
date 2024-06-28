package com.trisha.themovieapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.trisha.themovieapp.Movie;
import com.trisha.themovieapp.Model.MovieRepository;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MovieRepository repository;
    private  LiveData<List<Movie>> getAllMovies;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.repository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAllMovies(){
        return repository.getMutableLiveData();
    }
}
