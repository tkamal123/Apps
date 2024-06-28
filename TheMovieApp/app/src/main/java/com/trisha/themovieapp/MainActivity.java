package com.trisha.themovieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.trisha.themovieapp.databinding.ActivityMainBinding;
import com.trisha.themovieapp.view.MovieAdapter;
import com.trisha.themovieapp.viewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MovieAdapter adapter;
    private RecyclerView rv;
    private ArrayList<Movie> movieArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityMainBinding mainBinding;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        getPopularMovies();
        swipeRefreshLayout = mainBinding.swipe;
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();

            }
        });

    }
    private void getPopularMovies(){
        viewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> moviesFromLiveData) {
                movieArrayList = (ArrayList<Movie>) moviesFromLiveData;
                displayMoviesInRecyclerView();

            }
        });

    }

    private void displayMoviesInRecyclerView(){
        rv = mainBinding.recyclerView;
        adapter = new MovieAdapter(this, movieArrayList );
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(this,2));
        adapter.notifyDataSetChanged();

    }


}