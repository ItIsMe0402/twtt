package com.example.androiddev.tt;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private static final String CONSUMER_KEY = "QMFY3mSYwOzA6TX7m0hEtrUHk";
    private static final String CONSUMER_SECRET = "JC8IPPIFYHNdedfwsXgZXecyVr8QTDMy8BkwoTRUsQXIXAcrIM";
    private static final String ACCESS_TOKEN = "848470734460551168-h1lbV2q3E5jsEYPaVafFmKWhf4NkJmz";
    private static final String ACCESS_SECRET = "pvkFe1YzQ2MUKomsEhEXfZILOri4bPe3yxse8M9Mmliux";
    private static final String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
    private static final String OAUTH_VERSION = "1.0";
    private RequestUtil() {}

    public static void authenticate(RequestImpl request, String consumerKey, String consumerSecret)
            throws AuthFailureError, InvalidKeyException {
        authenticate(request, consumerKey, consumerSecret, "", "");
    }

    public static void authenticate(RequestImpl request, String consumerKey, String consumerSecret, String token, String tokenSecret)
            throws AuthFailureError, InvalidKeyException {
        StringBuilder builder = new StringBuilder("OAuth ");
        Iterator<Map.Entry<String, String>> paramsIterator =
                getSignedParams(request, consumerKey, consumerSecret, token, tokenSecret).entrySet().iterator();
        while (paramsIterator.hasNext()) {
            Map.Entry<String, String> paramEntry = paramsIterator.next();
            builder.append(percentEncode(paramEntry.getKey())).append("=\"")
                    .append(percentEncode(paramEntry.getValue())).append("\", ");
        }
        int len = builder.length();
        builder.delete(len - 2, len);
        request.addHeader("Authorization", builder.toString());
    }

    private static Map<String, String> getOAuthParams(String consumerKey, String token) {
        Map<String, String> oauthParamsMap = new TreeMap<>();
        if (token != null && token.length() > 0)
            oauthParamsMap.put("oauth_token", token);
        else
            oauthParamsMap.put("oauth_callback", "oob");
        oauthParamsMap.put("oauth_consumer_key", consumerKey);
        oauthParamsMap.put("oauth_nonce", getNonce());
        oauthParamsMap.put("oauth_signature_method", OAUTH_SIGNATURE_METHOD);
        oauthParamsMap.put("oauth_timestamp", getTimestamp());
        oauthParamsMap.put("oauth_version", OAUTH_VERSION);
        return oauthParamsMap;
    }

    public static String percentEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private static Map<String, String> encodedParams(Map<String, String> params) {
        Map<String, String> encodedParams = new HashMap<>();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry param = iterator.next();
            encodedParams.put(percentEncode((String) param.getKey()), percentEncode((String) param.getValue()));
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

    protected static String getBaseString(RequestImpl request, String consumerKey, String token) {
        Map<String, String> allParams = getOAuthParams(consumerKey, token);
        allParams.putAll(request.getParams());
        Map<String, String> encodedParams = encodedParams(allParams);
        SortedMap<String, String> sortedParams = getSortedParams(encodedParams);
        String url = request.getUrl();
        int queryStart = url.indexOf('?');
        String host = queryStart == -1 ? url : url.substring(0, queryStart);
        return request.getMethodString() + "&" + percentEncode(host) + "&" + percentEncode(paramsToString(sortedParams));
    }

    private static String getNonce() {
        Random random = new Random();
        return Long.toHexString(random.nextLong()) + Long.toHexString(random.nextLong());
    }

    @NonNull
    private static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    protected static Map<String, String> getSignedParams(RequestImpl request, String consumerKey, String consumerSecret, String token, String tokenSecret)
            throws InvalidKeyException {
        Map<String, String> oauthParams = getOAuthParams(consumerKey, token);
        String base = getBaseString(request, consumerKey, token);
        Mac mac = null;
        try {
            mac = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException e) {
        }
        String signingKey = percentEncode(consumerSecret) + "&" + percentEncode(tokenSecret);
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
            authenticate(request, CONSUMER_KEY, CONSUMER_SECRET);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        return request;
    }

    public static RequestImpl getSearchRequest(String query) {
        String url = "https://api.twitter.com/1.1/search/tweets.json" + "?q=" + percentEncode(query);
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
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return request;
    }

//    public static RequestImpl getTestRequest() {
//        RequestImpl request = new RequestImpl(Request.Method.POST, "https://api.twitter.com/oauth/request_token",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        String s = "OK";
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        String s = new String(error.networkResponse.data);
//                        error.printStackTrace();
//                    }
//                });
//        try {
//            authenticate(request, "xvz1evFS4wEEPTGEFPHBog", "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw",
//                    "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb"
//                    , "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE");
//        } catch (AuthFailureError authFailureError) {
//            authFailureError.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
//        return request;
//    }
}
