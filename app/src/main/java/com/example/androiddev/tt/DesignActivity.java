package com.example.androiddev.tt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Date;
import java.util.Random;

public class DesignActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);

        Tweet[] tweets = getTweets();
        mAdapter = new TweetAdapter(tweets);
        mRecyclerView.setAdapter(mAdapter);
    }

    private Tweet[] getTweets() {
        Tweet[] tweets = new Tweet[5];
        Random random = new Random();
        for (int i = 0; i < tweets.length; i++) {
            Tweet tweet = new Tweet();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            tweet.setMessage(new String(bytes));
            tweet.setCreatedAt(new Date(random.nextLong()));
            bytes = new byte[8];
            random.nextBytes(bytes);
            tweet.getUser().setName(new String(bytes));
            tweet.setId(random.nextInt());
            tweets[i] = tweet;
        }
        return tweets;
    }

}
