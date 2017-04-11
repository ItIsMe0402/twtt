package com.example.androiddev.tt;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by androiddev on 4/5/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {
    private Tweet[] mData;

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tweet_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(TweetAdapter.ViewHolder holder, int position) {
        Tweet tweet = mData[position];
        holder.tvCreatedAt.setText(tweet.getCreatedAt().toString());
        holder.tvMessage.setText(tweet.getMessage());
        holder.tvAccount.setText(tweet.getUser().getName());
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public TweetAdapter(Tweet[] data) {
        mData = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCreatedAt, tvMessage, tvAccount;
        public ViewHolder(ConstraintLayout constraintLayout) {
            super(constraintLayout);
            tvCreatedAt = (TextView) constraintLayout.findViewById(R.id.tvCreatedAt);
            tvMessage = (TextView) constraintLayout.findViewById(R.id.tvMessage);
            tvAccount = (TextView) constraintLayout.findViewById(R.id.tvAccount);
        }
    }
}
