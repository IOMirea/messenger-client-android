package com.iomirea.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import java.util.HashMap;


public class HttpClient {
    private final String API_URL = "https://iomirea.ml/api/";
    private final String API_VERSION = "v0";
    private final String BASE_URL = API_URL + API_VERSION;

    private String token;

    private RequestQueue queue;

    public HttpClient(Context context, String token) {
        this.queue = VolleyController.getInstance(context).getRequestQueue();
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void send_message(Long channel_id, String content, Response.Listener<Message> callback) {
        String url = BASE_URL + "/channels/" + channel_id + "/messages";

        HashMap<String, String> body = new HashMap<>();
        body.put("channel_id", channel_id.toString());
        body.put("content", content);

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, Message.class, token, body, callback, null);

        queue.add(request);
    }

    public void get_message(Long channel_id, Long message_id, Response.Listener<Message> callback) {
        String url = BASE_URL + "/channels/" + channel_id + "/messages/" + message_id;

        GenericRequest request = new GenericRequest<>(url, Message.class, token, callback, null);

        queue.add(request);
    }

    public void get_messages(Long channel_id, Response.Listener<Message[]> callback) {
        String url = BASE_URL + "/channels/" + channel_id + "/messages";

        GenericRequest request = new GenericRequest<>(url, Message[].class, token, callback, null);

        queue.add(request);
    }

    public void create_channel(HashMap body, Response.Listener<Channel> callback) {
        String url = BASE_URL + "/channels";

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, Channel.class, token, body, callback, null);

        queue.add(request);
    }

    public void send_bugreport(HashMap body, Response.Listener<BugReport> callback) {
        String url = BASE_URL + "/bugreports";

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, null, token, body, callback, null);

        queue.add(request);
    }

    public void identify(Response.Listener<User> callback) {
        String url = BASE_URL + "/users/@me";

        GenericRequest request = new GenericRequest<>(url, User.class, token, callback, null);

        queue.add(request);
    }
}