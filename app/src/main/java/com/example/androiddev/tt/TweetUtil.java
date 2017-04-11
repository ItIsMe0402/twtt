package com.example.androiddev.tt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by androiddev on 4/6/17.
 */

public class TweetUtil {
    public Tweet[] handle(JSONArray jsonTweets) throws JSONException, ParseException {
        int count = jsonTweets.length();
        Tweet[] tweets = new Tweet[count];
        for (int i = 0; i < count; i++) {
            Tweet tweet = new Tweet();
            JSONObject status = jsonTweets.getJSONObject(i);

            Object text = status.get("text");
            if (text instanceof String)
                tweet.setMessage((String) text);

            Object createdAt = status.get("created_at");
            if (createdAt instanceof String)
                tweet.setCreatedAt(SimpleDateFormat.getInstance().parse((String) createdAt));

            handleUser(tweet, status.getJSONObject("user"));

            tweets[i] = tweet;
        }
        return tweets;
    }

    protected void handleUser(Tweet tweet, JSONObject jsonUser) throws JSONException {
        Tweet.User user = tweet.getUser();

        Object name = jsonUser.get("name");
        if (name instanceof String)
            user.setName((String) name);

        Object imgUrl = jsonUser.get("profile_image_url");
        if (imgUrl instanceof String)
            user.setImgUrl((String) imgUrl);
    }

    private TweetUtil() {}
}
