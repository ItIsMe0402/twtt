package com.example.androiddev.tt;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by androiddev on 4/3/17.
 */

public class RequestUtil {
    private static final String CONSUMER_SECRET = "yqRKRZUfnmQiaHGaQyRQXBhksMQY7ecnh7M1yVOHf345KqdTk6";
    private static final String CONSUMER_KEY = "UipqN7KIwspph3ggEdIYcUSLQ";
    private static final String ACCESS_TOKEN = "848470734460551168-h1lbV2q3E5jsEYPaVafFmKWhf4NkJmz";
    private static final String ACCESS_SECRET = "pvkFe1YzQ2MUKomsEhEXfZILOri4bPe3yxse8M9Mmliux";
    private static final String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
    private static final String OAUTH_VERSION = "1.0";
    private RequestUtil() {}

    public static void authenticate(RequestImpl request, String consumerKey, String consumerSecret)
            throws NoSuchAlgorithmException, AuthFailureError, InvalidKeyException, MalformedURLException {
        authenticate(request, consumerKey, consumerSecret, "", "");
    }

    public static void authenticate(RequestImpl request, String consumerKey, String consumerSecret, String token, String tokenSecret)
            throws AuthFailureError, NoSuchAlgorithmException, InvalidKeyException, MalformedURLException {
        StringBuilder builder = new StringBuilder();//("OAuth ");
        Iterator<Map.Entry<String, String>> paramsIterator =
                getSortedParams(getSignedParams(request, consumerKey, consumerSecret, token, tokenSecret)).entrySet().iterator();
        while (paramsIterator.hasNext()) {
            Map.Entry<String, String> paramEntry = paramsIterator.next();
            builder.append(Uri.encode(paramEntry.getKey())).append("=")//append("=\"")
                    .append(Uri.encode(paramEntry.getValue())).append("&");//append("\", ");
        }
        int len = builder.length();
        builder.deleteCharAt(len - 1);
        request.addHeader("Authorization", builder.toString());
    }

    private static Map<String, String> getOAuthParams(String consumerKey, String token) {
        Map<String, String> oauthParamsMap = new HashMap<>();
        oauthParamsMap.put("oauth_nonce", getNonce());
        oauthParamsMap.put("oauth_consumer_key", consumerKey);
        oauthParamsMap.put("oauth_timestamp", getTimestamp());
        oauthParamsMap.put("oauth_version", OAUTH_VERSION);
        oauthParamsMap.put("oauth_signature_method", OAUTH_SIGNATURE_METHOD);
//        if (token != null && token.length() > 0)
//            oauthParamsMap.put("oauth_token", token);
//        else
            oauthParamsMap.put("oauth_callback", "oob");
        return oauthParamsMap;
    }

    private static Map<String, String> encodedParams(Map<String, String> params) {
        Map<String, String> encodedParams = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry param = iterator.next();
            encodedParams.put(Uri.encode((String) param.getKey()), Uri.encode((String) param.getValue()));
        }
        return encodedParams;
    }

    private static SortedMap<String, String> getSortedParams(Map<String, String> map) {
        String[] sortedKeys = map.keySet().toArray(new String[1]);
        Arrays.sort(sortedKeys);
        SortedMap<String, String> sortedMap = new TreeMap<>();
        for (String key : sortedKeys)
            sortedMap.put(key, map.get(key));
        return sortedMap;
    }

    @NonNull
    private static String paramsToString(Map<String, String> params) {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        int len = builder.length();
        if (len > 0)
            builder.deleteCharAt(len - 1);
        return builder.toString();
    }

    protected static String getBaseString(RequestImpl request, String consumerKey, String token) throws MalformedURLException {
        Map<String, String> allParams = getOAuthParams(consumerKey, token);
        allParams.putAll(request.getParams());
        Map<String, String> encodedParams = encodedParams(allParams);
        SortedMap<String, String> sortedParams = getSortedParams(encodedParams);
        String url = request.getUrl();
        int queryStart = url.indexOf('?');
        String host = queryStart == -1 ? url : url.substring(0, queryStart);
        return request.getMethodString() + "&" + Uri.encode(host) + "&" + Uri.encode(paramsToString(sortedParams));
    }

    private static String getNonce() {
        Random random = new Random();
        return (Long.toString(random.nextLong()) + Long.toString(random.nextLong())).replace("-", "");
    }

    @NonNull
    private static String getTimestamp() { return String.valueOf(System.currentTimeMillis() / 1000); }

    protected static Map<String, String> getSignedParams(RequestImpl request, String consumerKey, String consumerSecret, String token, String tokenSecret)
            throws NoSuchAlgorithmException, InvalidKeyException, MalformedURLException {
        Map<String, String> oauthParams = getOAuthParams(consumerKey, token);
        String base = getBaseString(request, consumerKey, token);
        Mac mac = Mac.getInstance("HmacSHA1");
        String signingKey = Uri.encode(consumerSecret) + "&" + Uri.encode(tokenSecret);
        mac.init(new SecretKeySpec(signingKey.getBytes(), "HmacSHA1"));
        oauthParams.put("oauth_signature", Base64.encodeToString(mac.doFinal(base.getBytes()), Base64.DEFAULT).trim());
        return oauthParams;
    }

    public static RequestImpl getAuthRequest() {
        RequestImpl request = new RequestImpl(Request.Method.POST, "https://api.twitter.com/oauth/request_token",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int i;
                        i = 0;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String s = new String(error.networkResponse.data);
                        error.printStackTrace();
                    }
                });
        try {
            try {
                authenticate(request, CONSUMER_KEY, CONSUMER_SECRET);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        return request;
    }

    public static RequestImpl getSearchRequest(String query) {
        String url = "https://api.twitter.com/1.1/search/tweets.json" + "?q=" + Uri.encode(query);
        RequestImpl request = new RequestImpl(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = response + "Breakpoint";
                        ;//TODO
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String data = new String(error.networkResponse.data);
                        error.printStackTrace();
                    }
                });
        try {
            authenticate(request, CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_SECRET);
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return request;
    }
}
