package com.trisha.themovieapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.trisha.themovieapp.Movie;
import com.trisha.themovieapp.R;
import com.trisha.themovieapp.databinding.MovieItemListBinding;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Movie> arrayList;

    public MovieAdapter(Context context, ArrayList<Movie> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       MovieItemListBinding movieItemListBinding = DataBindingUtil.inflate(
               LayoutInflater.from(parent.getContext()),
                       R.layout.movie_item_list,
                       parent,
                       false);
       return new MyViewHolder(movieItemListBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = arrayList.get(position);
        holder.movieItemListBinding.setMovie(movie);
    }

    @Override
    public int getItemCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        else {
            return 0;
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        MovieItemListBinding movieItemListBinding;

        public MyViewHolder(@NonNull MovieItemListBinding movieItemListBinding) {
            super(movieItemListBinding.getRoot());
            this.movieItemListBinding = movieItemListBinding;

            movieItemListBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position  = getAdapterPosition();
                }
            });
        }

    }
}
