package com.iomirea.http;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public final class GenericRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();

    private final Class<T> ClassType;

    private final Map<String, String> requestHeaders;

    private boolean muteRequest = false;

    private GenericRequest(int method, Class<T> classType, String url, String token, String requestBody, Response.Listener<T> listener,
                           Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener,  errorListener);
        ClassType = classType;

        this.requestHeaders = new HashMap<>();
        this.requestHeaders.put("Authorization", token);
        this.requestHeaders.put("User-Agent", "IOMirea-android-client 1");
    }

    GenericRequest(int method, String url, Class<T> classType, String token, Object toBeSent, Response.Listener<T> listener,
                   Response.ErrorListener errorListener) {
        this(method, classType, url, token, new Gson().toJson(toBeSent), listener,
                errorListener);
    }

    GenericRequest(int method, String url, Class<T> classType, String token, String requestBody, Response.Listener<T> listener,
                   Response.ErrorListener errorListener) {
        this(method, classType, url, token, requestBody, listener,
                errorListener);
    }

    GenericRequest(String url, Class<T> classType, String token, Response.Listener<T> listener,
                   Response.ErrorListener errorListener) {
        this(Request.Method.GET, url, classType, token, "", listener, errorListener);
    }

    public GenericRequest(int method, String url, Class<T> classType, String token, Object toBeSent, Response.Listener<T> listener,
                          Response.ErrorListener errorListener, boolean mute) {
        this(method, classType, url, token, new Gson().toJson(toBeSent), listener,
                errorListener);
        this.muteRequest = mute;

    }

    public GenericRequest(int method, String url, Class<T> classType, String token, String requestBody, Response.Listener<T> listener,
                          Response.ErrorListener errorListener, boolean mute) {
        this(method, classType, url, token, requestBody, listener,
                errorListener);
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
                T parsedObject = gson.fromJson(json, ClassType);
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