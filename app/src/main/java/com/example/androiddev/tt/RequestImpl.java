package com.example.androiddev.tt;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by androiddev on 4/3/17.
 */

public class RequestImpl extends StringRequest {
    private Map<String, String> mParams, mHeaders;

    public RequestImpl(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mParams = new HashMap<>();
        mHeaders = new HashMap<>();
    }

    public String getMethodString() {
        int method = getMethod();
        switch (method) {
            case Method.GET:
                return "GET";
            case Method.POST:
                return "POST";
            default: return "INVALID";
        }
    }

    public void setParam(String key, String value) {
        mParams.put(key, value);
    }

    public void addHeader(String key, String value) { mHeaders.put(key, value); }

    @Override
    public Map<String, String> getParams() {
        try {
            URL url = new URL(getUrl());
            String query = url.getQuery();
            Map<String, String> params = new HashMap<>();
            if (query != null) {
                String[] paramsString = query.split("&");
                for (int i = 0; i < paramsString.length; i++) {
                    String paramString = paramsString[i];
                    if (paramString.length() == 0)
                        break;
                    String[] keyValue = paramString.split("=");
                    if (keyValue.length == 2)
                        params.put(keyValue[0], keyValue[1]);
                }
            }
            if (getMethod() == Method.POST)
                params.putAll(mParams);
            return params;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void appendHeader(String header, String value) {
        String headerValue = mHeaders.get(header);
        if (headerValue == null) {
            mHeaders.put(header, value);
            return;
        }
        mHeaders.put(header, headerValue + value);
    }

    @Override
    public Map<String, String> getHeaders() { return mHeaders; }
}
