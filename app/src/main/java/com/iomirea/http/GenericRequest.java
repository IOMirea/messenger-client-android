package com.iomirea.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public final class GenericRequest<T> extends JsonRequest<T> {
    private final Gson gson = new Gson();
    private final Class<T> cls;
    private final Map<String, String> requestHeaders;

    private boolean muteRequest = false;

    private GenericRequest(int method, Class<T> classType, String url, String token, String requestBody,
                           final CallbackListener<T> callbackListener) {
        super(method, url, requestBody, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                callbackListener.callback(new Callback<T>(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callbackListener.callback(new Callback<T>(error));
            }
        });

        this.cls = classType;

        this.requestHeaders = new HashMap<>();
        this.requestHeaders.put("Authorization", token);
        this.requestHeaders.put("User-Agent", "IOMirea-android-client 1");
    }

    GenericRequest(int method, String url, Class<T> classType, String token, Object toBeSent,
                   final CallbackListener<T> callbackListener) {
        this(method, classType, url, token, new Gson().toJson(toBeSent), callbackListener);
    }

    GenericRequest(int method, String url, Class<T> classType, String token, String requestBody,
                   final CallbackListener<T> callbackListener) {
        this(method, classType, url, token, requestBody, callbackListener);
    }

    GenericRequest(String url, Class<T> classType, String token,
                   final CallbackListener<T> callbackListener) {
        this(Request.Method.GET, url, classType, token, "", callbackListener);
    }

    public GenericRequest(int method, String url, Class<T> classType, String token, Object toBeSent,
                          final CallbackListener<T> callbackListener, boolean mute) {
        this(method, classType, url, token, new Gson().toJson(toBeSent), callbackListener);
        this.muteRequest = mute;

    }

    public GenericRequest(int method, String url, Class<T> classType, String token, String requestBody,
                          final CallbackListener<T> callbackListener, boolean mute) {
        this(method, classType, url, token, requestBody, callbackListener);
        this.muteRequest = mute;

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        if (requestHeaders == null) {
            return super.getHeaders();
        } else {
            return requestHeaders;
        }
    }

    @Override
    protected final Response<T> parseNetworkResponse(NetworkResponse response) {
        if (muteRequest) {
            if (response.statusCode >= 200 && response.statusCode <= 299) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
        } else {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                T parsedObject = gson.fromJson(json, cls);
                return Response.success(parsedObject, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }

        return null;
    }
}