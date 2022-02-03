package com.lemondev.moviesapp.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lemondev.moviesapp.R;
import com.lemondev.moviesapp.models.MovieModel;
import com.lemondev.moviesapp.utils.Credentials;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    //type
    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH = 2;


    public MovieRecyclerViewAdapter(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;
        if (viewType == DISPLAY_SEARCH) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            return new MovieViewHolder(view, onMovieListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_pop_list_item, parent, false);
            return new PopMovieViewHolder(view, onMovieListener);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if (itemViewType == DISPLAY_SEARCH) {
            //设置参数
            ((MovieViewHolder) holder).title.setText(mMovies.get(position).getTitle());
            ((MovieViewHolder) holder).release_date.setText(mMovies.get(position).getRelease_date());

            //this is an error in runtime
            ((MovieViewHolder) holder).vote_count.setText(String.valueOf(mMovies.get(position).getVote_count()));

            //vote is over 10 ,our rating bar is 5.
            ((MovieViewHolder) holder).ratingBar.setRating(mMovies.get(position).getVote_average() / 2);

            //ImageView: use glide lib
            Glide.with(holder.itemView.getContext())
                    .load(Credentials.BASE_PIC_URL + mMovies.get(position).getPoster_path())
                    .into(((MovieViewHolder) holder).imageView);
        } else {
            //vote is over 10 ,our rating bar is 5.
            ((PopMovieViewHolder) holder).ratingBar.setRating(mMovies.get(position).getVote_average() / 2);

            //ImageView: use glide lib
            Glide.with(holder.itemView.getContext())
                    .load(Credentials.BASE_PIC_URL + mMovies.get(position).getPoster_path())
                    .into(((PopMovieViewHolder) holder).imageView);
        }

    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR) {
            return DISPLAY_POP;
        } else {
            return DISPLAY_SEARCH;
        }
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    public MovieModel getSelectedMovie(int position) {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }
}
